package services.impl;

import java.util.List;

import javax.inject.Inject;

import entities.Member;
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
    public List<Member> findUser(String keyWord) throws Exception {
        List<Member> user = chatRoomReponsitory.findUser(keyWord);
        return user;
    }
}
