package repositories;

import java.util.List;

import com.google.inject.ImplementedBy;

import entities.Groups;
import entities.Member;
import entities.MembersGroup;
import io.ebean.annotation.Transactional;
import repositories.impl.ChatRoomReponsitoryImpl;

// TODO: Auto-generated Javadoc
/**
 * The Interface ChatRoomReponsitory.
 */
@ImplementedBy(ChatRoomReponsitoryImpl.class)
public interface ChatRoomReponsitory {

    /**
     * Find by name.
     *
     * @param username the username
     * @param password the password
     * @return the member
     * @throws Exception the exception
     */
    Member findByName(String username, String password) throws Exception;

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
     * @param id the id
     * @return the list
     * @throws Exception the exception
     */
    List<Groups> findAllGroup(Long id) throws Exception;

    /**
     * Find all user group.
     *
     * @param id the id
     * @return the list
     * @throws Exception the exception
     */
    public List<Groups> findAllUserGroup(Long id) throws Exception;

    /**
     * Select member group.
     *
     * @param id the id
     * @return the list
     * @throws Exception the exception
     */
    List<Member> selectMemberGroup(Long id) throws Exception;


    /**
     * Creates the group.
     *
     * @param groupName the group name
     * @param lstMemberId the lst member id
     * @throws Exception the exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void createGroup(String groupName, List<Long> lstMemberId) throws Exception;

    /**
     * Leave group.
     *
     * @param groupId the group id
     * @param memberId the member id
     * @throws Exception the exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void leaveGroup(Long groupId, Long memberId)throws Exception;

    /**
     * Gets the members group by id.
     *
     * @param groupId the group id
     * @param memberId the member id
     * @return the members group by id
     * @throws Exception the exception
     */
    public MembersGroup getMembersGroupById(Long groupId, Long memberId) throws Exception;

    /**
     * Gets the group by id.
     *
     * @param groupId the group id
     * @return the group by id
     * @throws Exception the exception
     */
    public Groups getGroupById(Long groupId) throws Exception;

    /**
     * Gets the list members group.
     *
     * @param groupId the group id
     * @return the list members group
     * @throws Exception the exception
     */
    public List<MembersGroup> getListMembersGroup(Long groupId)throws Exception;

}
