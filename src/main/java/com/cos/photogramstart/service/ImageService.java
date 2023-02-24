package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	//이미지삭제
	@Transactional
	public void 이미지삭제(int imageId) {
		try {
			imageRepository.deleteById(imageId);
		}catch (Exception e) {
			throw new CustomApiException(e.getMessage());
		}
		
	}
	
	@Transactional(readOnly = true)
	public List<Image> 인기사진(){
		return imageRepository.mPopular();
	}
	
	
	@Transactional(readOnly = true) //영속성 컨텍스트 변경 감지를 해서, 더티체킹, db flush(반영) X
	public Page<Image> 이미지스토리(int principalId, Pageable pageable){
		Page<Image> images = imageRepository.mStroy(principalId, pageable);
		
		// 2번 사용자 로그인, 2번이 구독하고 있는 사람들 정보 뽑아내면서 그 이미지 좋아요 정보 쫙 뽑아와 . 그 좋아요 중에 내가 좋아요 누른 것도 있니?
		//Images에 좋아요 상태 담기
		images.forEach((image)->{
			
			image.setLikeCount(image.getLikes().size());
			
			image.getLikes().forEach((like)->{
				if(like.getUser().getId() == principalId) { //해당 이미지에 좋아요 한 사람들을 찾아서 현재 로그인 한 사람이 좋아요 한 것인지 비교
					image.setLikeState(true);
				}
			});
		});
		
		return images;
	}
	
	//yml에 적은 값도 내가 들고올 수 있다!
	@Value("${file.path}")//application.yml 참고
	private String uploadFolder;
	
	
	@Transactional //일의 최소단위(둘 중 하나라도 실패하면 롤백 둘 다 성공하면 커밋) - DB에 변형이 일어나는 과정은 무조건 트랙잭션 걸기
	public void 사진업로드(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		UUID uuid = UUID.randomUUID();
		String imageFileName = uuid + "_" +imageUploadDto.getFile().getOriginalFilename(); //실제 파일 이름이 들어감
		System.out.println("이미지 파일 이름: " + imageFileName );
		
		//D:/workspace/springbootwork/upload/
		//application.yml 참고
		Path imageFiPath = Paths.get(uploadFolder + imageFileName);
		
		//통신이 일어나거나 I/O(하드디스크에 기록 할 때)가 일어날 때 -> 예외가 발생할 수 있다.
		try {
			Files.write(imageFiPath, imageUploadDto.getFile().getBytes());
		}catch (Exception e) {
			e.printStackTrace();
		}

		//image 테이블에 저장
		Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser()); 
		imageRepository.save(image);
	}
	
}
