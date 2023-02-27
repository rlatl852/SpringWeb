<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../layout/header.jspf"%>
<div class="container">
	<div class="jumbotron text-center">
		<h1 class="display-4">제품 검색</h1>
	</div>
	<form class="form-inline my-3" id="prodSearchForm" action="${contextPath }/Prod/getList" method="get">
		<select class="form-control mr-sm-2" name="group_name" id="group_name">
			<option value="">식품군 선택</option>
			<c:forEach items="${groupName }" var="gn">
				<option value="${gn }"
					${param.group_name == '${gn}' ? 'selected="selected"' : ''}>${gn }</option>
			</c:forEach>
		</select>
		<input class="form-control mr-sm-2" name="maker_name" id="maker_name" value="${param.maker_name }" placeholder="제조사">
		<input class="form-control mr-sm-2" name="desc_kor" id="desc_kor" value="${param.desc_kor }" placeholder="제품명">
		<button class="btn btn-success">검색</button>
	</form>
	<form id="prodForm">
		<table class="table table-hover">
			<thead>
				<tr>
					<th class="text-center" width="50">번호</th>
					<th class="text-center" width="250">식품명</th>
					<th class="text-center" width="100">조사년도</th>
					<th class="text-center" width="150">제조사</th>
					<th class="text-center" width="100">자료원</th>
					<th class="text-center" width="50">용량</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${not empty list}">
						<c:forEach items="${list }" var="p" varStatus="i">
							<tbody>
								<tr>
									<td class="text-center">${((pageMarker.totalCount - i.index - 1) -  ((pageMarker.viewPage-1)*pageMarker.displayPageNum))+1}</td>
									<td>
										<a href="${p.food_cd }">${p.desc_kor }</a>
									</td>
									<td class="text-center">${p.research_year }</td>
									<td class="text-center">${p.maker_name }</td>
									<td class="text-center">${p.sub_ref_name }</td>
									<td class="text-center">${p.serving_size }</td>
								</tr>
							</tbody>
						</c:forEach>
					</c:when>
					<c:when test="${empty list}">
						<tbody>
							<tr>
								<td colspan="6">
									<p class="text-center">제품을 검색하세요</p>
								</td>
							</tr>
						</tbody>
					</c:when>
				</c:choose>
			</tbody>
		</table>
	</form>
	<c:if test="${not empty pageMarker }">
		<form id="pageForm">
			<div class="pagination justify-content-center">
				<c:if test="${pageMarker.prev }">
					<a href="1" class="page-link">[처음]</a>
					<a href="${pageMarker.startPage-1 }" class="page-link"><<</a>
				</c:if>
				<c:forEach begin="${pageMarker.startPage }" end="${pageMarker.endPage }" var="page">
					<a href="${page }" class="page-link">${page }</a>
				</c:forEach>
				<c:if test="${pageMarker.next }">
					<a href="${pageMarker.endPage+1 }" class="page-link">>></a>
					<a href="${pageMarker.realEndPage }" class="page-link">[마지막]</a>
				</c:if>
			</div>
		</form>
	</c:if>

	<!-- The Modal -->
	<div class="modal" id="replyModal" data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">

				<!-- Modal Header -->
				<div class="modal-header header">
					<h4 class="modal-title">제품 영양성분</h4>
					<button type="button" class="close" data-dismiss="modal">&times;</button>
				</div>

				<!-- Modal body -->
				<div class="modal-body body">
				</div>
				
			</div>
		</div>
	</div>
</div>
<script>
	$(function() {
		//페이지 이동
		$("#pageForm a").on("click", function(e) {
			e.preventDefault(); // 기본동작(href) 동작 금지
			let pageNum = $(this).attr('href');
			let inputTag = $("<input/>", {type : 'hidden', name : 'pageNum', value : pageNum})
			$('#pageForm').append(inputTag)
			if ('${param.group_name}' != '' || $('#maker_name').val().trim() != '' || $('#desc_kor').val().trim() != '') {
				let group_name = $("<input/>", {type : 'hidden', name : 'group_name', value : "${param.group_name}"})
				let maker_name = $("<input/>", {type : 'hidden', name : 'maker_name', value : "${param.maker_name}"})
				let desc_kor = $("<input/>", {type : 'hidden', name : 'desc_kor', value : "${param.desc_kor}"})
				$('#pageForm').append(group_name);
				$('#pageForm').append(maker_name);
				$('#pageForm').append(desc_kor);
			}
			$('#pageForm').submit();
		})
		
		let replyModal = $('#replyModal');
		$("#prodForm a").on("click", function(e) {
			e.preventDefault(); // 기본동작(href) 동작 금지
			let food_cd = $(this).attr('href');
			$.ajax({
				url: `${contextPath}/Prod/getProd`,
				type: 'get',
				data: {'food_cd' : food_cd },
				success: function(data){
					let prod = '';
					prod += '<table class="table table-bordered">';
						prod += '<thead>';
							prod += '<tr>';
								prod += '<th colspan="4">' + data.desc_kor + ' (1회제공량당)</th>';
							prod += '</tr>';
							prod += '<tr>';
								prod += '<td>열량(kcal)</td><td class= "text-right">' + data.nutr_cont1 + '</td>';
								prod += '<td>탄수화물(g)</td><td class= "text-right">' + data.nutr_cont2 + '</td>';
							prod += '</tr>';
							prod += '<tr>';
								prod += '<td>단백질(g)</td><td class= "text-right">' + data.nutr_cont3 + '</td>';
								prod += '<td>지방(g)</td><td class= "text-right">' + data.nutr_cont4 + '</td>';
							prod += '</tr>';
							prod += '<tr>';
								prod += '<td>당류(g)</td><td class= "text-right">' + data.nutr_cont5 + '</td>';
								prod += '<td>나트륨(mg)</td><td class= "text-right">' + data.nutr_cont6 + '</td>';
							prod += '</tr>';
							prod += '<tr>';
								prod += '<td>콜레스테롤(mg)</td><td class= "text-right">' + data.nutr_cont7 + '</td>';
								prod += '<td>포화지방산(g)</td><td class= "text-right">' + data.nutr_cont8 + '</td>';
							prod += '</tr>';
							prod += '<tr>';
								prod += '<td>트랜스지방(g)</td><td class= "text-right">' + data.nutr_cont9 + '</td>';
							prod += '</tr>';
							prod += '<tr>';
								prod += '<th colspan="4">원재료</th>';
							prod += '</tr>';
							prod += '<tr>';
								prod += '<td colspan="4">' + data.ingredient + '</td>';
							prod += '</tr>';
						prod += '</thead>';
					prod += '</table>';
					$('.body').html(prod);
					replyModal.modal("show");
				},
				error: function(){
					
				}
			})
		})
	})
</script>
<%@include file="../layout/footer.jspf"%>