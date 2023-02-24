package com.cos.photogramstart.domain.user;
//JPA - Java Persistence API (자바로 데이터를 영구적으로 저장(JPA)할 수 잇는 API를 제공)

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.map.Map;
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
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호증가 전략이 데이터베이스를 따라간다.
	private int id;
	
	//스키마.. 테이블이 변경된 것이기 때문에 yml 파일을 다시 create -> update 해줘야함
	@Column(length=100, unique = true) //유니크 제약조건 걸어줌 같은 값이 못 들어가게, OAuth2 로그인을 위해 컬럼 크기 늘리기
	private String username;
	@Column(nullable=false)
	private String password;
	@Column(nullable=false)
	private String name;
	private String website;
	private String bio;
	@Column(nullable=false)
	private String email;
	private String phone;
	private String gender;
	
	private String profileImageUrl; //사진
	private String role; //권한
	
	//양방향 매핑!!!
	//나는 연관관계의 주인이 아니다. 그러므로 테이블에 컬럼을 만들지마 대신 데이터는 가져와
	//User를 select 할 때 User id로 등록된 image들을 다 가져와!
	//EAGER =  User를 select할 때 해당 user id로 등록된 image들을 전부 Join 해서 가져와!!!
	//LAZY = User를 select할 때 해당 user id로 등록된 image들을 가져오지마 - 대신 getImages() 함수가 호출될 때 가져와!
	//회원 페이지 /user/1 보여줄 때 회원정보 + 이미지리스트를 가지고 오고 싶을 때 사용
	@OneToMany(mappedBy = "user", fetch= FetchType.LAZY) 
	@JsonIgnoreProperties({"user"}) //JPA 무한 참조가 되지 않게 하기 위해서 (user 정보 - image 정보 ,,, 또 다시 user정보 image정보 호출 되니까)
	private List<Image> images;
	
	@OneToMany(mappedBy = "user", fetch= FetchType.LAZY) 
	@JsonIgnoreProperties({"user"}) //JPA 무한 참조가 되지 않게 하기 위해서 (user 정보 - image 정보 ,,, 또 다시 user정보 image정보 호출 되니까)
	private List<Map> maps;
	
	private LocalDateTime createDate;
	
	@PrePersist //디비에 insert되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

	@Override
	public String toString() { //images 지우기
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", website="
				+ website + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", gender=" + gender
				+ ", profileImageUrl=" + profileImageUrl + ", role=" + role + ", createDate="
				+ createDate + "]";
	}

	
}
