<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file=".././layout/header.jspf"%>
<div class="container">
	<div class="jumbotron text-center">
		<h1 class="display-4">게시글 수정</h1>
	</div>
	<form action="modify" method="post" id="modifyForm" name="modifyForm">
		<div class="form-group">
			<label>제목</label>
			<input type="text" class="form-control" name="title" value="${param.title }">
		</div>
		<div class="form-group">
			<label>내용</label>
			<textarea class="form-control" name="content" rows="8" style="resize: none">${param.content }</textarea>
		</div>
		
		<div class="form-group">
			<div class="card">
				<div class="card-header">첨부파일</div>
				<div class="card-body">
					<div class="uploadDiv">
						<input type="file" name="uploadFile" multiple>
					</div>
					<div class="uploadResult">
						<ul style="list-style: none; margin: 0; padding: 0;">
						</ul>
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" name="writer" value='<sec:authentication property="principal.memberVO.userName"/>'>
		<button class="btn btn-primary modify">확인</button>
		<a href="" class="btn btn-primary">취소</a>
		
		
	</form>
</div>
<script>
	$(function() {
		let bno = '<c:out value="${board.bno }"/>'
		console.log(bno);
		$.getJSON(`${contextPath}/Board/getAttachList`, {bno : bno}, function(arr){
			let uR = "";
			$(arr).each(function(i, attach){
				if(attach.fileType){
					var fileCellPath = encodeURIComponent(attach.uploadPath + "/s_" + attach.uuid + "_" + attach.fileName);
					uR += "<li data-path='" + attach.uploadPath + "' data-uuid='" + attach.uuid + "' data-filename='" + attach.fileName + "' data-type='" + attach.fileType + "'>";
						uR += "<div>";
							uR += "<img src='/SpringWeb/display?fileName=" + fileCellPath + "' style='width:30px; height:30px;'>" + attach.fileName;
							uR += "<button type='button' data-file='" + fileCellPath + "' data-type='file' class='btn btn-waring btn-circle'>&times;</button>";
						uR += "</div>";
					uR += "</li>";
				} else {
					uR += "<li data-path='" + attach.uploadPath + "' data-uuid='" + attach.uuid + "' data-filename='" + attach.fileName + "' data-type='" + attach.fileType + "'>";
						uR += "<div>";
							uR += "<img src='/SpringWeb/resources/img/file.png' style='width:30px; height:30px;'>" + attach.fileName;
							uR += "<button type='button' data-file='" + fileCellPath + "' data-type='file' class='btn btn-waring btn-circle'>&times;</button>";
						uR += "</div>";
					uR += "</li>";
				}
				$('.uploadResult ul').html(uR);
			});
		});
		
		$(".uploadResult").on("click", "button", function(){
			if(confirm("파일을 삭제하시겠습니까?")){
				var targetLi = $(this).closest("li");
				
				targetLi.remove();
			}
		});
		
		function showUploadResult(uploadResultArr){
			if(!uploadResultArr || uploadResultArr.length == 0){return;}
			var uploadResult = $(".uploadResult ul");
			var uR = "";
			$(uploadResultArr).each(function(i, obj){
				// image type
				if(obj.fileType){
					var fileCellPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);
					uR += "<li data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"'";
					uR += " data-filename='"+obj.fileName+"' data-type='"+obj.fileType+"'><div>";
					uR += "<img src='/SpringWeb/display?fileName=" + fileCellPath + "' style='width:30px; height:30px;'>";
					uR += "<span>" + obj.fileName + "</span>";
					uR += "<button type='button' data-file='" + fileCellPath + "' data-type='image' class='btn btn-waring btn-circle'>";
					uR += "<i class='fa fa-time'></i>&times</button><br></div></li>";
				} else {
					var fileCellPath = encodeURIComponent(obj.uploadPath + "/" + obj.uuid + "_" + obj.fileName);
					var fileLink = fileCellPath.replace(new RegExp(/\\/g), "/");
					uR += "<li data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"'";
					uR += " data-filename='"+obj.fileName+"' data-type='"+obj.fileType+"'><div>"
					uR += "<img src='/SpringWeb/resources/img/file.png' style='width:30px; height:30px;'>";
					uR += "<span>" + obj.fileName + "</span>";
					uR += "<button type='button' data-file='" + fileCellPath + "' data-type='file' class='btn btn-waring btn-circle'>";
					uR += "&times;</button></div></li>";
				}
			});
			uploadResult.append(uR);
		}
		
		$("input[type='file']").change(function(e){
			var formData = new FormData();
			var inputFile = $("input[name='uploadFile']");
			var files = inputFile[0].files;
			for(var i=0; i<files.length; i++){
				if(!checkExtension(files[i].name, files[i].size)){
					return false;
				}
				formData.append("uploadFile", files[i]);
			}
			$.ajax({
				url: "/SpringWeb/uploadAjaxAction",
				processData: false,
				contentType: false,
				data: formData,
				type: "post",
				dataType: "json",
				beforeSend: function(xhr) {	//ajax를 사용하면 spring security로 인해서 403에러가 나올때 csrf token을 사용하기위해
					var header = $("meta[name='_csrf_header']").attr('content');	//ajax를 사용하면 spring security로 인해서 403에러가 나올때 csrf token을 사용하기위해
					var token = $("meta[name='_csrf']").attr('content');			//ajax를 사용하면 spring security로 인해서 403에러가 나올때 csrf token을 사용하기위해
					xhr.setRequestHeader(header, token);
				},
				success: function(result){
					showUploadResult(result);
				}
			});
		});
		
		// 파일체크
		var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");	// 파일 종류 체크
		var maxSize = 5242880	// 5MB
		function checkExtension(fileName, fileSize){
			if(regex.test(fileName)){
				alert("해당 종류의 파일은 업로드 할 수 없습니다.");
				return false;
			}
			if(fileSize >= maxSize){
				alert("파일 크기가 초과되었습니다.");
				return false;
			}
			return true;
		}
		
		$("#modifyForm a").on("click", function(e) {
			e.preventDefault(); // 기본동작 금지
			let bnoTag = $("<input/>", {type : 'hidden', name : 'bno', value : "${param.bno}"})
			let pageNum = $("<input/>", {type : 'hidden', name : 'pageNum', value : "${param.pageNum}"})
			let typeTag = $("<input/>", {type : 'hidden', name : 'type', value : "${param.type}"})
			let keyword = $("<input/>", {type : 'hidden', name : 'keyword', value : "${param.keyword}"})
			if (pageNum.val() != '') {$('#modifyForm').append(pageNum);}
			if (bnoTag.val() != '') {$('#modifyForm').append(bnoTag);}
			if (keyword.val() != '') {$('#modifyForm').append(keyword);}
			if (typeTag.val() != '') {$('#modifyForm').append(typeTag);}
			$('#modifyForm').attr("method", "get"); // 요청주소 설정
			$('#modifyForm').attr("action", "content"); // 요청주소 설정
			$('#modifyForm').submit();
		});
		
		$(document).ready(function(){
			$('.modify').on('click', function(e) {
				//remove버튼을 click했을 때 실행될 코드
				e.preventDefault(); // 기본동작 금지
				if(document.modifyForm.title.value == ""){
					alert("제목을 입력하세요");
					document.modifyForm.title.focus();
					return;
				}
				if(document.modifyForm.content.value == ""){
					alert("내용을 입력하세요");
					document.modifyForm.content.focus();
					return;
				}
				let csrfToken = $("<input/>", {type : 'hidden', name : '${_csrf.parameterName }', value : "${_csrf.token }"})
				let bnoTag = $("<input/>", {type : 'hidden', name : 'bno', value : "${param.bno}"})
				if (bnoTag.val() != '') {$('#modifyForm').append(bnoTag);}
				if (csrfToken.val() != '') {$('#modifyForm').append(csrfToken);}
				var str = "";
				$(".uploadResult ul li").each(function(i, obj){
					var jobj = $(obj);
					console.log(obj);
					str += "<input type='hidden' name='attachList[" + i + "].fileName' value='" + jobj.data("filename") + "'>";
					str += "<input type='hidden' name='attachList[" + i + "].uuid' value='" + jobj.data("uuid") + "'>";
					str += "<input type='hidden' name='attachList[" + i + "].uploadPath' value='" + jobj.data("path") + "'>";
					str += "<input type='hidden' name='attachList[" + i + "].fileType' value='" + jobj.data("type") + "'>";
				})
				$('#modifyForm').append(str).submit();
			});
		})
	})
</script>
<%@include file=".././layout/footer.jspf"%>