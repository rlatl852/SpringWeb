package com.SpringWeb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.SpringWeb.domain.CustomUser;
import com.SpringWeb.domain.MemberDTO;
import com.SpringWeb.mapper.MemberMapper;

import lombok.extern.log4j.Log4j;

@Log4j
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	MemberMapper memberMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("Load User By UserName : " + username);
		log.info(memberMapper.read(username));
		MemberDTO vo = memberMapper.read(username);
		if(vo==null) {
			throw new UsernameNotFoundException(username);
		}
		System.out.println("queried by member mapper : " + vo);
		return new CustomUser(vo);
	}
}
