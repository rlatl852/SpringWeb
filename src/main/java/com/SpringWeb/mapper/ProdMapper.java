package com.SpringWeb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.SpringWeb.domain.Criteria;
import com.SpringWeb.domain.PageVO;
import com.SpringWeb.domain.ProductDTO;

public interface ProdMapper {

	List<String> getGroupName();
	
	List<ProductDTO> prodList(
			@Param("pDto") ProductDTO pDto,
			@Param("criteria") Criteria criteria
			);
	
	int totalCount(ProductDTO pDto);
	
	ProductDTO getProd(ProductDTO pDto);
}
