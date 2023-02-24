package com.cos.photogramstart.web.api;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.service.CommentService;
import com.cos.photogramstart.web.dto.CMRespDto;
import com.cos.photogramstart.web.dto.comment.CommentDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class CommentApiController {
	private final CommentService commentService;
	
	@PostMapping("/api/comment/image")
	public ResponseEntity<?> imageCommentSave(@Valid @RequestBody CommentDto commentDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails){ //json 형태로 들어온 값 처리 하기 위해서 어노테이션 붙여줌 
		Comment comment = commentService.이미지댓글쓰기(commentDto.getContent(), commentDto.getImageId(), principalDetails.getUser().getId());
		return new ResponseEntity<>(new CMRespDto<>(1, "이미지에댓글달기 성공",  comment), HttpStatus.CREATED);
	}
	
	@PostMapping("/api/comment/map")
	public ResponseEntity<?> mapCommentSave(@Valid @RequestBody CommentDto commentDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails){ //json 형태로 들어온 값 처리 하기 위해서 어노테이션 붙여줌 
		Comment comment = commentService.일정댓글쓰기(commentDto.getContent(), commentDto.getMapId(), principalDetails.getUser().getId());
		return new ResponseEntity<>(new CMRespDto<>(1, "일정에댓글달기 성공",  comment), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/api/comment/{id}")
	public ResponseEntity<?> commentDelete(@PathVariable int id){
		commentService.댓글삭제(id);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "댓글삭제 성공",  null), HttpStatus.OK);
	}
}
