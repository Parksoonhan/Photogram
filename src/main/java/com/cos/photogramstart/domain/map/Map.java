package com.cos.photogramstart.domain.map;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.likes.Likes;
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
public class Map {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호증가 전략이 데이터베이스를 따라간다.
	private int id;
	private String title;
	private String detail;
	private String date;
	private String member;
	private String course;
	
	@JsonIgnoreProperties({"image", "map"}) //유저 정보가 들고있는 이미지까진 필요 없음
	@JoinColumn(name="userId")
	@ManyToOne(fetch = FetchType.EAGER) //이미지를 select 하면 조인해서 User정보를 같이 들고옴
	private User user; // 1

	// 이미지 좋아요
	@JsonIgnoreProperties({"image", "map"})
	@OneToMany(mappedBy = "map") 
	private List<Likes> likes;
	
	// 댓글
	@OrderBy("id DESC")
	//양방향 매핑 .. 같이 들고와야되니가
	//lazy 적용 ..  인기 페이지에선 안 가져오도록 적용 시켜줌
	@JsonIgnoreProperties({"image", "map"}) //user엔 없는데 image엔 있어서 무한 참조가 일어나는 것임 (1번 이미지에 대한 댓글을 가져오는데 또 거기서 이미지를 들고와버리면 ... 무한굴레)
	@OneToMany(mappedBy = "map") 
	private List<Comment> comments;
	
	private LocalDateTime createDate;
	
	@Transient //db에 컬럼이 만들어지지 않는다.
	private boolean likeState;
	
	@Transient
	private int likeCount;
	
	@PrePersist //디비에 insert되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

}


