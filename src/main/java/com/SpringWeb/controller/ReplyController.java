package com.SpringWeb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SpringWeb.domain.ReplyDTO;
import com.SpringWeb.service.ReplyService;

import lombok.extern.log4j.Log4j;

@Controller
@RestController
@RequestMapping("/Replies")
public class ReplyController {

	@Autowired
	private ReplyService service;
	
	@GetMapping("/get/{bno}")
	public ResponseEntity<List<ReplyDTO>> getList(@PathVariable Long bno){
		/*
		 * if(service.readAll(bno).isEmpty()) { return new
		 * ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); }
		 */
		return new ResponseEntity<>(service.readAll(bno), HttpStatus.OK);
	}
	
	// 댓글 정보를 반환
	@PostAuthorize("isAuthenticated() and #model[replyObject].replyer == principal.memberDTO.userName or hasRole('ROLE_ADMIN')")
	@GetMapping("/{rno}")
	public ResponseEntity<ReplyDTO> get(@PathVariable Long rno, Model model){
		ReplyDTO read = service.read(rno);
		model.addAttribute("replyObject", read);
		return new ResponseEntity<ReplyDTO>(service.read(rno), HttpStatus.OK);
	}
	
	//댓글 작성
	@PostAuthorize("isAuthenticated()")
	@PostMapping("/new")
	public ResponseEntity<String> create(@RequestBody ReplyDTO vo){
		service.register(vo);
		return new ResponseEntity<String>("sucess", HttpStatus.OK);
	}
	
	//댓글 수정
	@PreAuthorize("isAuthenticated() and #vo.replyer == principal.memberDTO.userName or hasRole('ROLE_ADMIN')")
	@PutMapping("/{rno}")
	public ResponseEntity<String> update(@RequestBody ReplyDTO vo, @PathVariable Long rno){
		vo.setRno(rno);
		service.modify(vo);
		return new ResponseEntity<String>("sucess", HttpStatus.OK);
	}
	
	//댓글 삭제
	@PreAuthorize("isAuthenticated() and #vo.replyer == principal.memberDTO.userName or hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{rno}")
	public ResponseEntity<String> delete(@RequestBody ReplyDTO vo){
		System.out.println(vo);
		service.remove(vo.getRno());
		return new ResponseEntity<String>("sucess", HttpStatus.OK);
	}
}
