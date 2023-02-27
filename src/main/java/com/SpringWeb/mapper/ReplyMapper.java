package com.SpringWeb.mapper;

import java.util.List;

import com.SpringWeb.domain.ReplyDTO;

public interface ReplyMapper {

	List<ReplyDTO> getReplyList(Long bno);
	
	ReplyDTO get(Long rno);
	
	int insert(ReplyDTO vo);
	
	void delete(Long rno);
	
	void update(ReplyDTO vo);
	
	void deleteAll(Long rno);
}
