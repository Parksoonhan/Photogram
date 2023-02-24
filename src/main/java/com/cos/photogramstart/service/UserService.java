package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.domain.user.subscribe.SubScribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final SubScribeRepository subScribeRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//yml에 적은 값도 내가 들고올 수 있다!
	@Value("${file.path}")//application.yml 참고
	private String uploadFolder;
	
	@Transactional
	public User 회원프로필사진변경(int principalId,MultipartFile profileImageFile) {
		UUID uuid = UUID.randomUUID();
		String imageFileName = uuid + "_" +profileImageFile.getOriginalFilename(); //실제 파일 이름이 들어감
		
		Path imageFiPath = Paths.get(uploadFolder + imageFileName);
		
		try {
			Files.write(imageFiPath, profileImageFile.getBytes());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		User userEntity = userRepository.findById(principalId).orElseThrow(()->{
			throw new CustomApiException("유저를 찾을 수 없습니다.");
		});

		userEntity.setProfileImageUrl(imageFileName);
		
		return userEntity;
		
	} //더티체킹으로 업데이트 됨.
	
	
	@Transactional(readOnly = true) 
	public UserProfileDto 회원프로필(int pageUserId, int principalId) {
		UserProfileDto dto = new UserProfileDto();
		
		//SELECT * FROM image WHERE userId = :userId
		User userEntity = userRepository.findById(pageUserId).orElseThrow(()->{
			throw new CustomException("해당 프로필 페이지는 없는 페이지 입니다.");
		});
		
		dto.setUser(userEntity);
		dto.setPageOwnerState(pageUserId == principalId); //1은 페이지 주인 , -1은 페이지 주인 아님
		dto.setImageCount(userEntity.getImages().size());
		dto.setMapCount(userEntity.getMaps().size());
		
		int subscribeState = subScribeRepository.mSubscribeState(principalId, pageUserId);
		int subscribeCount = subScribeRepository.mSubscribeCount(pageUserId);
		
		dto.setSubscribeState(subscribeState ==1);
		dto.setSubscribeCount(subscribeCount);  
		
		//좋아요 카운트 추가하기
		userEntity.getImages().forEach((image)->{
			image.setLikeCount(image.getLikes().size());
		});
		
		return dto;
	}
	
	@Transactional //회원수정이 일어나니까
	public User 회원수정(int id, User user) {
		//1. 영속화
		 //1. 무조건 찾았다 걱정마-> get  2. 못 찾았어 익셉션 발동시킬게 -> orElseThrow()
		//User userEntity = userRepository.findById(id).get();
//		User userEntity = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
//			@Override
//			public IllegalArgumentException get() {
//				// TODO Auto-generated method stub
//				return new IllegalArgumentException("찾을 수 없는 id 입니다.");
//			}
//		});
		
		//람다식
		User userEntity = userRepository.findById(id).orElseThrow(() -> { return new CustomValidationApiException("찾을 수 없는 id 입니다.");});
		
		//2. 영속화된 오브젝트를 수정 - 더티체킹(업데이트 완료)
		userEntity.setName(user.getName());
		
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		
		userEntity.setPassword(encPassword);
		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());
		return userEntity;
	}
	
}
