package com.cos.photogramstart.handler.ex;

import java.util.Map;

public class CustomValidationException extends RuntimeException{

	private static final long serialVersionUID=1L;
	
	private Map<String, String> errorMap;
	
	
	public CustomValidationException(String message, Map<String, String> errorMap) {
		//message는 getter를 안 만들어줘도 된다.
		//왜냐하면 Throwable클래스(부모)에 이미 getMessage가 선언되어 있기 때문이다.
		super(message);
		this.errorMap = errorMap;
	}
		
	public Map<String, String> getErrorMap(){
		return errorMap;
	}
}
