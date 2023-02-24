package com.cos.photogramstart.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor //생성자 자동생성 ..(뭐 this.이딴 거 안해도 됨)
@Service // 1.ioC 2.트랜잭션관리
public class AuthService {
	
		private final UserRepository userRepository;
		private final BCryptPasswordEncoder bCryptPasswordEncoder;

		@Transactional // Write(insert, update, Delete)
		public User 회원가입(User user) { //외부에서 통신받은 값
			//회원가입 진행
			String rawPassword = user.getPassword();
			String encPassword = bCryptPasswordEncoder.encode(rawPassword);
			user.setPassword(encPassword);
			user.setRole("ROLE_USER"); //관리자 - ROLE_ADMIN
			User userEntitiy = userRepository.save(user);  //데이터베이스에 저장
			return userEntitiy; //이건 데이터베이스에서 꺼내온 값이다.
		}

}
