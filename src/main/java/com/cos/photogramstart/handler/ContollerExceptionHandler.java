package com.cos.photogramstart.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.CMRespDto;

//이 파일을 거친 애들은 다 CustomValidationException 클래스에서 에러가 발생한 애들이고 얘네는 CMRespDto를 거쳐서 값을 받환하게 됨
//그냥 오류 잡고 싶으면 이 클래스 하나 만들어서 사용하면 다 거쳐가서 잡아냄
@RestController //데이터 응답
@ControllerAdvice //이걸 쓰면 모든 익셉션을 다 낚아챔
public class ContollerExceptionHandler {

	//CustomValidationException 여기 클래스를 거칠 때 발생하는 모든 오류들을 가로챔
	 //런타임 익셉션이 발생하는 모든 오류 가로챔
//	public CMRespDto<?> validationException(CustomValidationException e) {
//		return new CMRespDto<Map<String,String>>(-1, e.getMessage(), e.getErrorMap()); //이게 있기 때문에 CMRespDto<?>어떤 타입을 적어도 됨
	@ExceptionHandler(CustomValidationException.class)
	public String validationException(CustomValidationException e) {
		//CMRespDto, Script비교
		//1. 클라이언트에게 응답할 때는 Script 좋음
		//2. Ajax통신 - CMRespDto
		//3. Android통신 - CMRespDto
		// 결국 클라이언트가 응답받을 땐 스크립트 개발자가 응답받을 땐 디티오가 편함ㄴ
		if(e.getErrorMap() == null) {
			return Script.back(e.getMessage());
		}else {
			return Script.back(e.getErrorMap().toString()); 
		}
	}
	
	@ExceptionHandler(CustomException.class)
	public String exception(CustomException e) {
			return Script.back(e.getMessage());
	}
	
	@ExceptionHandler(CustomValidationApiException.class)
	public  ResponseEntity<?> validationApiException(CustomValidationApiException e) { //<?>자동으로 타입 반환
		//return new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()); 
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST); //상태코드 자동 반환
	}
	
	@ExceptionHandler(CustomApiException.class)
	public  ResponseEntity<?> apiException(CustomApiException e) { //<?>자동으로 타입 반환
		//return new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()); 
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST); //상태코드 자동 반환
	}
}
