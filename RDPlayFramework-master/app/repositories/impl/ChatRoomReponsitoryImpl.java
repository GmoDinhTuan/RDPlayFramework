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
    public List<Groups> findAllGroup() throws Exception {
        String sql = " Select grp.groupsname, grp.id, grp.status, grp.description from groups grp where grp.status= " + CommonConsts.VALUE_STATUS_ONE;

        RawSql rawSql = RawSqlBuilder.parse(sql).create();

        List<Groups> listGroup = Ebean.find(Groups.class).setRawSql(rawSql).findList();
        return listGroup;
    }

    @Override
    public List<MembersGroup> selectMemberGroup(Long id) throws Exception {
        String sql = " Select mem.username from member mem join membersgroup memgroup on mem.id = memgroup.memberid where memgroup.status=" 
    + CommonConsts.VALUE_STATUS_ONE +" and memgroup.groupid = :groupid";
        RawSql rawSql = RawSqlBuilder.parse(sql).create();

        List<MembersGroup> listGroup = Ebean.find(MembersGroup.class).setRawSql(rawSql)
            .setParameter(CommonConsts.GROUP_ID, id).findList();
        return listGroup;
    }
    
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
}
