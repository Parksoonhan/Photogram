package com.cos.photogramstart.domain.likes;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.map.Map;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.subscribe.Subscribe;
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
@Table( //유니크 제약조건 1번 유저가 1번 이미지를 좋아요 한번만 할 수 있게
		uniqueConstraints = {
				@UniqueConstraint(
						name = "likes_uk",
						columnNames = {"imageId", "mapId" ,"userId"} //실제 컬럼명 적어줘야함
				)
		}
)
public class Likes { //N
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호증가 전략이 데이터베이스를 따라간다.
	private int id;
	
	//무한참조됨
	@JoinColumn(name="imageId")
	@ManyToOne //기본적인 전략이 EAGER OneToMany가 LAZY 전략임
	private Image image; //1
	
	//무한참조됨
	@JoinColumn(name="mapId")
	@ManyToOne //기본적인 전략이 EAGER OneToMany가 LAZY 전략임
	private Map map; //1

	//likes 안에 유저에 이미지가 나오면 오류가 나기때문에 그걸 잡아줘야함
	// 오류가 터지고 나서 잡아봅시다.
	@JsonIgnoreProperties({"images"})
	@JoinColumn(name = "userId")
	@ManyToOne
	private User user; // 1
	
	private LocalDateTime createDate;
	 
	@PrePersist //디비에 insert되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
	
}
