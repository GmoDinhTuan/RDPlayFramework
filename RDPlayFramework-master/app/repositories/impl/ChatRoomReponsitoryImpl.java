package repositories.impl;

import java.util.List;

import javax.inject.Singleton;

import common.CommonConsts;
import entities.Groups;
import entities.Member;
import entities.MembersGroup;
import io.ebean.Ebean;
import io.ebean.RawSql;
import io.ebean.RawSqlBuilder;
import io.ebean.annotation.Transactional;
import repositories.ChatRoomReponsitory;

// TODO: Auto-generated Javadoc
/**
 * The Class ChatRoomReponsitoryImpl.
 */
@Singleton
public class ChatRoomReponsitoryImpl implements ChatRoomReponsitory {

    /*
     * (non-Javadoc)
     *
     * @see repositories.ChatRoomReponsitory#findByName(java.lang.String, java.lang.String)
     */
    @Override
    public Member findByName(String username, String password) throws Exception {
        Member user = Member.find.query().where().like(CommonConsts.USERNAME, username).findOne();
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see repositories.ChatRoomReponsitory#findUser(java.lang.String, java.lang.String)
     */
    @Override
    public List<Member> findUser() throws Exception {
        String sql = " Select mem.id, mem.username, mem.status, mem.description from member mem where mem.status= " + CommonConsts.VALUE_STATUS_ONE;

        RawSql rawSql = RawSqlBuilder.parse(sql).create();

        List<Member> list = Ebean.find(Member.class).setRawSql(rawSql).findList();
        return list;
    }

    /*
     * (non-Javadoc)
     *
     * @see repositories.ChatRoomReponsitory#findAllGroup()
     */
    @Override
    public List<Groups> findAllGroup(Long id) throws Exception {
    	StringBuilder sql = new StringBuilder();
        sql.append(" Select grp.groupsname, grp.id, grp.status, grp.description from groups grp join membersgroup mgr on mgr.id = grp.id where grp.status= " + CommonConsts.VALUE_STATUS_ONE + "and mgr.memberid = :id");
        RawSql rawSql = RawSqlBuilder.parse(sql.toString()).create();

        List<Groups> listGroup = Ebean.find(Groups.class).setRawSql(rawSql).setParameter("id", id).findList();
        return listGroup;
    }

    /* (non-Javadoc)
     * @see repositories.ChatRoomReponsitory#findAllUserGroup(java.lang.Long)
     */
    @Override
    public List<Groups> findAllUserGroup(Long id) throws Exception {
    	StringBuilder sql = new StringBuilder();
        sql.append("Select grp.groupsname, grp.id, grp.status, grp.description from groups grp, membersgroup mgr where grp.id = mgr.groupid and mgr.memberid = :id");
        RawSql rawSql = RawSqlBuilder.parse(sql.toString()).create();

        List<Groups> listGroup = Ebean.find(Groups.class).setRawSql(rawSql).setParameter("id", id).findList();
        return listGroup;
    }

    /* (non-Javadoc)
     * @see repositories.ChatRoomReponsitory#selectMemberGroup(java.lang.Long)
     */
    @Override
    public List<Member> selectMemberGroup(Long id) throws Exception {
        String sql = " Select mem.username from member mem join membersgroup memgroup on mem.id = memgroup.memberid where memgroup.status="
    + CommonConsts.VALUE_STATUS_ONE +" and memgroup.groupid = :groupid";
        RawSql rawSql = RawSqlBuilder.parse(sql).create();

        List<Member> listGroup = Ebean.find(Member.class).setRawSql(rawSql)
            .setParameter(CommonConsts.GROUP_ID, id).findList();
        return listGroup;
    }

    /* (non-Javadoc)
     * @see repositories.ChatRoomReponsitory#createGroup(java.lang.String, java.util.List)
     */
    @Override
	@Transactional(rollbackFor = Exception.class)
    public void createGroup(String groupName, List<Long> lstMemberId) throws Exception{
    	Groups group = new Groups();
    	group.setGroupsname(groupName);
    	group.setStatus("1");
    	Ebean.save(group);
    	int size = lstMemberId.size();
    	for(int i=0;i<size;i++) {
//    		Member member = lstMemberId.get(i);

    		MembersGroup membersGroup = new MembersGroup();
        	membersGroup.setGroupId(group.getId());
        	membersGroup.setMemberId(lstMemberId.get(i));
        	membersGroup.setStatus("1");
        	Ebean.save(membersGroup);
    	}

    }

    /* (non-Javadoc)
     * @see repositories.ChatRoomReponsitory#leaveGroup(java.lang.Long, java.lang.Long)
     */
    @Override
	@Transactional(rollbackFor = Exception.class)
    public void leaveGroup(Long groupId, Long memberId)throws Exception{
    	List<MembersGroup> lstMembersGroup = getListMembersGroup(groupId);
    	int size = lstMembersGroup.size();
    	if(size == 1) {
    		//remove member and delete group
    		MembersGroup membersGroup = lstMembersGroup.get(0);
    		membersGroup.setStatus("0");
    		Ebean.save(membersGroup);

    		Groups group = getGroupById(groupId);
    		if(group != null) {
    			group.setStatus("0");
    			Ebean.save(group);
    		}
    	}else if(size > 1){
    		//remove member
    		MembersGroup membersGroup = getMembersGroupById(groupId, memberId);
    		if(membersGroup != null) {
    			membersGroup.setStatus("0");
        		Ebean.save(membersGroup);
    		}
    	}
    }

    /* (non-Javadoc)
     * @see repositories.ChatRoomReponsitory#getMembersGroupById(java.lang.Long, java.lang.Long)
     */
    @Override
    public MembersGroup getMembersGroupById(Long groupId, Long memberId) throws Exception{
    	StringBuilder sql = new StringBuilder();
        sql.append("Select mgr.id, mgr.groupid, mgr.memberid, mgr.status from MembersGroup mgr where  mgr.groupid = :groupId and mgr.memberId = :memberId and status = :status");
        RawSql rawSql = RawSqlBuilder.parse(sql.toString()).create();

        MembersGroup membersGroup = Ebean.find(MembersGroup.class).setRawSql(rawSql)
        		.setParameter("groupId", groupId)
        		.setParameter("memberId", memberId)
        		.setParameter("status", CommonConsts.VALUE_STATUS_ONE)
        		.findOne();
        return membersGroup;
    }

    /* (non-Javadoc)
     * @see repositories.ChatRoomReponsitory#getGroupById(java.lang.Long)
     */
    @Override
    public Groups getGroupById(Long groupId) throws Exception{
    	StringBuilder sql = new StringBuilder();
        sql.append("select g.id, g.groupname, g.status, g.description from groups g where g.id = :groupId and status = :status ");
        RawSql rawSql = RawSqlBuilder.parse(sql.toString()).create();

        Groups Group = Ebean.find(Groups.class).setRawSql(rawSql)
        		.setParameter("groupId", groupId)
        		.setParameter("status", CommonConsts.VALUE_STATUS_ONE)
        		.findOne();
        return Group;
    }

    /* (non-Javadoc)
     * @see repositories.ChatRoomReponsitory#getListMembersGroup(java.lang.Long)
     */
    @Override
    public List<MembersGroup> getListMembersGroup(Long groupId)throws Exception{
    	StringBuilder sql = new StringBuilder();
        sql.append("Select mgr.id, mgr.groupid, mgr.memberid, mgr.status from MembersGroup mgr where  mgr.groupid = :groupId and status = :status");
        RawSql rawSql = RawSqlBuilder.parse(sql.toString()).create();

        List<MembersGroup> lstMembersGroup = Ebean.find(MembersGroup.class).setRawSql(rawSql)
        		.setParameter("groupId", groupId)
        		.setParameter("status", CommonConsts.VALUE_STATUS_ONE)
        		.findList();
        return lstMembersGroup;
    }
}
