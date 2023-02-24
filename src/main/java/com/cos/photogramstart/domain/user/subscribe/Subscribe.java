package com.cos.photogramstart.domain.user.subscribe;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cos.photogramstart.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder //빌더패턴 사용
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity //디비에 테이블을 생성
@Table( //유니크 제약조건 거는법 (id 1이 2를 두번 구독할 수 없게끔)
		uniqueConstraints = {
				@UniqueConstraint(
						name = "subscribe_uk",
						columnNames = {"fromUserId", "toUserId"} //실제 컬럼명 적어줘야함
				)
		}
)
public class Subscribe {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호증가 전략이 데이터베이스를 따라간다.
	private int id;
	
	//ORM따라 만들어진 컬럼명 바꾸기
	@JoinColumn(name="fromUserId")
	@ManyToOne
	private User fromUser;
	
	@JoinColumn(name="toUserId")
	@ManyToOne
	private User toUser;
	
	private LocalDateTime createDate;
	
	@PrePersist //디비에 insert되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
