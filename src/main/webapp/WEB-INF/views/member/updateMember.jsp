<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../layout/header.jspf"%>

<div class="container">
	<div class="jumbotron text-center">
		<h1 class="display-4">회원 정보 수정</h1>
	</div>
	<form action="${contextPath }/member/passwordCheck" method="get">
		<div class="form-group">
			<label>비밀번호 확인</label>
			<input type="password" class="form-control" id="pw" name="userpw" placeholder="PASSWORD" required>
		</div>
		<div class="form-group">
			<input type="submit" class="btn btn-outline-secondary" value="로그인" />
			<a href="${contextPath }/" class="btn btn-outline-secondary">취소</a>
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" />
		</div>
	</form>
</div>

<%@include file="../layout/footer.jspf"%>