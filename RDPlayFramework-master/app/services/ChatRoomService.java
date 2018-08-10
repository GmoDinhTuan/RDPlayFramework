package services;

import com.google.inject.ImplementedBy;

import entities.Member;
import services.impl.ChatRoomServiceImpl;

// TODO: Auto-generated Javadoc
/**
 * The Interface ChatRoomService.
 */
@ImplementedBy(ChatRoomServiceImpl.class)
public interface ChatRoomService {
	
	/**
	 * Check login.
	 *
	 * @param username the username
	 * @param password the password
	 * @return the user
	 * @throws Exception the exception
	 */
	Member checkLogin(String username, String password) throws Exception;
	
}
