package com.SpringWeb.controller;

import java.security.Principal;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.SpringWeb.domain.MemberDTO;
import com.SpringWeb.service.MemberService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/member")
@Log4j
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/login")
	public String loginInput(@RequestParam(value = "error", required = false) String error,
							@RequestParam(value = "exception", required = false) String exception, Model model) {
		if(error != null){
			model.addAttribute("error", error);
		}
		if(exception != null) {
			model.addAttribute("exception", exception);
		}
		return "/member/login";
	}
	
	@GetMapping("/logout")
	public void logoutGet(){
		System.out.println("custom logout");
	}
	
	@GetMapping("/addMember")
	public String addMember() {
		return "member/addMember";
	}
	
	@PostMapping("/addMember")
	public String addMemberA(MemberDTO vo) {
		BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
		vo.setUserpw(pwEncoder.encode(vo.getUserpw()));
		if(vo.getUserId() != "" && vo.getUserpw() != "" && vo.getUserName() != "" && vo.getEmail() != "" && vo.getPhoneNumber() != "") {
			if(memberService.getIdCheck(vo) == 0) {
				memberService.addMember(vo);
				memberService.addAuth(vo);
			}
		}
		return "home";
	}
	
	@GetMapping("/getIdCheck")
	@ResponseBody
	public String idCheck(MemberDTO vo) {
		int result = memberService.getIdCheck(vo);
		String check = "";
		if(result > 0) {
			check = "N";
		} else {
			check = "Y";
		}
		System.out.println(check);
		return check;
	}
	
	@GetMapping("/updateMember")
	public String updateMember(MemberDTO vo){
		return "/member/updateMember";
	}
	
	@GetMapping("/passwordCheck")
	public String passwordCheck(MemberDTO vo){
		System.out.println("password : " + vo.getUserpw());
		return "/member/updateMember";
	}
}
