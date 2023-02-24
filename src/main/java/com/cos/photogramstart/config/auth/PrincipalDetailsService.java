package com.cos.photogramstart.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
//로그인 요청이 들어오면 UserDetailsService이 낚아챔 (.loginProcessingUrl("/auth/signin")->이 요청)
@Service //ioC
public class PrincipalDetailsService implements UserDetailsService{ 

	private final UserRepository userRepository;
	
	//1.패스워드는 알아서 체킹하니까 신경 쓸 필요없다.
	//2.리턴이 잘 되면 자동으로 UserDetails에 세션을 만든다.
	@Override
	//시큐리티가 알아서 비번 처리 해줘서 username만 확인하면 됨
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User userEntitiy = userRepository.findByUsername(username);
		
		if(userEntitiy == null) {
			return null;
		}else {
			return new PrincipalDetails(userEntitiy); //얘가 세션에 저장이 되는 것임
		}
	}

}
