package com.SpringWeb.mapper;

import com.SpringWeb.domain.MemberDTO;

public interface MemberMapper {

	public MemberDTO read(String userid);
	
	public void addMember(MemberDTO vo);
	
	public void addAuth(MemberDTO vo);
	
	int getId(MemberDTO vo);
}
