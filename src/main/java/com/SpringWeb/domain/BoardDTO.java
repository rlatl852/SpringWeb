package com.SpringWeb.domain;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDTO {

	private int bno;	// 게시글 번호
	private String title, content, writer;	// 게시글 제목, 게시글 내용, 게시글 작성자
	private Date regdate;	// 게시글 등록일
	
	private Long readCount, replyCnt;	// 게시글 조회수
	private List<BoardAttachDTO> attachList;
}
