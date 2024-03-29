package com.cos.photogramstart.domain.likes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface LikesRepository extends JpaRepository<Likes, Integer>{

	@Modifying
	@Query(value="INSERT INTO likes(imageId, userId, createDate) VALUES(:imageId, :principalId, now())" , nativeQuery = true)
	int mLikes(int imageId, int principalId);
	
	@Modifying
	@Query(value="DELETE FROM likes WHERE imageId = :imageId and userId =:principalId" , nativeQuery = true)
	int mUnLikes(int imageId, int principalId);
	
	@Modifying
	@Query(value="INSERT INTO likes(mapId, userId, createDate) VALUES(:mapId, :principalId, now())" , nativeQuery = true)
	int mapLikes(int mapId, int principalId);
	
	@Modifying
	@Query(value="DELETE FROM likes WHERE mapId = :mapId and userId =:principalId" , nativeQuery = true)
	int mapUnLikes(int mapId, int principalId);
}
