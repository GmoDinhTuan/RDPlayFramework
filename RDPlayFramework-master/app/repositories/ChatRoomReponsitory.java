package repositories;

import java.util.List;

import com.google.inject.ImplementedBy;

import entities.Member;
import repositories.impl.ChatRoomReponsitoryImpl;

@ImplementedBy(ChatRoomReponsitoryImpl.class)
public interface ChatRoomReponsitory {

	Member findByName(String username, String password) throws Exception;

	List<Member> findUser(String keyWord) throws Exception;

}
