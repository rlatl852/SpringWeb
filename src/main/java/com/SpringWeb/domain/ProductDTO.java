package com.SpringWeb.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {

	private String food_cd = "";		// 식품코드
	private String group_name = "";		// 식품군
	private String desc_kor = "";		// 식품 이름
	private int research_year = 0;		// 조사년도
	private String maker_name = "";		// 제조사명
	private String sub_ref_name = "";	// 자료출처
	private double serving_size = 0.0;	// 총내용량
	private double nutr_cont1 = 0.0;	// 열량(kcal)(1회제공량당)
	private double nutr_cont2 = 0.0;	// 탄수화물(g)(1회제공량당)
	private double nutr_cont3 = 0.0;	// 단백질(g)(1회제공량당)
	private double nutr_cont4 = 0.0;	// 지방(g)(1회제공량당)
	private double nutr_cont5 = 0.0;	// 당류(g)(1회제공량당)
	private double nutr_cont6 = 0.0;	// 나트륨(mg)(1회제공량당)
	private double nutr_cont7 = 0.0;	// 콜레스테롤(mg)(1회제공량당)
	private double nutr_cont8 = 0.0;	// 포화지방산(g)(1회제공량당)
	private double nutr_cont9 = 0.0;	// 트랜스지방(g)(1회제공량당)
	private String ingredient = "";
}
