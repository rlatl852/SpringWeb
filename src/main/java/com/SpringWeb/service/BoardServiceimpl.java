package com.SpringWeb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SpringWeb.domain.BoardAttachDTO;
import com.SpringWeb.domain.BoardDTO;
import com.SpringWeb.domain.Criteria;
import com.SpringWeb.mapper.BoardAttachMapper;
import com.SpringWeb.mapper.BoardMapper;
import com.SpringWeb.mapper.ReplyMapper;

@Service
public class BoardServiceimpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private ReplyMapper replyMapper;
	
	@Autowired
	private BoardAttachMapper attachMapper;
	
	@Override
	public List<BoardDTO> readAll(Criteria criteria){
		return boardMapper.getList(criteria);
	}
	
	@Override
	public int totalCount(Criteria criteria) {
		return boardMapper.totalCount(criteria);
	}
	
	@Override
	public BoardDTO read(Long bno) {
		return boardMapper.get(bno);
	}
	
	@Override
	@Transactional
	public void write(BoardDTO vo) {
		boardMapper.insert(vo);
		if(vo.getAttachList() == null || vo.getAttachList().size() <= 0) {
			return;
		}
		vo.getAttachList().forEach(attach -> {
			attach.setBno(vo.getBno());
			attachMapper.insert(attach);
		});
	}
	
	@Override
	public void modify(BoardDTO vo) {
		attachMapper.deleteAll(Long.valueOf(vo.getBno()));
		boardMapper.update(vo);
		if(vo.getAttachList() !=null && vo.getAttachList().size() > 0) {
			vo.getAttachList().forEach(attach -> {
				attach.setBno(vo.getBno());
				attachMapper.insert(attach);
			});
		}
	}
	
	@Override
	@Transactional
	public void remove(Long bno) {
		replyMapper.deleteAll(bno);
		attachMapper.deleteAll(bno);
		boardMapper.delete(bno);
	}
	
	@Override
	public void readCount(Long bno) {
		boardMapper.readCount(bno);
	}

	@Override
	public List<BoardAttachDTO> getAttachList(Long bno) {
		return attachMapper.findByBno(bno);
	}
}
