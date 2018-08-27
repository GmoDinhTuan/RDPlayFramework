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
    public List<Groups> findAllGroup(Long id) throws Exception {
        List<Groups> listGroups = chatRoomReponsitory.findAllGroup(id);
        return listGroups;
    }

    /* (non-Javadoc)
     * @see services.ChatRoomService#findAllUserGroup(java.lang.Long)
     */
    @Override
    public List<Groups> findAllUserGroup(Long id) throws Exception {
        List<Groups> listGroups = chatRoomReponsitory.findAllUserGroup(id);
        return listGroups;
    }

    /* (non-Javadoc)
     * @see services.ChatRoomService#selectMemberGroup(java.lang.String)
     */
    @Override
    public List<Member> selectMemberGroup(Long id) throws Exception {
        List<Member> listMemberGroups = chatRoomReponsitory.selectMemberGroup(id);
        return listMemberGroups;
    }

    /* (non-Javadoc)
     * @see services.ChatRoomService#createGroup(java.lang.String, java.util.List)
     */
    @Override
    public void createGroup(String groupName, List<Long> lstMemberId)throws Exception {
        chatRoomReponsitory.createGroup(groupName, lstMemberId);
    }

    /* (non-Javadoc)
     * @see services.ChatRoomService#leaveGroup(java.lang.Long, java.lang.Long)
     */
    @Override
    public void leaveGroup(Long groupId, Long memberId)throws Exception{
        chatRoomReponsitory.leaveGroup(groupId, memberId);
    }

    /* (non-Javadoc)
     * @see services.ChatRoomService#getMembersGroupById(java.lang.Long, java.lang.Long)
     */
    @Override
    public MembersGroup getMembersGroupById(Long groupId, Long memberId) throws Exception{
        return chatRoomReponsitory.getMembersGroupById(groupId, memberId);
    }

    /* (non-Javadoc)
     * @see services.ChatRoomService#getGroupById(java.lang.Long)
     */
    @Override
    public Groups getGroupById(Long groupId) throws Exception{
        return chatRoomReponsitory.getGroupById(groupId);
    }

    /* (non-Javadoc)
     * @see services.ChatRoomService#getListMembersGroup(java.lang.Long)
     */
    @Override
    public List<MembersGroup> getListMembersGroup(Long groupId)throws Exception{
        return chatRoomReponsitory.getListMembersGroup(groupId);
    }
}
