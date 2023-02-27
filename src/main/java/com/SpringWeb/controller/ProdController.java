package com.SpringWeb.controller;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.SpringWeb.domain.BoardDTO;
import com.SpringWeb.domain.Criteria;
import com.SpringWeb.domain.PageVO;
import com.SpringWeb.domain.ProductDTO;
import com.SpringWeb.service.ProdService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/Prod")
@Log4j
public class ProdController {

	@Autowired
	ProdService prodService;
	
	// 제품 검색 목록
	@GetMapping("/list")
	public String prodList(Model model, Criteria criteria, ProductDTO pDto) {
		List<String> readGroupList = prodService.groupNameList();
		model.addAttribute("groupName", readGroupList);
		return "prod/prodList";
	}
	
	// 제품 목록
	@GetMapping("/getList")
	public String getProdList(ProductDTO pDto, Model model, Criteria criteria){
		List<String> readGroupList = prodService.groupNameList();
		List<ProductDTO> list = prodService.getProdList(pDto, criteria);
		PageVO pageVO = new PageVO(prodService.totalCount(pDto), criteria);
		model.addAttribute("groupName", readGroupList);
		model.addAttribute("list", list);
		model.addAttribute("pageMarker", pageVO);
		return "prod/prodList";
	}
	
	@GetMapping("/getProd")
	@ResponseBody
	public ProductDTO getProduct(ProductDTO pDto) {
		log.info(pDto.getFood_cd());
		return prodService.getProduct(pDto);
	}
}
