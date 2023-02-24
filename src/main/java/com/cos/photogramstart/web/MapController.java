package com.cos.photogramstart.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.map.Map;
import com.cos.photogramstart.service.ImageService;
import com.cos.photogramstart.service.MapService;
import com.cos.photogramstart.web.dto.CMRespDto;
import com.cos.photogramstart.web.dto.map.MapUploadDto;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MapController {

	private final MapService mapService;

	@GetMapping("/user/getmate")
	public String getmate() {
		return "user/getmate";
	}
	
	//이미지 등록
	@PostMapping("/user/getmate")
	public String putmate(MapUploadDto mapUploadDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		mapService.일정업로드(mapUploadDto, principalDetails);
		
		return "redirect:/user/"+ principalDetails.getUser().getId();
	}
	
	@GetMapping("/user/maplist/{userId}")
	public String mapList(@PathVariable int userId, Model model, @PageableDefault(size=3) Pageable pageable){ //페이징
		model.addAttribute("userId", userId);
		return "user/maplist"; 
	}
	
	@DeleteMapping("/map/delete/{mapId}")
	public ResponseEntity<?> mapDelete(@PathVariable int mapId){
		mapService.일정삭제(mapId);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "일정 삭제 성공",  null), HttpStatus.OK);
	}
}
