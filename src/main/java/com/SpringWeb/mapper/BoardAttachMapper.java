package com.SpringWeb.mapper;

import java.util.List;

import com.SpringWeb.domain.BoardAttachDTO;

public interface BoardAttachMapper {

	public void insert(BoardAttachDTO attachVO);
	public void delete(String uuid);
	public List<BoardAttachDTO> findByBno(Long bno);
	public void deleteAll(Long bno);
	public List<BoardAttachDTO> getOldFiles();
}
