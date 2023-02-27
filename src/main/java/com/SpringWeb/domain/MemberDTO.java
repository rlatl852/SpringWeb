package com.SpringWeb.domain;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {

	private String userId;
	private String userpw;
	private String userName;
	private LocalDateTime regDate;
	private String email;
	private String phoneNumber;
	private List<AuthDTO> authList;
}
