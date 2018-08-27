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
     * @return the list
     * @throws Exception the exception
     */
    List<Groups> findAllGroup() throws Exception;
    
    public List<Groups> findAllUserGroup(Long id) throws Exception;

    /**
     * Select member group.
     *
     * @param id the id
     * @return the list
     * @throws Exception the exception
     */
    List<Member> selectMemberGroup(Long id) throws Exception;
    
    
    @Transactional(rollbackFor = Exception.class)
    public void createGroup(String groupName, List<Long> lstMemberId) throws Exception;
    
    @Transactional(rollbackFor = Exception.class)
    public void leaveGroup(Long groupId, Long memberId)throws Exception;
    
    public MembersGroup getMembersGroupById(Long groupId, Long memberId) throws Exception;
    
    public Groups getGroupById(Long groupId) throws Exception;
    
    public List<MembersGroup> getListMembersGroup(Long groupId)throws Exception;

}
