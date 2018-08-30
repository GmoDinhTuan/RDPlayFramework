package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Pair;
import akka.japi.pf.PFBuilder;
import akka.stream.Materializer;
import akka.stream.javadsl.BroadcastHub;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.MergeHub;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import bean.LoginFormBean;
import common.CommonConsts;
import dto.ChatRoomDto;
import entities.Groups;
import entities.Member;
import play.data.Form;
import play.data.FormFactory;
import play.libs.F;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.WebSocket;
import services.ChatRoomService;

/**
 * The Class Application.
 */
public class Application extends Controller {

    /** The chat room service. */
    private ChatRoomService chatRoomService;

    /** The form factory. */
    private FormFactory formFactory;

    /** The user flow. */
    private final Flow<String, String, NotUsed> userFlow;

    /**
     * Instantiates a new application.
     *
     * @param actorSystem the actor system
     * @param mat the mat
     * @param chatRoomService the chat room service
     * @param formFactory the form factory
     */
    @Inject
    public Application(ActorSystem actorSystem,
                          Materializer mat, ChatRoomService chatRoomService, FormFactory formFactory/*, @Named("userParentActor") ActorRef userParentActor*/) {
        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
        LoggingAdapter logging = Logging.getLogger(actorSystem.eventStream(), logger.getName());

        //noinspection unchecked
        @SuppressWarnings({"unchecked", "rawtypes"})
        Source<String, Sink<String, NotUsed>> source = MergeHub.of(String.class)
                .log("source", logging)
                .recoverWithRetries(-1, new PFBuilder().match(Throwable.class, e -> Source.empty()).build());
        Sink<String, Source<String, NotUsed>> sink = BroadcastHub.of(String.class);

        Pair<Sink<String, NotUsed>, Source<String, NotUsed>> sinkSourcePair = source.toMat(sink, Keep.both()).run(mat);
        Sink<String, NotUsed> chatSink = sinkSourcePair.first();
        Source<String, NotUsed> chatSource = sinkSourcePair.second();
        this.userFlow = Flow.fromSinkAndSource(chatSink, chatSource).log("userFlow", logging);
        this.chatRoomService = chatRoomService;
        this.formFactory = formFactory;
    }

    /**
     * Login.
     *
     * @return the result
     */
    public Result login() {
        session().clear();
        return ok(views.html.login
            .render(formFactory.form(LoginFormBean.class).fill(new LoginFormBean())));
    }

    /**
     * Logout.
     *
     * @return the result
     */
    public Result logout() {
         session().clear();
         return redirect(controllers.routes.Application.login());
    }

    /**
     * Authenticate.
     *
     * @return the result
     * @throws Exception the exception
     */
    public Result authenticate() throws Exception{
        Form<LoginFormBean> loginForm = formFactory.form(LoginFormBean.class).bindFromRequest();
        Member user = chatRoomService.checkLogin(loginForm.rawData().get(CommonConsts.USERNAME), loginForm.rawData().get(CommonConsts.PASSWORD));
        if(user == null){
            return redirect(controllers.routes.Application.login());
        }
        ChatRoomDto chatRoomDto = new ChatRoomDto();
        chatRoomDto.setRoomName(user.getUsername());
        session(CommonConsts.ID, user.getId().toString());
        session(CommonConsts.USERNAME, user.getUsername());
        session(CommonConsts.PASSWORD, user.getPassword());
        return redirect(controllers.routes.Application.index(user.getUsername()));
    }

    /**
     * Index.
     *
     * @param username the username
     * @return the result
     * @throws Exception the exception
     */
    public Result index(String username) throws Exception {
        if(session().get(CommonConsts.ID) != null && session().get(CommonConsts.ID).isEmpty() == false){
            Long userId = Long.valueOf(session().get(CommonConsts.ID));
            List<Member> userList = chatRoomService.findUser(userId);
            List<Groups> groupList = chatRoomService.findAllUserGroup(userId);
            List<Member> lstMemberInGroup = new ArrayList<Member>();
            Http.Request request = request();
            String url = controllers.routes.Application.chatRoom(username).webSocketURL(request);
            return ok(views.html.chatRoom.render(userList, lstMemberInGroup, groupList, CommonConsts.EMPTY, username, null, url, username));
        }else{
            return forbidden();
        }
    }

    /**
     * Chat.
     *
     * @param idTo the id to
     * @param type the type
     * @param name the name
     * @param description the description
     * @return the result
     * @throws Exception the exception
     */
    public Result chat(Long idTo, String type, String name, String description) throws Exception {
        if(session().get(CommonConsts.ID) != null && session().get(CommonConsts.ID).isEmpty() == false){
            Long userId = Long.valueOf(session().get(CommonConsts.ID));
            ChatRoomDto chatRoomDto = new ChatRoomDto();
            List<Member> userList = chatRoomService.findUser(userId);
            List<Groups> groupList = chatRoomService.findAllUserGroup(userId);
            List<Member> lstMemberInGroup = new ArrayList<Member>();
            Http.Request request = request();
            if (type.equals(CommonConsts.PARAM_GROUP)) {
                lstMemberInGroup = chatRoomService.selectMemberGroup(idTo);
                chatRoomDto.setRoomName(name);
                String url = controllers.routes.Application.chatRoom(name).webSocketURL(request);
                return ok(views.html.chatRoom.render(userList, lstMemberInGroup, groupList, description, name, idTo, url, session(CommonConsts.USERNAME)));
            } else {
                chatRoomDto.setRoomName(sortId(userId, idTo));
                String url = controllers.routes.Application.chatRoom(sortId(userId, idTo)).webSocketURL(request);
                return ok(views.html.chatRoom.render(userList, lstMemberInGroup, groupList, description, name, null, url, session(CommonConsts.USERNAME)));
            }
        }else{
            return forbidden();
        }

    }

