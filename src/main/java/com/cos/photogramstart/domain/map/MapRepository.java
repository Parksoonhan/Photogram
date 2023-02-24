package com.cos.photogramstart.domain.map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface MapRepository extends JpaRepository<Map, Integer>{
	@Query(value="SELECT * FROM map WHERE userid = :userId ORDER BY id desc", nativeQuery = true)
	Page<Map> mapLists(int userId, Pageable pageable); //페이징 자동으로 됨
}
