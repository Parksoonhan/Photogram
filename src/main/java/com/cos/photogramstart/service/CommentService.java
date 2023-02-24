package com.cos.photogramstart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.comment.CommentRepository;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.domain.map.Map;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public Comment 이미지댓글쓰기(String content, int imageId, int userId) {
		//mSave로 내가 새로 만들어줄 게 아니라 기본 jpa 메소드인 save를 사용해줘야함
		//왜냐면 리턴받는 완벽한 정보값이 필요하기 때문이다.
		//return commentRepository.mSave(content, imageId, userId);
		
		//Tip. set해주는 거 귀찮으니까 가짜 객체를 만들어주기
		//객체를 만들 때 아이디 값만 담아서 insert 할 수 있다
		//대신 return 시에 image객체와 user객체는 id값만 가지고 있는 빈 객체를 리턴받는다.
		Image image = new Image();
		image.setId(imageId);
		
		User userEntity = userRepository.findById(userId).orElseThrow(()->{
			throw new CustomApiException("유저 아이디를 찾을 수 없습니다");
		});
		
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setImage(image);
		comment.setUser(userEntity);
		
		return commentRepository.save(comment);
	}

	
	@Transactional
	public Comment 일정댓글쓰기(String content, int mapId, int userId) {
		Map map = new Map();
		map.setId(mapId);
		
		User userEntity = userRepository.findById(userId).orElseThrow(()->{
			throw new CustomApiException("유저 아이디를 찾을 수 없습니다");
		});
		
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setMap(map);
		comment.setUser(userEntity);
		
		return commentRepository.save(comment);
	}
	
	@Transactional
	public void 댓글삭제(int id) {
		try {
			commentRepository.deleteById(id);
		}catch (Exception e) {
			throw new CustomApiException(e.getMessage());
		}
		
	}
}
