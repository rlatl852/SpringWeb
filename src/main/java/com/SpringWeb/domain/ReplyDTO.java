package com.SpringWeb.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyDTO {
	
	private long rno;
	private long bno;
	private String reply;
	private String replyer;
	private Date regDate;
}
