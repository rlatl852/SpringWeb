package com.SpringWeb.service;

import java.util.List;

import com.SpringWeb.domain.ReplyDTO;

public interface ReplyService {

	List<ReplyDTO> readAll(Long bno);
	
	ReplyDTO read(Long rno);
	
	void register(ReplyDTO vo);
	
	void remove(Long rno);
	
	void modify(ReplyDTO vo);
}
