package com.cos.photogramstart.web.dto.auth;


import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data //getter setter 만들어주는 어노테이션
public class SignupDto {
	//@Max(20) //20자 넘어가면 안됨
	@Size(min=1, max=20)
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	@NotBlank
	private String email;
	@NotBlank
	private String name;

	public User toEntity() { //이 함수 진짜 간단하다...
		return User.builder()
				.username(username)
				.password(password)
				.email(email)
				.name(name)
				.build();
	}
}
