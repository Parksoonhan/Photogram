package com.cos.photogramstart.config.oauth.provider;

public interface OAuth2UserInfo {
	String getProviderId();
	String getProvider(); //구글,카카오, 네이버 등등
	String getEmail();
	String getName();
	String getGender();
	String getMobile();
	
	
}
