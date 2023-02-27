package com.SpringWeb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SpringWeb.domain.MemberDTO;
import com.SpringWeb.mapper.MemberMapper;

@Service
public class MemberServiceimpl implements MemberService {

	@Autowired
	private MemberMapper mapper;
	
	@Override
	public void addMember(MemberDTO vo) {
		mapper.addMember(vo);
	}

	@Override
	public void addAuth(MemberDTO vo) {
		mapper.addAuth(vo);
	}

	@Override
	public int getIdCheck(MemberDTO vo) {
		return mapper.getId(vo);
	}
}
