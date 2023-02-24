package com.cos.photogramstart.web.dto;

import java.util.Map;

import com.cos.photogramstart.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//게터,세터, 생성자
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CMRespDto<T> { //제네릭 사용(문자열 들어오면 문자열, map들어오면 map)
	private int code; //1(성공), -1(실패)
	private String message;
	private T data;
}
