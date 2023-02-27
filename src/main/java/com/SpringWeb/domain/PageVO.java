package com.SpringWeb.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageVO {

	private int startPage;
	private int endPage;
	private int realEndPage;
	private int totalCount;
	private int displayPageNum = 10; //페이지 버튼 수
	private int viewPage;
	private Criteria criteria;
	
	private boolean prev ;// 이전페이지
	private boolean next ;// 다음페이지
	
	public PageVO(int totalCount, Criteria criteria) {
		this.totalCount = totalCount;
		this.criteria = criteria;
		this.viewPage = criteria.getPageNum();
		endPage = (int) Math.ceil(criteria.getPageNum()/(double)displayPageNum) * displayPageNum;
		startPage = endPage - displayPageNum + 1;
		// 게시물 412개 실제 마지막 페이지는 (올림)412/10=41.2
		realEndPage = (int) Math.ceil(totalCount/(double)criteria.getAmount());
		// 412/10.0 => 41.2 => 42.0 => 42
		
		//이전 페이지는 1~10 페이지를 제외하고 모두 출력 : true
		// 1~10페이지는 startPage가 항상1
		prev = startPage != 1; // ? true : false; 삼항연산자
		next = endPage < realEndPage;
		
		// endPage		realEndPage
		//1~10 : 10			42
		//11~20 : 20		42
		//...
		//41~50 : 50		42
		if(endPage > realEndPage) endPage = realEndPage;
	}
}
