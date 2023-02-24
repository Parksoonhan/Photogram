package com.cos.photogramstart.web;

import java.util.HashMap;
import java.util.Map;

import javax.management.RuntimeErrorException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor //final걸려있는 모든 생성자 찾아줄 때 편함 / final 필드를 DI 할때 사용
@Controller //1.ioC 2.파일을 리턴하는 컨트롤러
public class AuthController {

	
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	//의존성 주입
	//ioc에서 authService를 찾게
	private final AuthService authService;
	
//이건 불편한 방법이다.. 옛날 방법!
//	public AuthController(AuthService authService) {
//		this.authService = authService;
//	}
	
	@GetMapping("/auth/signin")
	public String signinForm() {
		return "auth/signin";
	}
	
	@GetMapping("/auth/signup")
	public String signupForm() {
		return "auth/signup";
	}
	
	//회원가입버튼 -> /auth/signup -> /auth/signin
	@PostMapping("/auth/signup")
	//@ResponseBody //이게 있으면 데이터를 응답하는데 어떤 때는 파일 어떤 땐 문자열 호출하는 그게 .. 별로야!! 바꿀게용
	//BindingResult은 maven(pom.xml)에 dependency를 추가 해줘야함
	public String signup(@Valid SignupDto signupDto, BindingResult bindingResult) { //키 밸류 형식으로 받아옴
		//User <- SignupDto
		User user = signupDto.toEntity();
		User userEntitiy = authService.회원가입(user);
		return "auth/signin"; //회원가입이 성공하면 갈 페이지
	}
		

	
}
