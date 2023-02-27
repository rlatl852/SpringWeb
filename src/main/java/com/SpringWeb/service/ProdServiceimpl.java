package com.SpringWeb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SpringWeb.domain.Criteria;
import com.SpringWeb.domain.ProductDTO;
import com.SpringWeb.mapper.ProdMapper;

@Service
public class ProdServiceimpl implements ProdService {

	@Autowired
	ProdMapper prodMapper;
	
	@Override
	public List<String> groupNameList() {
		return prodMapper.getGroupName();
	}

	@Override
	public List<ProductDTO> getProdList(ProductDTO pDto, Criteria criteria) {
		return prodMapper.prodList(pDto, criteria);
	}

	@Override
	public int totalCount(ProductDTO pDto) {
		return prodMapper.totalCount(pDto);
	}

	@Override
	public ProductDTO getProduct(ProductDTO pDto) {
		return prodMapper.getProd(pDto);
	}

}
