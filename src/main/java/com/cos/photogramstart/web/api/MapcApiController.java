package com.cos.photogramstart.web.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.map.Map;
import com.cos.photogramstart.service.ImageService;
import com.cos.photogramstart.service.LikesService;
import com.cos.photogramstart.service.MapService;
import com.cos.photogramstart.web.dto.CMRespDto;
import com.cos.photogramstart.web.dto.map.MapUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MapcApiController {
	
	private final MapService mapService;
	private final LikesService likesService;
	
	@GetMapping("/api/maplist/{userId}")
	public ResponseEntity<?> mapList(@PathVariable int userId, @AuthenticationPrincipal PrincipalDetails principalDetails
			,@PageableDefault(size=3) Pageable pageable){ //페이징
			Page<Map> lists = mapService.일정리스트(userId, principalDetails.getUser().getId(), pageable);
			return new ResponseEntity<>(new CMRespDto<>(1, "성공",  lists), HttpStatus.OK);
	}
	@PostMapping("/api/map/{mapId}/likes")
	public ResponseEntity<?> likes(@PathVariable int mapId, @AuthenticationPrincipal PrincipalDetails principalDetails){
		likesService.일정좋아요(mapId, principalDetails.getUser().getId());
		return new ResponseEntity<>(new CMRespDto<>(1, "일정좋아요성공",  null), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/api/map/{mapId}/likes")
	public ResponseEntity<?> mapId(@PathVariable int mapId, @AuthenticationPrincipal PrincipalDetails principalDetails){
		likesService.일정좋아요취소(mapId, principalDetails.getUser().getId());
		return new ResponseEntity<>(new CMRespDto<>(1, "일정좋아요취소성공",  null), HttpStatus.OK);
	}
	
}
