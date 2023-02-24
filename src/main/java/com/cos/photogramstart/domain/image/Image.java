package com.cos.photogramstart.domain.image;

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
import com.cos.photogramstart.domain.likes.Likes;
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
public class Image { //N

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호증가 전략이 데이터베이스를 따라간다.
	private int id;
	private String caption;
	private String postImageUrl; //사진을 전송받아서 그 사진을 서버에 특정 폴더에 저장 - DB에 그 저장된 경로를 insert
	
	@JsonIgnoreProperties({"image"}) //유저 정보가 들고있는 이미지까진 필요 없음
	@JoinColumn(name="userId")
	@ManyToOne(fetch = FetchType.EAGER) //이미지를 select 하면 조인해서 User정보를 같이 들고옴
	private User user; // 1
	
	// 이미지 좋아요
	@JsonIgnoreProperties({"image"})
	@OneToMany(mappedBy = "image") 
	private List<Likes> likes;
	
	// 댓글
	@OrderBy("id DESC")
	//양방향 매핑 .. 같이 들고와야되니가
	//lazy 적용 ..  인기 페이지에선 안 가져오도록 적용 시켜줌
	@JsonIgnoreProperties({"image"}) //user엔 없는데 image엔 있어서 무한 참조가 일어나는 것임 (1번 이미지에 대한 댓글을 가져오는데 또 거기서 이미지를 들고와버리면 ... 무한굴레)
	@OneToMany(mappedBy = "image") 
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

	//@Data로 게터세터 만들었지만 또 호출
	//오브젝트를 콘솔에 출력할 때 문제가 될 수 있어서 User 부분을 출력되지 않게 함.
//	@Override
//	public String toString() {
//		return "Image [id=" + id + ", caption=" + caption + ", postImageUrl=" + postImageUrl //+ ", user=" + user
//				+ ", createDate=" + createDate + "]";
//	}
	
	
}
