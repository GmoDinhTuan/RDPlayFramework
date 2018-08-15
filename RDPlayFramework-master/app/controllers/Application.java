package controllers;

import static play.libs.Json.toJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Http.Session;
import play.mvc.Result;
import services.ChatRoomService;

/**
 * The Class Application.
 */
public class Application extends Controller {

    /** The chat room service. */
    private ChatRoomService chatRoomService;

    /** The form factory. */
    private FormFactory formFactory;

	
	private Member user;
	
	private final JPAApi jpa;

    @Inject
    public Application(ChatRoomService chatRoomService, FormFactory formFactory, JPAApi jpa) {
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
//		Session session = Http.Context.current().session();
		
		List<Member> userList = chatRoomService.findUser();
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
        return ok(views.html.chatRoom.render(userList, groupList, memberGroup, "",
            session(CommonConsts.USERNAME)));
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
        if (type.equals("group")) {
            memberGroup = chatRoomService.selectMemberGroup(id);
        }
        return ok(views.html.chatRoom.render(userList, groupList, memberGroup, description, name));
    }
    
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

	public Member getUser() {
		return user;
	}

	public void setUser(Member user) {
		this.user = user;
	}
    
    
}
