package repositories;

import com.google.inject.ImplementedBy;

import entities.Member;
import repositories.impl.ChatRoomReponsitoryImpl;

@ImplementedBy(ChatRoomReponsitoryImpl.class)
public interface ChatRoomReponsitory {
	
	Member findByName(String username, String password) throws Exception;

}
