<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file=".././layout/header.jspf"%>
<div class="container">
	<div class="jumbotron text-center">
		<h1 class="display-4">게시물 조회</h1>
	</div>
	<form id="contentForm">
		<input type="hidden" value="${board.bno }" name="bno" id="bno">
		<table class="table">
			<thead>
				<tr>
					<td>번호 : ${board.bno }</td>
					<td colspan='2'>제목 : ${board.title }</td>
				</tr>
				<tr>
					<td>등록일 : <fmt:formatDate value="${board.regdate}"
							pattern="yyyy-MM-dd" /></td>
					<td>작성자 : ${board.writer }</td>
					<td>조회수 : ${board.readCount }</td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td colspan='3'>
						<div class="uploadImgResult">
							<ul>
								
							</ul>
						</div>
						<div>${board.content }</div>
					</td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td colspan='3'>
						<div class="card">
							<div class="card-header">첨부파일</div>
							<div class="card-body">
								<div class="uploadResult">
									<ul style="list-style: none;">
									</ul>
								</div>
							</div>
						</div>
					</td>
				</tr>
			</tfoot>
		</table>
		<div class="card-footer">
			<button class="btn btn-primary list">목록</button>
			<c:set var="admin" value="관리자" />
			<sec:authorize access="isAuthenticated()">
				<c:set var="writer" value="${board.writer }" />
				<sec:authentication property="principal.memberVO.userName" var="username"/>
				<c:choose>
					<c:when test="${writer eq username}">
						<input type="hidden" value="${writer }" name="writer">
						<button class="btn btn-warning modify">수정</button>
						<button class="btn btn-danger remove">삭제</button>
					</c:when>
					<c:when test="${username eq admin}">
						<input type="hidden" value="${writer }" name="writer">
						<button class="btn btn-danger remove">삭제</button>
					</c:when>
				</c:choose>
			</sec:authorize>
		</div>
	</form>
	<!-- content and -->
	
	<!-- reply start -->
	<sec:authorize access="isAuthenticated()">
		<div class="container p-3 my-3 border">
			<form class="form-group" id="replyForm">
				<label for="comment">댓글 쓰기</label>
				<input type="hidden" name="replyer" value='<sec:authentication property="principal.memberVO.userName"/>'>
				<textarea class="form-control" style="resize: none" rows="5" id="comment" name="reply"></textarea>
				<button type="button" class="btn btn-warning" id="replyReg">등록</button>
			</form>
		</div>
	</sec:authorize>
	<div class="reply">
		<ul class="list-group">
		</ul>
	</div>
	<!-- reply and -->
	
	<!-- The Modal -->
	<div class="modal" id="replyModal" data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				
				<!-- Modal Header -->
				<div class="modal-header">
					<h4 class="modal-title">댓글 등록</h4>
					<button type="button" class="close" data-dismiss="modal">&times;</button>
				</div>
				
				<!-- Modal body -->
				<div class="modal-body">
					<input type="text" class="form-control" name="replyer" readOnly="readonly">
					<div class="form-group">
						댓글 내용<input type="text" class="form-control" name="reply">
					</div>
					<div class="form-group">
						등록일<input type="text" class="form-control" name="regDate" readOnly="readonly">
					</div>
				</div>

				<!-- Modal footer -->
				<div class="modal-footer">
					<button type="button" class="btn btn-warning" id="modalModBtn">수정</button>
					<button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
				</div>
			
			</div>
		</div>
	</div>
