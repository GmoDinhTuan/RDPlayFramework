package repositories.impl;

import java.util.List;

import javax.inject.Singleton;

import common.CommonConsts;
import entities.Groups;
import entities.Member;
import io.ebean.Ebean;
import io.ebean.RawSql;
import io.ebean.RawSqlBuilder;
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
        Member user = Member.find.query().where().like("username", username).findOne();
        if (user.getPassword().equals(password)) {
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
    public List<Member> findUser(String keyWord) throws Exception {
        List<Member> list;
        if (!keyWord.equals(CommonConsts.EMPTY)) {
            list = Member.find.query().where().ilike("name", "%" + keyWord + "%").orderBy("id desc")
                .setMaxRows(100).findPagedList().getList();
        } else {
            String sql = " Select mem.username from member mem where mem.status='1'";

            RawSql rawSql = RawSqlBuilder.parse(sql).create();

            list = Ebean.find(Member.class).setRawSql(rawSql).findList();
        }
        return list;
    }

    /* (non-Javadoc)
     * @see repositories.ChatRoomReponsitory#findAllGroup()
     */
    @Override
    public List<Groups> findAllGroup() throws Exception {
        String sql = " Select grp.groupsname from groups grp where grp.status='1'";

        RawSql rawSql = RawSqlBuilder.parse(sql).create();

        List<Groups> listGroup = Ebean.find(Groups.class).setRawSql(rawSql).findList();
        return listGroup;
    }
}
