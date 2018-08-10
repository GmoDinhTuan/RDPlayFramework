package repositories.impl;

import javax.inject.Singleton;

import entities.Member;
import repositories.ChatRoomReponsitory;

// TODO: Auto-generated Javadoc
/**
 * The Class ChatRoomReponsitoryImpl.
 */
@Singleton
public class ChatRoomReponsitoryImpl implements ChatRoomReponsitory {

	/* (non-Javadoc)
	 * @see repositories.ChatRoomReponsitory#findByName(java.lang.String, java.lang.String)
	 */
	@Override
	public Member findByName(String username, String password) throws Exception {
		Member user = Member.find.query().where().like("username", username).findOne();
		if (user.getPassword().equals(password)) {
			return user;
		}
		return null;
	}
}
