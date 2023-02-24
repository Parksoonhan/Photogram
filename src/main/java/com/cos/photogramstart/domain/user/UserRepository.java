package com.cos.photogramstart.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

//어노테이션이 없어도 JpaRepository 선언하면 자동으로 ioc에 등록이 된다.
//JpaRepository는 user테이블의 모든 정보를 끌고 오고 insert하고 id값으로 찾을 수 있고,, 등등
public interface UserRepository extends JpaRepository<User, Integer>{
	//JPA query method
	User findByUsername(String username);
}
