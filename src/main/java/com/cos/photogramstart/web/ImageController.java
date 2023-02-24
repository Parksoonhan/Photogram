package com.cos.photogramstart.web;

import java.util.List;

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
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.ImageService;
import com.cos.photogramstart.web.dto.CMRespDto;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ImageController {

	private final ImageService imageService;
	
	@GetMapping({"/", "/image/story"})
	public String story() {
		return "image/story";
	}
	
	@GetMapping("/image/popular")
	public String popular(Model model) {
		
		//api는 데이터를 리턴하는 서버!!
		//이건 ajax나 다른 것들 호출 안하니까 그냥 컨트롤러에서 구현해줘도 됨.
		List<Image> images = imageService.인기사진();
		
		model.addAttribute("images", images);
		
		return "image/popular";
	}
	
	@GetMapping("/image/upload")
	public String upload() {
		return "image/upload";
	}
	
	@PostMapping("/image")
	public String imageUpload(ImageUploadDto imageUploadDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		if(imageUploadDto.getFile().isEmpty()) {
			throw new CustomValidationException("이미지가 첨부되지 않았습니다.", null);
		}else {
			//서비스 호출
			imageService.사진업로드(imageUploadDto, principalDetails);
			
			return "redirect:/user/"+ principalDetails.getUser().getId();
		}
	}
	
	@DeleteMapping("/image/delete/{imageId}")
	public ResponseEntity<?> imageDelete(@PathVariable int imageId){
		imageService.이미지삭제(imageId);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "이미지삭제 성공",  null), HttpStatus.OK);
	}
	
}
