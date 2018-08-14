package controllers;

import static play.libs.Json.toJson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import bean.LoginFormBean;
import common.CommonConsts;
import entities.Groups;
import entities.Member;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Http.Session;
import play.mvc.Result;
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
	
	private Member user;

	/**
	 * Instantiates a new student controller.
	 *
	 * @param chatRoomService the chat room service
	 * @param formFactory the form factory
	 */
	@Inject
	public Application(ChatRoomService chatRoomService, FormFactory formFactory) {
		this.chatRoomService = chatRoomService;
		this.formFactory = formFactory;
	}

	/**
	 * Login.
	 *
	 * @return the result
	 */
	public Result login(){
		session().clear();
		return ok(views.html.login.render(formFactory.form(LoginFormBean.class).fill(new LoginFormBean())));
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
		
		List<Member> userList = chatRoomService.findUser("");
		return redirect(controllers.routes.Application.index());
	}

    public Result index() throws Exception{
    	session().put(CommonConsts.ID, user.getId());
    	String id = session().get(CommonConsts.ID);
		session().put(CommonConsts.USERNAME, user.getUsername());
		session().put(CommonConsts.PASSWORD, user.getPassword());
        List<Member> userList = chatRoomService.findUser("");
        List<Groups> groupList = chatRoomService.findAllGroup();
        return ok(views.html.chatRoom.render(userList, groupList));
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public Result addGroup()
    {
        JsonNode json = request().body()
                                 .asJson();
        String groupName = json.findPath("groupName").textValue();
        List<Member> lstMember = new ArrayList<Member>();
        json.findPath("lstMember").forEach((JsonNode node) -> {
        	Member member = new Member();
        	member.setId(node.asText());
        	lstMember.add(member);
        });
//        Member person = Json.fromJson(json, Person.class);
//        if(name == null) {
//            return badRequest("Missing parameter [code]");
//        } else {
//            return ok(toJson(name));
//        }
        
////        person.save();
        return ok(Json.toJson(lstMember));
    }

	public Member getUser() {
		return user;
	}

	public void setUser(Member user) {
		this.user = user;
	}
    
    
}
