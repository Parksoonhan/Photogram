package com.cos.photogramstart.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.domain.map.Map;
import com.cos.photogramstart.domain.map.MapRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.map.MapUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MapService {
	
	private final MapRepository mapRepository;

	
	//이미지삭제
	@Transactional
	public void 일정삭제(int mapId) {
		try {
			mapRepository.deleteById(mapId);
		}catch (Exception e) {
			throw new CustomApiException(e.getMessage());
		}
		
	}
	
	@Transactional(readOnly = true) //영속성 컨텍스트 변경 감지를 해서, 더티체킹, db flush(반영) X
	public Page<Map> 일정리스트(int userId, int principalId,Pageable pageable){
		Page<Map> lists = mapRepository.mapLists(userId, pageable);
		
		// 2번 사용자 로그인, 2번이 구독하고 있는 사람들 정보 뽑아내면서 그 이미지 좋아요 정보 쫙 뽑아와 . 그 좋아요 중에 내가 좋아요 누른 것도 있니?
		//Images에 좋아요 상태 담기
		lists.forEach((list)->{
			
			list.setLikeCount(list.getLikes().size());
			
			list.getLikes().forEach((like)->{
				if(like.getUser().getId() == principalId) { //해당 이미지에 좋아요 한 사람들을 찾아서 현재 로그인 한 사람이 좋아요 한 것인지 비교
					list.setLikeState(true);
				}
			});
		});
		
		return lists;
	}
	
	
	@Transactional
	public void 일정업로드(MapUploadDto mapUploadDto, PrincipalDetails principalDetails) {
		try {
			Map map = mapUploadDto.toEntity(principalDetails.getUser());
			mapRepository.save(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
