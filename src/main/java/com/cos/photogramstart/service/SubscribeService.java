package com.cos.photogramstart.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.user.subscribe.SubScribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {

	private final SubScribeRepository subScribeRepository;
	private final EntityManager em; //레파지토리는 엔티티매니저를 구현해서 만들어져 있는 구현체
	
	@Transactional(readOnly = true)
	public List<SubscribeDto> 구독리스트(int principalId, int pageUserId) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT u.id, u.username, u.profileImageUrl,  ");
		sb.append("if((SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId= u.id),1,0) subscribeState, ");
		sb.append("if((?=u.id),1,0) equalUserState  ");
		sb.append("FROM user u INNER JOIN subscribe s ");
		sb.append("ON u.id = s.toUserId ");
		sb.append("WHERE s.fromUserId = ?"); //세미콜론 첨부하면 안됨
		
		//첫번째 물음표 = principalId (로그인한 아이디)
		//두번째 물음표 = principalId (로그인한 아이디)
		//마지막 물음표 = pageUserId (페이지의 주인 아이디)
		
		//쿼리완성
		Query query = em.createNativeQuery(sb.toString())
			.setParameter(1, principalId)
			.setParameter(2, principalId)
			.setParameter(3, pageUserId);
			
		
		//쿼리실행
		//qlrm - pom.xml에 라이브러리 추가(dto에 db결과를 매핑하기 위해서)
 		JpaResultMapper result = new JpaResultMapper();
		List<SubscribeDto> subscribeDtos = result.list(query, SubscribeDto.class);
		
		
		return subscribeDtos;
	}
	
	//return void로 바꾼 이유
	//db에서 익셉션 터지면 그냥 try/catch로 처리하면 되기 때문에 관련 코드 다 주석처리함
	// ControllerExceptionHandler에서 다 처리
	
	@Transactional //데이터 베이스에 영향이 갈때 걸어줌
	public void 구독하기(int fromUserId, int toUserId) {
		//int result = subScribeRepository.mSubscribe(fromUserId, toUserId);
		//return result;
		try {
			subScribeRepository.mSubscribe(fromUserId, toUserId);
		}catch (Exception e) {
			throw new CustomApiException("이미 구독을 하였습니다.");
		}
	}
	
	@Transactional //데이터 베이스에 영향이 갈때 걸어줌
	public void 구독취소하기(int fromUserId, int toUserId) {
		subScribeRepository.mUnSubscribe(fromUserId, toUserId);
		//int result = subScribeRepository.mUnSubscribe(fromUserId, toUserId);
		//return result;
	}
}
