package com.cos.photogramstart.web.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

//@NotNull = null값 체크
//@NotEmpty = 빈값이거나 null 체크
//@NotBlank = 빈값이거나 null 빈공백까지 체크
//20230224 mapId 추가로 인해 @NotNull 어노테이션은 주석처리 하는 것으로 변경

@Data
public class CommentDto {
	@NotBlank // 빈값이거나 null 빈공백까지 체크
	private String content; 
	//@NotNull //null값 체크
	private Integer imageId;
	//@NotNull //null값 체크
	private Integer mapId;
	
	//toEntity가 필요없다
}
