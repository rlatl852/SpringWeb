package com.SpringWeb.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Criteria {
	
	private int pageNum;
	private int amount;
	
	private String type;
	private String keyword;
	
	public Criteria() {
		this(1, 10);
	}
	
	public Criteria(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}
	
	public int getStart() {
		return (this.pageNum - 1) * this.amount;
	}
	
	public String[] getTypeCollection() {	// #{typeCollection }
		// type => T, C, W, TC, CW, TW
		return this.type != null ? type.split("") : new String[] {};
		
	}
}
