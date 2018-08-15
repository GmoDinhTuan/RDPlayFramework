package controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.persistence.EntityManager;

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
import entities.Groups;
import entities.Member;
import entities.MembersGroup;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.libs.F;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.WebSocket;
import services.ChatRoomService;

// TODO: Auto-generated Javadoc
/**
 * The Class Application.
 */
public class Application extends Controller {

    /** The chat room service. */
    private ChatRoomService chatRoomService;

    /** The form factory. */
    private FormFactory formFactory;

	private final JPAApi jpa;

    /** The user. */
    private Member user;

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
                          Materializer mat, ChatRoomService chatRoomService, FormFactory formFactory, JPAApi jpa) {
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
        this.jpa = jpa;
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
     * Authenticate.
     *
     * @return the result
     * @throws Exception the exception
     */
    public Result authenticate() throws Exception{
        Form<LoginFormBean> loginForm = formFactory.form(LoginFormBean.class).bindFromRequest();
        user = chatRoomService.checkLogin(loginForm.rawData().get(CommonConsts.USERNAME), loginForm.rawData().get(CommonConsts.PASSWORD));
        if(user == null){
            return ok("Login không thành công");
        }
        return redirect(controllers.routes.Application.index());
    }

    /**
     * Index.
     *
     * @return the result
     * @throws Exception the exception
     */
    public Result index() throws Exception {
        session(CommonConsts.ID, user.getId().toString());
        session(CommonConsts.USERNAME, user.getUsername());
        session(CommonConsts.PASSWORD, user.getPassword());
        List<Member> userList = chatRoomService.findUser();
        List<Groups> groupList = chatRoomService.findAllGroup();
        List<MembersGroup> memberGroup = new ArrayList<>();
        Http.Request request = request();
        String url = controllers.routes.Application.chatRoom().webSocketURL(request);
        return ok(views.html.chatRoom.render(userList, groupList, memberGroup, "",
            session(CommonConsts.USERNAME), url, session(CommonConsts.USERNAME)));
    }

    /**
     * Chat.
     *
     * @param id the id
     * @param type the type
     * @param name the name
     * @param description the description
     * @return the result
     * @throws Exception the exception
     */
    public Result chat(Long id, String type, String name, String description) throws Exception {
        List<Member> userList = chatRoomService.findUser();
        List<Groups> groupList = chatRoomService.findAllGroup();
        List<MembersGroup> memberGroup = new ArrayList<>();
        if (type.equals(CommonConsts.PARAM_GROUP)) {
            memberGroup = chatRoomService.selectMemberGroup(id);
        }
        Http.Request request = request();
        String url = controllers.routes.Application.chatRoom().webSocketURL(request);
        return ok(views.html.chatRoom.render(userList, groupList, memberGroup, description, name, url, session(CommonConsts.USERNAME)));
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
     * @return the web socket
     */
    public WebSocket chatRoom() {
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
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result searchMember() throws Exception{
    	List<Member> userList = chatRoomService.findUser();
        List<Groups> groupList = chatRoomService.findAllGroup();
        Map<String, List<?>> mapUserGroup = new HashMap<String, List<?>>();
        mapUserGroup.put("userList", userList);
        mapUserGroup.put("groupList", groupList);
        return ok(Json.toJson(mapUserGroup));

    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result addGroup(){
        JsonNode json = request().body()
                                 .asJson();
        String groupName = json.findPath("groupName").textValue();
        List<Long> lstMemberId = new ArrayList<Long>();
//        jpa.withTransaction(()->{
//
//
//        });
        json.findPath("lstMember").forEach((JsonNode node) -> {

    		lstMemberId.add(node.asLong());
//        	Member member = new Member();
//        	member.setId(node.asLong());
//        	Groups group = new Groups();
//        	group.setGroupsname(groupName);
//        	group.setStatus("1");
//        	group.insert();
//        	EntityManager em = jpa.em();
//
//        	MembersGroup membersGroup = new MembersGroup();
//        	membersGroup.setGroupId(group.getId());
//        	membersGroup.setMemberId(member.getId());
//        	membersGroup.setStatus("1");
//        	membersGroup.insert();
        });
        try {
        	chatRoomService.createGroup(groupName, lstMemberId);
        }catch(Exception e) {
        	e.printStackTrace();
        }



//        Member person = Json.fromJson(json, Person.class);
//        if(name == null) {
//            return badRequest("Missing parameter [code]");
//        } else {
//            return ok(toJson(name));
//        }

////        person.save();
        return ok(Json.toJson(lstMemberId));
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public Member getUser() {
        return user;
    }

    /**
     * Sets the user.
     *
     * @param user the new user
     */
    public void setUser(Member user) {
        this.user = user;
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

}
