<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../layout/header.jspf"%>

<div class="container">
	<div class="jumbotron text-center">
		<h1 class="display-4">회원 가입</h1>
	</div>
	<form action="${contextPath }/member/addMember" method="post" id='addMForm'>
		<div class="form-group col-md-4">
			<label for="userId">아이디</label>
			<input type="text" class="form-control" name="userId" id="userId" size="12" maxlength="12" placeholder="아이디">
			<div id="check_id"></div>
		</div>
		<div class="form-group col-md-4">
			<label for="userpw">비밀번호</label>
			<input type="password" class="form-control" name="userpw" id="userpw" maxlength="12" placeholder="비밀번호">
		</div>
		<div class="form-group col-md-4">
			<label for="userName">이름</label>
			<input type="text" class="form-control" name="userName" id="userName" maxlength="10" placeholder="이름">
		</div>
		<div class="form-group col-md-4">
			<label for="phoneNumber">전화번호</label>
			<input type="tel" class="form-control" name="phoneNumber" id="phoneNumber" maxlength="13" placeholder="전화번호">
		</div>
		<div class="form-group col-md-6">
			<label for="email">메일주소</label>
			<input type="email" class="form-control" name="email" id="email" placeholder="메일주소">
		</div>
		<input type="button" class="btn btn-outline-secondary" value="확인" id="addM">
		<a href="${contextPath }/" class="btn btn-outline-secondary">취소</a>
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" />
	</form>
</div>
<script>
$(function() {
	$('#userId').on('focusout', function(){
		let userId = $(this).val();
		$.ajax({
			url: `${contextPath}/member/getIdCheck`,
			type: 'get',
			data: {'userId' : userId },
			success: function(data){
				if(data == 'Y'){
					$('#check_id span').remove();
				} else if(data == 'N'){
					$('#check_id').html('<span style="color : red;">사용 불가능한 ID입니다.</span>');
				}
			},
			error: function(){
				alert("error");
			}
		})
	})
	$('#addM').on('click', function(e){
		e.preventDefault(); // 기본동작(href) 동작 금지
		if($('#userId').val() == '' || $('#userId').val() <= 0){
			alert('아이디를 입력하세요');
			$('#userId').focus();
			return;
		} else if($('#userpw').val() == '' || $('#userpw').val() <= 0){
			alert('비밀번호를 입력하세요');
			$('#userpw').focus();
			return;
		} else if($('#userName').val() == '' || $('#userName').val() <= 0){
			alert('이름을 입력하세요');
			$('#userName').focus();
			return;
		} else if($('#phoneNumber').val() == '' || $('#phoneNumber').val() <= 0){
			alert('전화번호 입력하세요');
			$('#phoneNumber').focus();
			return;
		} else if($('#email').val() == '' || $('#email').val() <= 0){
			alert('이메일을 입력하세요');
			$('#email').focus();
			return;
		}
		$("#addMForm").submit();
	})
	
})
</script>
<%@include file="../layout/footer.jspf"%>