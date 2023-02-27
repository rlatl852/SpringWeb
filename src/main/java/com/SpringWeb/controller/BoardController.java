package com.SpringWeb.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.SpringWeb.domain.BoardAttachDTO;
import com.SpringWeb.domain.BoardDTO;
import com.SpringWeb.domain.Criteria;
import com.SpringWeb.domain.PageVO;
import com.SpringWeb.service.BoardService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/Board")
@Log4j
public class BoardController {
	
	@Autowired
	private BoardService service;
	
	// 게시물 목록
	@GetMapping("/list")
	public String boardList(Model model, Criteria criteria) {
		List<BoardDTO> readAll = service.readAll(criteria);
		PageVO pageVO = new PageVO(service.totalCount(criteria), criteria);
		model.addAttribute("list", readAll);
		model.addAttribute("pageMarker", pageVO);
		return "board/boardList";
	}
	
	//게시물 조회
	@GetMapping("/content")
	public String contentString(long bno, Model model, HttpServletRequest request,
            HttpServletResponse response) {
		BoardDTO read = service.read(bno);
		model.addAttribute("board", read);
		Cookie oldCookie = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("postView")) {
					oldCookie = cookie;
				}
			}
		}
		
		if (oldCookie != null) {
			if (!oldCookie.getValue().contains("["+ bno +"]")) {
				this.service.readCount(bno);
				oldCookie.setValue(oldCookie.getValue() + "_[" + bno + "]");
				oldCookie.setPath("/");
				oldCookie.setMaxAge(60 * 60 * 24); 							// 쿠키 시간
				response.addCookie(oldCookie);
			}
		} else {
			this.service.readCount(bno);
			Cookie newCookie = new Cookie("postView", "[" + bno + "]");
			newCookie.setPath("/");
			newCookie.setMaxAge(60 * 60 * 24); 								// 쿠키 시간
			response.addCookie(newCookie);
		}
		
		return "board/content";
	}
	
	//게시물 쓰기 폼
	@PostAuthorize("isAuthenticated()")
	@GetMapping("/write")
	public String writeForm() {
		return "board/write";
	}
	
	// 게시글 등록 처리
	@PostAuthorize("isAuthenticated()")
	@PostMapping("/write")
	public String write(BoardDTO vo, RedirectAttributes rttr) {
		//log.info("========================================");
		//log.info("register : " + vo);
		//if(vo.getAttachList() != null) {
		//	vo.getAttachList().forEach(attach -> log.info(attach));
		//}
		//log.info("========================================");
		service.write(vo);
		rttr.addFlashAttribute("regResult", vo.getBno());
		return "redirect:/Board/list"; // foward 방식 금지(board/boardList)
	}
	
	// 수정 폼으로 이동
	@PostAuthorize("isAuthenticated() and #model[board].writer == principal.memberDTO.userName")
	@GetMapping("/modify")
	public String modifyForm(Long bno, Model model) {
		BoardDTO read = service.read(bno);
		model.addAttribute("board", read);
		return "board/modify";
	}

	// 수정 처리
	@PreAuthorize("isAuthenticated() and #vo.writer == principal.memberDTO.userName")
	@PostMapping("/modify")
	public String modify(BoardDTO vo, RedirectAttributes rttr) {
		service.modify(vo);
		rttr.addFlashAttribute("modResult", vo.getBno());
		return "redirect:list";
	}
	
	@PostMapping("/remove")
	@PreAuthorize("isAuthenticated() and #writer == principal.memberDTO.userName or hasRole('ROLE_ADMIN')")
	public String remove(String writer, Long bno, RedirectAttributes rttr) {
		List<BoardAttachDTO> attachList = service.getAttachList(bno);
		service.remove(bno);
		deleteFile(attachList);
		rttr.addFlashAttribute("delResult", bno);
		return "redirect:list";
	}
	
	@GetMapping("/getAttachList")
	@ResponseBody
	public ResponseEntity<List<BoardAttachDTO>> getAttachList(long bno){
		return new ResponseEntity<List<BoardAttachDTO>>(service.getAttachList(bno), HttpStatus.OK);
	}
	
	private void deleteFile(List<BoardAttachDTO> attachList) {
		if(attachList == null || attachList.size() == 0) return;
		attachList.forEach(attach -> {
			try {
				Path file = Paths.get("C:\\Users\\slumb\\Desktop\\김영환\\uploadFile\\" + attach.getUploadPath() + "\\" + attach.getUuid() + "_" + attach.getFileName());
				Files.deleteIfExists(file);
				if(Files.probeContentType(file).startsWith("image")) {
					Path thumbNail = Paths.get("C:\\Users\\slumb\\Desktop\\김영환\\uploadFile\\" + attach.getUploadPath() + "\\s_" + attach.getUuid() + "_" + attach.getFileName());
					log.info(thumbNail);
					Files.delete(thumbNail);
				}
			} catch (Exception e) {
				log.error("delete file error" + e.getMessage());
			}
		});
	}
}
