package controllers;

import javax.inject.Inject;

import bean.LoginFormBean;
import common.CommonConsts;
import entities.Member;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
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
		Member user = chatRoomService.checkLogin(loginForm.rawData().get(CommonConsts.USERNAME), loginForm.rawData().get(CommonConsts.PASSWORD));
		if(user == null){
			return ok("Login không thành công");
		}
		session(CommonConsts.ID, user.getId());
		session(CommonConsts.USERNAME, user.getUsername());
		session(CommonConsts.PASSWORD, user.getPassword());
		session(CommonConsts.ID);
		return ok("Login thành công");
	}
}
