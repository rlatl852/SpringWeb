package com.SpringWeb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SpringWeb.domain.ReplyDTO;
import com.SpringWeb.mapper.BoardMapper;
import com.SpringWeb.mapper.ReplyMapper;

@Service
public class ReplyServiceimpl implements ReplyService {

	private static final int REPLY_ADD_COUNT = 1;
	private static final int REPLY_SUB_COUNT = -1;
	
	@Autowired
	private ReplyMapper mapper;
	
	@Autowired
	private BoardMapper boardMapper;
	
	@Override
	public List<ReplyDTO> readAll(Long bno) {
		return mapper.getReplyList(bno);
	}

	@Override
	public ReplyDTO read(Long rno) {
		return mapper.get(rno);
	}

	@Override
	@Transactional
	public void register(ReplyDTO vo) {
		boardMapper.updateReplyCnt(vo.getBno(), REPLY_ADD_COUNT);;
		mapper.insert(vo); // 댓글 등록
	}

	@Override
	@Transactional
	public void remove(Long rno) {
		boardMapper.updateReplyCnt(mapper.get(rno).getBno(), REPLY_SUB_COUNT);
		mapper.delete(rno); // 댓글 삭제
	}

	@Override
	public void modify(ReplyDTO vo) {
		mapper.update(vo);
	}
	
	

}
