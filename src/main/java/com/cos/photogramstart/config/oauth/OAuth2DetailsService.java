package com.cos.photogramstart.config.oauth;

import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.config.oauth.provider.NaverUserInfo;
import com.cos.photogramstart.config.oauth.provider.OAuth2UserInfo;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService{
	
	private final UserRepository userRepository;
	//private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest); //유저 정보		
		String platform = userRequest.getClientRegistration().getRegistrationId(); //매체 확인(naver, kakao, google)
		//System.out.println("oAuth2User====>"+ oAuth2User);
		
		
		
		System.out.println("oAuth2User.getAttributes()====>"+ oAuth2User.getAttributes());
		System.out.println("oAuth2User.getAttributes().response=>>"+oAuth2User.getAttributes().get("response"));
		//System.out.println("userInfo====>"+ userInfo);
		OAuth2UserInfo oAuth2UserInfo = null;
		
		if(platform.equals("naver")) {
			oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
		}else if(platform.equals("kakao")) {
		}else if(platform.equals("google")) {
			oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
		}else {
			System.out.println("다른 플랫폼 지원 x");
		}
		
		String provider = oAuth2UserInfo.getProvider(); //매체명
		String providerId = oAuth2UserInfo.getProviderId(); //회원 아이디
		
		//OAuth2 로그인 시 username과 password는 필요없지만 형식상 넣어줌
		String username = provider+"_"+ providerId;
		String password = new BCryptPasswordEncoder().encode("provider");
		String role = "ROLE_USER";
		String email = oAuth2UserInfo.getEmail();
		String name = oAuth2UserInfo.getName();
		String gender = oAuth2UserInfo.getGender();
		String phone = oAuth2UserInfo.getMobile().replaceAll("-", "");

		System.out.println(provider+"/"+providerId+"/"+username+"/"+password+"/"+role+"/"+ email+"/"+name+"/"+ gender+"/"+phone);
		
		User userEntity = userRepository.findByUsername(username);
		
		
		if(userEntity == null) { //해당 플랫폼으로 가입된 아이디가 조회되지 않으면 가입처리
			User user = User.builder()
					.username(username)
					.password(password)
					.role(role)
					.email(email)
					.name(name)
					.gender(gender)
					.phone(phone)
					.build();
			
			return new PrincipalDetails(userRepository.save(user), oAuth2User.getAttributes());
		}else{ //네이버로 이미 로그인 되어있는 경우
			return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
		}
	}
	
}
