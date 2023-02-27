package com.SpringWeb.service;

import com.SpringWeb.domain.MemberDTO;

public interface MemberService {

	void addMember(MemberDTO vo);

	void addAuth(MemberDTO vo);
	
	int getIdCheck(MemberDTO vo);
}
