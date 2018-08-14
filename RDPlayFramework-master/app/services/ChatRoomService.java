package services;

import java.util.List;

import com.google.inject.ImplementedBy;

import entities.Groups;
import entities.Member;
import entities.MembersGroup;
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

	   /**
   	 * Find user.
   	 *
   	 * @return the list
   	 * @throws Exception the exception
   	 */
    List<Member> findUser() throws Exception;

    /**
     * Find all group.
     *
     * @return the list
     * @throws Exception the exception
     */
    List<Groups> findAllGroup() throws Exception;

    /**
     * Select member group.
     *
     * @param id the id
     * @return the list
     * @throws Exception the exception
     */
    List<MembersGroup> selectMemberGroup(String id) throws Exception;

}
