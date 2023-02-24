package com.cos.photogramstart.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;

@Component //RestController든 Service든 다 컨포넌트를 상속해서 만들어져 있음
@Aspect
public class ValidationAdvice {

	//무한 참조 때문에 User에 toString()부분에 images 지워줌
	
	@Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))") //컨트롤러 안의 모든 메소드가 실행될 때 작동한다는 뜻
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
		System.out.println("===================================================");
		System.out.println("web api 컨트롤러");
	
		Object[] args = proceedingJoinPoint.getArgs();
		for(Object arg : args) {
			if(arg instanceof BindingResult) {
				System.out.println("api 유효성 검사를 하는 함수입니다.");
				BindingResult bindingResult = (BindingResult) arg;
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage()); //getField란 예를 들어 username같은 것들
						System.out.println("에러메세지===>" + error.getDefaultMessage());
					}
					throw new CustomValidationApiException("유효성검사실패", errorMap);
				}
			}
		}
		
		//proceedingJoinPoint => profile 함수의 모든 곳에 접근할 수 있는 변수
		//profile 함수보다 먼저 실행
		
		return proceedingJoinPoint.proceed();
		
	}
	
	@Around("execution(* com.cos.photogramstart.web.*Controller.*(..))") //컨트롤러 안의 모든 메소드가 실행될 때 작동한다는 뜻
	public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
		System.out.println("===================================================");
		System.out.println("web 컨트롤러");
		
		Object[] args = proceedingJoinPoint.getArgs();
		for(Object arg : args) {
			if(arg instanceof BindingResult) {
				System.out.println("유효성 검사를 하는 함수입니다.");
				BindingResult bindingResult = (BindingResult) arg;
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage()); //getField란 예를 들어 username같은 것들
						System.out.println("에러메세지===>" + error.getDefaultMessage());
					}
					throw new CustomValidationException("유효성검사실패", errorMap);
				}
				
			}
		}
		
		return proceedingJoinPoint.proceed();
	} 
}
