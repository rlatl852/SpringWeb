package com.SpringWeb.service;

import java.util.List;

import com.SpringWeb.domain.Criteria;
import com.SpringWeb.domain.ProductDTO;

public interface ProdService {

	List<String> groupNameList();
	
	List<ProductDTO> getProdList(ProductDTO pDto, Criteria criteria);
	
	int totalCount(ProductDTO pDto);
	
	ProductDTO getProduct(ProductDTO pDto);
}
