package com.cos.photogramstart.handler.ex;

import java.util.Map;

/*CustomApiException
-> 데이터를 리턴하는 컨트롤러
CustomException
-> html 파일 리턴하는 컨트롤러
CustomValidationException
-> 어떤 데이터를 받을 때 리턴하는 컨트롤러*/

public class CustomApiException extends RuntimeException{

	private static final long serialVersionUID=1L;
	
	public CustomApiException(String message) {
		super(message);
	}
}