    /**
     * Chat room js.
     *
     * @param name the name
     * @param url the url
     * @return the result
     */
    public Result chatRoomJs(String name, final String url) {
        return ok(views.js.chatRoom.render(name, url));
    }

    /**
     * Chat room.
     *
     * @param roomName the room name
     * @return the web socket
     */
    public WebSocket chatRoom(String roomName) {
        return WebSocket.Text.acceptOrResult(request -> {
            if (sameOriginCheck(request)) {
                return CompletableFuture.completedFuture(F.Either.Right(userFlow));
            } else {
                return CompletableFuture.completedFuture(F.Either.Left(forbidden()));
            }
        });
    }

    /**
     * Checks that the WebSocket comes from the same origin.  This is necessary to protect
     * against Cross-Site WebSocket Hijacking as WebSocket does not implement Same Origin Policy.
     *
     * See https://tools.ietf.org/html/rfc6455#section-1.3 and
     * http://blog.dewhurstsecurity.com/2013/08/30/security-testing-html5-websockets.html
     *
     * @param request the request
     * @return true, if successful
     */
    private boolean sameOriginCheck(Http.RequestHeader request) {
        @SuppressWarnings("deprecation")
        String[] origins = request.headers().get("Origin");
        if (origins.length > 1) {
            // more than one origin found
            return false;
        }
        String origin = origins[0];
        return originMatches(origin);
    }

    /**
     * Adds the group.
     *
     * @return the result
     * @throws Exception the exception
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result searchMember() throws Exception{
        if(session().get(CommonConsts.ID) != null && session().get(CommonConsts.ID).isEmpty() == false){
            Long userId = Long.valueOf(session().get(CommonConsts.ID));
            List<Member> userList = chatRoomService.findUser(userId);
            List<Groups> groupList = chatRoomService.findAllUserGroup(userId);
            Map<String, List<?>> mapUserGroup = new HashMap<String, List<?>>();
            mapUserGroup.put("groupList", groupList);
            mapUserGroup.put("userList", userList);
            return ok(Json.toJson(mapUserGroup));
        }else{
            return forbidden(Json.toJson("error"));
        }
    }

    /**
     * Leave group.
     *
     * @return the result
     * @throws Exception the exception
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result leaveGroup() throws Exception{
        if(session().get(CommonConsts.ID) != null && session().get(CommonConsts.ID).isEmpty() == false){
            Long userId = Long.valueOf(session().get(CommonConsts.ID));
            JsonNode json = request().body().asJson();
            Long groupId = json.findPath(CommonConsts.ID).asLong();
            try {
                chatRoomService.leaveGroup(groupId, userId);
            }catch(Exception e) {
                e.printStackTrace();
                return ok(Json.toJson("error"));
            }
            return redirect(controllers.routes.Application.index(session(CommonConsts.USERNAME)));
        }else{
            return forbidden(Json.toJson("error"));
        }
    }

    /**
     * Adds the group.
     *
     * @return the result
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result addGroup(){
        if(session().get(CommonConsts.ID) != null && session().get(CommonConsts.ID).isEmpty() == false){
            Long userId = Long.valueOf(session().get(CommonConsts.ID));
            JsonNode json = request().body().asJson();
            String groupName = json.findPath("groupName").textValue();
            List<Long> lstMemberId = new ArrayList<Long>();
            json.findPath("lstMember").forEach((JsonNode node) -> {
                lstMemberId.add(node.asLong());
            });
            lstMemberId.add(userId);
            try {
                chatRoomService.createGroup(groupName, lstMemberId);
            }catch(Exception e) {
                e.printStackTrace();
            }
            return ok(Json.toJson(lstMemberId));
        }else{
            return forbidden(Json.toJson("error"));
        }
    }

    /**
     * Origin matches.
     *
     * @param origin the origin
     * @return true, if successful
     */
    private boolean originMatches(String origin) {
        if (origin == null) return false;
        try {
            URI url = new URI(origin);
            return url.getHost().equals("localhost")
                   && (url.getPort() == 9090 || url.getPort() == 19001);
        } catch (Exception e ) {
            return false;
        }
    }

    /**
     * Write file from client to server.
     *
     * @return the result
     * @throws Exception the exception
     */
    public Result writeFile() throws Exception {
        MultipartFormData<File> body = request().body().asMultipartFormData();
        FilePart<File> file = body.getFile("files");
        if (file != null) {
            String fileName = file.getFilename();
            File srcFile = (File) file.getFile();
            byte[] fileData = new byte[(int) srcFile.length()];
            FileInputStream in = new FileInputStream(srcFile);
            in.read(fileData);
            in.close();
            File dirFile = new File("public\\files\\" + fileName);
            OutputStream os = new FileOutputStream(dirFile);
            os.write(fileData);
            printContent(dirFile);
            os.close();
            return ok("true");
        }
         return ok("false");
    }

    /**
     * Read file from server.
     *
     * @param fileName the file name
     * @return the result
     * @throws Exception the exception
     */
    public Result readFile(String fileName) throws Exception {
        File dirFile = new File("public\\files\\" + fileName);
        boolean inline = true;
        return ok(dirFile, inline);
    }

    /**
     * Prints the content file.
     *
     * @param file the file
     * @throws Exception the exception
     */
    public static void printContent(File file) throws Exception {
        System.out.println("Print File Content");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
    }

    /**
     * Sort id.
     *
     * @param fromId the from id
     * @param toId the to id
     * @return the string
     */
    private String sortId(Long fromId, Long toId) {
        String sortId;
        if(fromId >= toId) {
            sortId = fromId.toString() + toId.toString();
        } else {
            sortId = toId.toString() + fromId.toString();
        }
        return sortId;
    }
}
