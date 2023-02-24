package com.cos.photogramstart.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User{

	private static final long serialVersionUID = 1L;

	private User user;
	private Map<String, Object> attributes;
	
	// 일반 로그인
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	// OAuth2로그인
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
	}
	
	//권한 : 한개가 아닐 수 있음(3개이상의 권한도 있을 수 있음)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 권한을 가져오는 함수
		//콜렉션도 걀국 어레이 리스트 함수니까 반환형 바꿔
		Collection<GrantedAuthority> collector = new ArrayList<>();
//		collector.add(new GrantedAuthority() {
//
//			@Override
//			public String getAuthority() {
//				return user.getRole();
//			}
//		});
		collector.add(() -> {  return user.getRole(); }); //위의 코드를 람다식으로 변환 
		
		return collector;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return attributes; //{id:fsdfwtwewe name: 최주호}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return (String) attributes.get("name");
	}

}
