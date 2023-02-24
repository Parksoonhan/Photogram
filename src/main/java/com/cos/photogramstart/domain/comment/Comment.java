package com.cos.photogramstart.domain.comment;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.likes.Likes;
import com.cos.photogramstart.domain.map.Map;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder //빌더패턴 사용
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity //디비에 테이블을 생성
public class Comment { //1
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호증가 전략이 데이터베이스를 따라간다.
	private int id;
	
	@Column(length = 100, nullable = false)
	private String content; //1
	
	@JsonIgnoreProperties({"images"}) //유저 정보가 들고있는 이미지까진 필요 없음
	@JoinColumn(name="userId")
	//하나 셀렉트 할 때 따라오는 게 하나면 EAGER사용
	@ManyToOne(fetch = FetchType.EAGER)
	private User user; //n
	
	@JoinColumn(name="imageId")
	@ManyToOne(fetch = FetchType.EAGER)
	private Image image;
	
	@JoinColumn(name="mapId")
	@ManyToOne(fetch = FetchType.EAGER)
	private Map map;
	
	private LocalDateTime createDate;
	
	@PrePersist //디비에 insert되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

}
