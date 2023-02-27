package com.SpringWeb.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.SpringWeb.domain.BoardAttachDTO;
import com.SpringWeb.domain.BoardDTO;
import com.SpringWeb.domain.Criteria;

public interface BoardService {

	List<BoardDTO> readAll(Criteria criteria);
	
	int totalCount(Criteria criteria);
	
	BoardDTO read(Long bno);
	
	void write(BoardDTO vo);
	
	void modify(BoardDTO vo);
	
	void remove(Long bno);
	
	void readCount(Long vo);
	
	List<BoardAttachDTO> getAttachList(Long bno);
}