</div>
<%-- Uncaught SyntaxError: Unexpected token '<' (at reply.js:7:1) > 에러가 크롬 개발자 도구 콘솔에 출력되었을 때 HomeController의 GetMapping를 GetMapping("/")로 변경함 --%>
<script>
	var user = null;
	var auth = null;
	<sec:authorize access="isAuthenticated()">
		user = `${username}`;
		auth = `${admin}`;
	</sec:authorize>
	$(function() {
		var bno = '<c:out value="${board.bno }"/>'
		$.getJSON(`${contextPath}/Board/getAttachList`, {bno : bno}, function(arr){
			let uIR = "";
			let uR = "";
			$(arr).each(function(i, attach){
				if(attach.fileType){
					var fileCellPath = encodeURIComponent(attach.uploadPath + "/s_" + attach.uuid + "_" + attach.fileName);
					uIR += "<img src='/SpringWeb/display?fileName=" + fileCellPath + "' style='height:250px;'>";
					$('.uploadImgResult ul').html(uIR);
				}
			});
			$(arr).each(function(i, attach){
				if(attach.fileType){
					var fileCellPath = encodeURIComponent(attach.uploadPath + "/s_" + attach.uuid + "_" + attach.fileName);
					uR += "<li>";
						uR += "<div>";
							uR += "<a href='#' data-path='" + attach.uploadPath + "' data-uuid='" + attach.uuid + "' data-filename='" + attach.fileName + "' data-type='" + attach.fileType + "'>"
								uR += "<img src='/SpringWeb/display?fileName=" + fileCellPath + "' style='width:30px; height:30px;'>" + attach.fileName;
							uR += "</a>";
						uR += "</div>";
					uR += "</li>";
				} else {
					uR += "<li>";
						uR += "<div>";
							uR += "<a href='#' data-path='" + attach.uploadPath + "' data-uuid='" + attach.uuid + "' data-filename='" + attach.fileName + "' data-type='" + attach.fileType + "'>"
								uR += "<img src='/SpringWeb/resources/img/file.png' style='width:30px; height:30px;'>" + attach.fileName;
							uR += "</a>";
						uR += "</div>";
					uR += "</li>";
				}
				$('.uploadResult ul').html(uR);
			});
		});
		$(document).on('click', '.uploadResult a', function(e){
			e.preventDefault(); // 기본동작 금지
			console.log('view image');
			let aObj = $(this);
			let path = encodeURIComponent(aObj.data("path") + "/" + aObj.data("uuid") + "_" + aObj.data("filename"));
			self.location = `${contextPath}/download?fileName=` + path
		});
		
		let contentForm = $('#contentForm');
		//list버튼을 click했을 때 실행될 코드
		$('.list').on('click', function(e) {
			e.preventDefault(); // 기본동작 금지
			let pageNum = $("<input/>", {type : 'hidden', name : 'pageNum', value : "${param.pageNum}"})
			let typeTag = $("<input/>", {type : 'hidden', name : 'type', value : "${param.type}"})
			let keyword = $("<input/>", {type : 'hidden', name : 'keyword', value : "${param.keyword}"})
			contentForm.find('input[name="bno"]').remove(); // 파라미터 비우기
			if (pageNum.val() != '') {contentForm.append(pageNum);}
			if (typeTag.val() != '') {contentForm.append(typeTag);}
			if (keyword.val() != '') {contentForm.append(keyword);}
			contentForm.attr("method", "get"); // 메소드 방식 설정
			contentForm.attr("action", "list"); // 요청주소 설정
			contentForm.submit();
		});

		//modify버튼을 click했을 때 실행될 코드
		$('.modify').on('click', function(e) {
			e.preventDefault(); // 기본동작 금지
			let pageNum = $("<input/>", {type : 'hidden', name : 'pageNum', value : "${param.pageNum}"})
			let title = $("<input/>", {type : 'hidden', name : 'title', value : "${board.title }"})
			let content = $("<input/>", {type : 'hidden', name : 'content', value : "${board.content }"})
			let typeTag = $("<input/>", {type : 'hidden', name : 'type', value : "${param.type}"})
			let keyword = $("<input/>", {type : 'hidden', name : 'keyword', value : "${param.keyword}"})
			if (typeTag.val() != '') {contentForm.append(typeTag);}
			if (keyword.val() != '') {contentForm.append(keyword);}
			if (pageNum.val() != '') {contentForm.append(pageNum);}
			if (title.val() != '') {contentForm.append(title);}
			if (content.val() != '') {contentForm.append(content);}
			contentForm.attr("method", "get"); // 메소드 박식 설정
			contentForm.attr("action", "modify"); // 요청주소 설정
			contentForm.submit();
		});

		//remove버튼을 click했을 때 실행될 코드
		$('.remove').on('click', function(e) {
			e.preventDefault(); // 기본동작 금지
			let csrfToken = $("<input/>", {type : 'hidden', name : '${_csrf.parameterName }', value : "${_csrf.token }"})
			let writer = $("<input/>", {type : 'hidden', name : 'writer', value : "${_csrf.token }"})
			if (csrfToken.val() != '') {contentForm.append(csrfToken);}
			contentForm.attr("method", "post"); // 메소드 박식 설정
			contentForm.attr("action", "remove"); // 요청주소 설정
			contentForm.submit();
		});
	})
</script>
<script type="text/javascript" src="${contextPath }/resources/js/reply.js"></script>
<script type="text/javascript" src="${contextPath }/resources/js/get.js" charset="UTF-8"></script>
<%@include file=".././layout/footer.jspf"%>