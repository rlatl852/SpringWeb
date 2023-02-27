<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../layout/header.jspf"%>
<div class="container">
	<div class="jumbotron text-center">
		<h1 class="display-4">게시판</h1>
	</div>
	<sec:authorize access="isAuthenticated()">
		<form id="writeForm">
			<a class="btn btn-primary" href="write">글쓰기</a>
		</form>
	</sec:authorize>
	<form class="form-inline my-3" id="searchForm">
		<select class="form-control mr-sm-2" name="type" id="type">
			<option value="">검색종류선택</option>
			<option value="T" ${param.type == 'T' ? 'selected' : '' }>제목</option>
			<option value="C" ${param.type == 'C' ? 'selected' : '' }>내용</option>
			<option value="W" ${param.type == 'W' ? 'selected' : '' }>작성자</option>
			<option value="TC" ${param.type == 'TC' ? 'selected' : '' }>제목+내용</option>
			<option value="CW" ${param.type == 'CW' ? 'selected' : '' }>내용+작성자</option>
			<option value="TW" ${param.type == 'TW' ? 'selected' : '' }>제목+작성자</option>
		</select>
		<input class="form-control mr-sm-2" id="keyword" name="keyword" type="search" value="${param.keyword }" placeholder="Search">
		<button class="btn btn-success" type="submit">검색</button>
	</form>
	<form id="boardForm">
		<table class="table table-hover">
			<tr>
				<td>번호</td>
				<td>제목</td>
				<td>작성자</td>
				<td>등록일</td>
				<td>조회수</td>
			</tr>
			<c:forEach items="${list }" var="b" varStatus="i">
				<tr>
					<td>${((pageMarker.totalCount - i.index - 1) -  ((pageMarker.viewPage-1)*pageMarker.displayPageNum))+1}</td>
					<td><a href="${b.bno }">${b.title }[${b.replyCnt }]</a></td>
					<td>${b.writer }</td>
					<td><fmt:formatDate value="${b.regdate}" pattern="yyyy-MM-dd" /></td>
					<td>${b.readCount }</td>
				</tr>
			</c:forEach>
		</table>
	</form>
	<div>
		<form id="pageForm">
			<div class="pagination justify-content-center">
				<c:if test="${pageMarker.prev }">
					<a href="1" class="page-link">[처음]</a>
					<a href="${pageMarker.startPage-1 }" class="page-link"><<</a>
				</c:if>
				<c:forEach begin="${pageMarker.startPage }"
					end="${pageMarker.endPage }" var="page">
					<a href="${page }" class="page-link">${page }</a>
				</c:forEach>
				<c:if test="${pageMarker.next }">
					<a href="${pageMarker.endPage+1 }" class="page-link">>></a>
					<a href="${pageMarker.realEndPage }" class="page-link">[마지막]</a>
				</c:if>
			</div>
		</form>
	</div>
</div>
<script>
	$(function(){
		//게시물 이동
		$("#boardForm a").on('click', function(e) { // 페이지 폼의 a태그를 클릭하면 익명함수를 실행
			e.preventDefault();
			let bno = $(this).attr('href');
			
			let bnoTag = $("<input/>", {type : 'hidden', name : 'bno', value : bno})
			let pageNum = $("<input/>", {type : 'hidden', name : 'pageNum', value : "${param.pageNum}"})
			if($('#type').val().trim() != '' || $('#keyword').val().trim() != ''){
				let typeTag = $("<input/>", {type : 'hidden', name : 'type', value : "${param.type}"})
				let keyword = $("<input/>", {type : 'hidden', name : 'keyword', value : "${param.keyword}"})
				$('#boardForm').append(typeTag);
				$('#boardForm').append(keyword);
			}
			$('#boardForm').append(bnoTag);
			$('#boardForm').append(pageNum);
			$('#boardForm').attr("action", "content");
			$('#boardForm').submit();
		});
		
		//페이지 이동
		$("#pageForm a").on("click", function(e) {
			e.preventDefault(); // 기본동작(href) 동작 금지
			let pageNum = $(this).attr('href');
			let inputTag = $("<input/>", {type : 'hidden', name : 'pageNum', value : pageNum})
			$('#pageForm').append(inputTag)
			if($('#type').val().trim() != '' || $('#keyword').val().trim() != ''){
				let typeTag = $("<input/>", {type : 'hidden', name : 'type', value : "${param.type}"})
				let keyword = $("<input/>", {type : 'hidden', name : 'keyword', value : "${param.keyword}"})
				$('#pageForm').append(typeTag);
				$('#pageForm').append(keyword);
			}
			$('#pageForm').submit();
		})
		
		//글쓰기 페이지 이동
		$('#writeForm a').on('click', function(e){
			e.preventDefault(); // 기본동작(href) 동작 금지
			$('#boardForm').attr("action", "write");
			$('#boardForm').submit();
		})
		
		//검색 유효성 검사
		$("#searchForm button").on('click', function(e){
			e.preventDefault();
			if($('#type').val().trim()==''){
				alert('검색종류를 선택하세요')
				return;
			}
			if($('#keyword').val().trim()==''){
				alert('검색어를 입력하세요')
				return;
			}
			$('#searchForm').submit();
		});
	})
</script>
<%@include file="../layout/footer.jspf"%>