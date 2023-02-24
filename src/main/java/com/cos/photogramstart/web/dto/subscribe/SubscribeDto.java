package com.cos.photogramstart.web.dto.subscribe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscribeDto {
	private int id;
	private String username;
	private String profileImageUrl;
	//Integer로 한 이유 mariaDB는 int값을 못 받는다더라
	private Integer subscribeState;
	private Integer equalUserState;
}
