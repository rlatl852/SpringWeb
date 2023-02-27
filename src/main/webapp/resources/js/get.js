/*console.log('get.jsp');
console.log(contextPath)
console.log(replyService)*/
$(function() {
	let bno = $('#bno').val();
	var replyUl = $(".list-group");
	function showList() {
		replyService.getList(bno, function(reply) {
			let replyContent = "";
			if (reply == null || reply.length == 0) {
				replyUl.html(replyContent);
				return
			}
			for (let r of reply) {
				replyContent += `<form id="replyList${r.rno}" data-rno="${r.rno}">`;
				replyContent += `<li class="list-group-item">`;
				replyContent += `<p name="replyerName" id="replyer" value="${r.replyer}">${r.replyer}&nbsp(${displayTime(r.insertDate)})</p>`;
				replyContent += `<p >${r.reply}</p>`;
				if (user == r.replyer && user != null) {
					replyContent += `
						<p>
							<button type="button" class="btn btn-warning" id="replyUp${r.rno}}">수정</button>
							<button type="button" class="btn btn-danger" id="replyDel${r.rno}}">삭제</button>
						</p>`;
				} else if (user == auth && user != null) {
					replyContent += `
						<p>
							<button type="button" class="btn btn-danger" id="replyDel${r.rno}}">삭제</button>
						</p>`;
				}

				replyContent += `</li >`;
				replyContent += `</form >`;

			}
			replyUl.html(replyContent);
		})
	}

	// regDate
	function displayTime(time) {
		var date = moment(time).format("YYYY-MM-DD HH:mm:ss");	//moment 라이브러리 사용
		return date
	}
	
	showList();

	let replyForm = document.getElementById('replyForm');
	let replyModal = $('#replyModal')

	let modalInputReply = replyModal.find('input[name="reply"]');
	let modalInputReplyer = replyModal.find('input[name="replyer"]');
	let modalInputRegDate = replyModal.find('input[name="regDate"]');

	//댓글 등록 처리
	$('#replyReg').on('click', function() {
		let reply = {
			replyer: replyForm.replyer.value,
			reply: replyForm.reply.value,
			bno: bno
		}
		replyService.add(reply);
		location.reload();
	})
	
	
	//댓글 삭제 처리
	$('.reply ul').on('click', '[id^=replyDel]', function() {
		let rno = $(this).closest('form').data('rno')
		replyService.get(rno, function(vo) {
			if (!user) {
				alert("로그인이 필요합니다.");
				return
			}
			if (user != vo.replyer && user != '관리자') {
				alert("작성자가 아닙니다.");
				return
			}
			if (user == vo.replyer || user == '관리자') {
				let reply = {
					replyer: vo.replyer,
					rno: rno
				}
				replyService.remove(reply)
			}
		})
		location.reload();
	})

	//댓글 수정 폼
	$('.reply ul').on('click', '[id^=replyUp]', function() {
		let rno = $(this).closest('form').data('rno')

		replyService.get(rno, function(vo) {
			if (!user) {
				alert("로그인이 필요합니다.");
				return
			}
			if (user != vo.replyer) {
				alert("작성자가 아닙니다.");
				return
			}
			modalInputReply.val(vo.reply);
			modalInputReplyer.val(vo.replyer);
			modalInputRegDate.val(displayTime(vo.insertDate));
			modalInputRegDate.closest('div').show();
			replyModal.data('rno', vo.rno);
			replyModal.modal("show");
		})
	})

	//수정 처리
	$('#modalModBtn').on('click', function() {
		let reply = {
			rno: replyModal.data('rno'),
			reply: modalInputReply.val(),
			replyer: modalInputReplyer.val()
		}

		replyService.update(reply, function() {
			if (!user) {
				alert("로그인이 필요합니다.");
				return
			}
			if (user != reply.replyer) {
				alert("작성자가 아닙니다.");
				return
			}
			replyModal.modal('hide');
			location.reload();
		})
	});
})
