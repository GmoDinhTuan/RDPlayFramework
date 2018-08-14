package services.impl;

import java.util.List;

import javax.inject.Inject;

import entities.Groups;
import entities.Member;
import entities.MembersGroup;
import repositories.ChatRoomReponsitory;
import services.ChatRoomService;

// TODO: Auto-generated Javadoc
/**
 * The Class ChatRoomServiceImpl.
 */
public class ChatRoomServiceImpl implements ChatRoomService{

	/** The chat room reponsitory. */
	private ChatRoomReponsitory chatRoomReponsitory;

	/**
	 * Instantiates a new chat room service impl.
	 *
	 * @param chatRoomReponsitory the chat room reponsitory
	 */
	@Inject
	public ChatRoomServiceImpl(ChatRoomReponsitory chatRoomReponsitory) {
		this.chatRoomReponsitory = chatRoomReponsitory;
	}

	/* (non-Javadoc)
	 * @see services.ChatRoomService#checkLogin(bean.LoginFormBean)
	 */
	@Override
	public Member checkLogin(String username, String password) throws Exception {
		Member user = chatRoomReponsitory.findByName(username, password);
		if (user != null) {
			return user;
		}
		return null;
	}

	   /* (non-Javadoc)
     * @see services.ChatRoomService#findUser(java.lang.String, java.lang.String)
     */
    @Override
    public List<Member> findUser() throws Exception {
        List<Member> user = chatRoomReponsitory.findUser();
        return user;
    }

    /* (non-Javadoc)
     * @see services.ChatRoomService#findAllGroup()
     */
    @Override
    public List<Groups> findAllGroup() throws Exception {
        List<Groups> listGroups = chatRoomReponsitory.findAllGroup();
        return listGroups;
    }

    /* (non-Javadoc)
     * @see services.ChatRoomService#selectMemberGroup(java.lang.String)
     */
    @Override
    public List<MembersGroup> selectMemberGroup(String id) throws Exception {
        List<MembersGroup> listMemberGroups = chatRoomReponsitory.selectMemberGroup(id);
        return listMemberGroups;
    }
}
