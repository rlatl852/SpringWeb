<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../layout/header.jspf"%>
<div class="container">
	<div class="jumbotron text-center">
		<h1 class="display-4">글쓰기</h1>
	</div>
	<form action="write" method="post" id="writeForm">
		<div class="form-group">
			<label>제목</label>
			<input type="text" class="form-control" placeholder="제목" name="title">
		</div>
		<div class="form-group">
			<label>내용</label>
			<textarea class="form-control" placeholder="내용" name="content" rows="8" style="resize: none"></textarea>
		</div>
		<div class="uploadDiv">
			<input type="file" name="uploadFile" multiple>
		</div>
		<div class="card">
			<div class="card-header">첨부파일</div>
			<div class="card-body">
				<div class="uploadResult">
					<ul style="list-style: none;">
					</ul>
				</div>
			</div>
		</div>
		<input type="hidden" name="writer" value='<sec:authentication property="principal.memberVO.userName"/>'>
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" />
		<button type="button" class="btn btn-primary">확인</button>
		<a href="list" class="btn btn-primary">취소</a>
	</form>
</div>
<script>
	var header = $("meta[name='_csrf_header']").attr('content');	//ajax를 사용하면 spring security로 인해서 403에러가 나올때 csrf token을 사용하기위해
	var token = $("meta[name='_csrf']").attr('content');			//ajax를 사용하면 spring security로 인해서 403에러가 나올때 csrf token을 사용하기위해
	$(document).ready(function() {
		var cloneObj = $(".uploadDiv").clone();
		$("button[type='button']").on("click", function(e) {
			if($("input[name='title']").val().trim() == ''){
				alert("제목을 입력하세요");
				$("#title").focus();
				return false;
			}
			if($("textarea[name='content']").val().trim() == ''){
				alert("내용을 입력하세요");
				$("#content").focus();
				return false;
			}
			var str = "";
			$(".uploadResult ul li").each(function(i, obj){
				var jobj = $(obj);
				str += "<input type='hidden' name='attachList[" + i + "].fileName' ";
				str += "value='"+jobj.data("filename")+"'>";
				str += "<input type='hidden' name='attachList[" + i + "].uuid'";
				str += "value='"+jobj.data("uuid")+"'>";
				str += "<input type='hidden' name='attachList[" + i + "].uploadPath' ";
				str += "value='"+jobj.data("path")+"'>";
				str += "<input type='hidden' name='attachList[" + i + "].fileType' ";
				str += "value='"+jobj.data("type")+"'>";
			});
			$('#writeForm').append(str).submit();
		});
		
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
		
		function showUploadResult(uploadResultArr){
			if(!uploadResultArr || uploadResultArr.length == 0){return;}
			var uploadResult = $(".uploadResult ul");
			var str = "";
			$(uploadResultArr).each(function(i, obj){
				// image type
				if(obj.fileType){
					var fileCellPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);
					str += "<li data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"'";
					str += " data-filename='"+obj.fileName+"' data-type='"+obj.fileType+"'><div>";
					str += "<img src='/SpringWeb/display?fileName=" + fileCellPath + "' style='width:30px; height:30px;'>";
					str += "<span>" + obj.fileName + "</span>";
					str += "<button type='button' data-file='" + fileCellPath + "' data-type='image' class='btn btn-waring btn-circle'>";
					str += "<i class='fa fa-time'></i>&times</button><br></div></li>";
				} else {
					var fileCellPath = encodeURIComponent(obj.uploadPath + "/" + obj.uuid + "_" + obj.fileName);
					var fileLink = fileCellPath.replace(new RegExp(/\\/g), "/");
					str += "<li data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"'";
					str += " data-filename='"+obj.fileName+"' data-type='"+obj.fileType+"'><div>"
					str += "<img src='/SpringWeb/resources/img/file.png' style='width:30px; height:30px;'>";
					str += "<span>" + obj.fileName + "</span>";
					str += "<button type='button' data-file='" + fileCellPath + "' data-type='file' class='btn btn-waring btn-circle'>";
					str += "&times;</button></div></li>";
				}
			});
			uploadResult.append(str);
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
					xhr.setRequestHeader(header, token);
				},
				success: function(result){
					$(".uploadDiv").html(cloneObj);
					showUploadResult(result);
				}
			});
		});
		
		$(".uploadResult").on("click", "button", function(e){
			var targetFile = $(this).data("file");
			var type = $(this).data("type");
			var targetLi = $(this).closest("li")
			$.ajax({
				url: "/SpringWeb/deleteFile",
				data: {fileName: targetFile, type: type},
				dataType: "text",
				type: "post",
				beforeSend: function(xhr) {	//ajax를 사용하면 spring security로 인해서 403에러가 나올때 csrf token을 사용하기위해
					xhr.setRequestHeader(header, token);
				},
				success: function(result){
					alert(result);
					targetLi.remove();
				}
			});
		});
	});
</script>
<%@include file="../layout/footer.jspf"%>