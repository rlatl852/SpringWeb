package com.SpringWeb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.SpringWeb.domain.BoardDTO;
import com.SpringWeb.domain.Criteria;

public interface BoardMapper {

	List<BoardDTO> getList(Criteria Criteria);
	
	int totalCount(Criteria criteria);
	
	BoardDTO get(Long bno);
	
	void insert(BoardDTO vo);
	
	void update(BoardDTO vo);
	
	void delete(Long bno);
	
	void readCount(Long bno);
	
	void updateReplyCnt(
			@Param("bno") Long bno,
			@Param("amount") int amount
	);
}
