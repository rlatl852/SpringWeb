<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../layout/header.jspf"%>

<div class="container">
	<div class="jumbotron text-center">
		<h1 class="display-4">로그인</h1>
	</div>
	<form method="post" action="<c:url value="/SpringWeb/login"/>" name="loginForm">
		<div class="form-group">
			<label>아이디</label>
			<input type="text" class="form-control" id="id" name="username" placeholder="ID" required autofocus>
		</div>
		<div class="form-group">
			<label>비밀번호</label>
			<input type="password" class="form-control" id="pw" name="password" placeholder="PASSWORD" required>
		</div>
		<div class="form-group">
			<input type="button" class="btn btn-outline-secondary" value="로그인" onclick="loginCheck();" />
			<a href="${contextPath }/" class="btn btn-outline-secondary">취소</a>
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" />
		</div>
	</form>
	<c:if test="${exception ne null }">
		<div class="alert alert-danger">
			${exception}
		</div>
	</c:if>
</div>

<script>
function loginCheck(){
	if(document.loginForm.username.value == ""){
		alert("아이디를 입력하세요");
		document.loginForm.username.focus();
		return;
	}
	if(document.loginForm.password.value == ""){
		alert("비밀번호를 입력하세요");
		document.loginForm.password.focus();
		return;
	}
	document.loginForm.submit();
}
</script>
<%@include file="../layout/footer.jspf"%>