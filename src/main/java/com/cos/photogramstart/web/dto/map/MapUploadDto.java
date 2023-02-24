package com.cos.photogramstart.web.dto.map;

import java.util.ArrayList;

import com.cos.photogramstart.domain.map.Map;
import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data
public class MapUploadDto {
	private String title;
	private String detail;
	private String date;
	private String member;
	private String course;
	
	public Map toEntity(User user) {
		return Map.builder()
				.title(title)
				.detail(detail)
				.date(date)
				.member(member)
				.course(course)
				.user(user)
				.build();
	}
}
