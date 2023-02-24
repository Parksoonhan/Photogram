package com.cos.photogramstart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.cos.photogramstart.config.oauth.OAuth2DetailsService;

import lombok.RequiredArgsConstructor;

//@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final OAuth2DetailsService oAuth2DetailsService;
	
	@Bean
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	//현재 방식에서 네이버로그인 구현하려면 필터체인 형식으로 바꿔줘야한대요
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		// TODO Auto-generated method stub
		//signup에서 post로 던질 때 임시난수값(csrf)를 같이 던지는데 그 때 페이지 이동까지 막음 .. 그래서 막아줘야
		http.csrf().disable();
		//super.configure(http); 주석 
		//기존 시큐리티가 가지고 있는 기능이 다 비활성화됨.
		http.authorizeRequests() //인증이 필요한 경로의 페이지에 접근 시 로그인 페이지에 바로 가
		.antMatchers("/", "/user/**", "/image/**", "subscribe/**", "/comment/**", "/api/**").authenticated()
		.anyRequest().permitAll()
		.and()
		.formLogin()
		.loginPage("/auth/signin") //GET 
		.loginProcessingUrl("/auth/signin") //POST 방식으로 요청이 되면 스프링 시큐리티가 로그인 프로세스진행
		.defaultSuccessUrl("/")
		.and()
		.oauth2Login() //form로그인도 하겠지만 oauth2로그인도 할 거라는 뜻
		.userInfoEndpoint() // oauth2 로그인을 하면 최종응답을 회원 정보로 바로 받을 수 있다.
		.userService(oAuth2DetailsService);
		return http.build();
	}
}
