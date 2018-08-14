package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.webjars.play.WebJarsUtil;

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
import play.mvc.Controller;
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

    /** The user. */
    private Member user;

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
    public Result authenticate() throws Exception {
        Form<LoginFormBean> loginForm = formFactory.form(LoginFormBean.class).bindFromRequest();
        user = chatRoomService.checkLogin(loginForm.rawData().get(CommonConsts.USERNAME),
            loginForm.rawData().get(CommonConsts.PASSWORD));
        if (user == null) {
            return ok("Login không thành công");
        }
        return redirect("/index");
    }

    /**
     * Index.
     *
     * @return the result
     * @throws Exception the exception
     */
    public Result index() throws Exception {
        session(CommonConsts.ID, user.getId());
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
    public Result chat(String id, String type, String name, String description) throws Exception {
        List<Member> userList = chatRoomService.findUser();
        List<Groups> groupList = chatRoomService.findAllGroup();
        List<MembersGroup> memberGroup = new ArrayList<>();
        if (type.equals("group")) {
            memberGroup = chatRoomService.selectMemberGroup(id);
        }
        return ok(views.html.chatRoom.render(userList, groupList, memberGroup, description, name));
    }
}
