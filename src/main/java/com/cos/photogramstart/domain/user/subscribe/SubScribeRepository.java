package com.cos.photogramstart.domain.user.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubScribeRepository extends JpaRepository<Subscribe, Integer>{

	@Modifying //INSERT, DELETE, UPDATE를 네이티브 쿼리로 작성하려면 해당 어노테이션이 필요
	@Query(value = "INSERT INTO subscribe(fromUserId, toUserId, createDate) VALUES(:fromUserId, :toUserId, now())", nativeQuery = true)
	void mSubscribe(int fromUserId, int toUserId); // 반환 데이터형이 int인 이유 -> 변경된 행의 개수가 리턴됨
	
	@Modifying
	@Query(value="DELETE FROM subscribe WHERE fromUserId = :fromUserId and toUserId = :toUserId", nativeQuery = true)
	void mUnSubscribe(int fromUserId, int toUserId);
	
	@Query(value="SELECT COUNT(*) FROM subscribe where fromUserId = :principalId AND toUserId= :pageUserId", nativeQuery = true)
	int mSubscribeState(int principalId, int pageUserId);
	
	@Query(value="SELECT COUNT(*) FROM subscribe WHERE fromUserId= :pageUserId", nativeQuery = true)
	int mSubscribeCount( int pageUserId);
}
