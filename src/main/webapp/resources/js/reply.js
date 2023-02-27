var header = $("meta[name='_csrf_header']").attr('content');	//ajax를 사용하면 spring security로 인해서 403에러가 나올때 csrf token을 사용하기위해
var token = $("meta[name='_csrf']").attr('content');			//ajax를 사용하면 spring security로 인해서 403에러가 나올때 csrf token을 사용하기위해

var replyService = (function() {
	function add(reply, callback, error) {
		$.ajax({
			type: 'post',
			url: `${contextPath}/Replies/new`,
			data: JSON.stringify(reply),
			contentType: "application/json; charset=utf-8",
			beforeSend: function(xhr) {	//ajax를 사용하면 spring security로 인해서 403에러가 나올때 csrf token을 사용하기위해
				xhr.setRequestHeader(header, token);
			},
			success: function(result, status, xhr) {
				if (callback) {
					callback(result)
				}
			},
			error: function(xhr, status, er) {
				if (error) {
					error(er)
				}
			}
		})
	}//	add end

	function getList(bno, callback, error) {
		let url = `${contextPath}/Replies/get/${bno}`;
		let success = function(result) {
			if (callback) { callback(result) }
		};
		$.getJSON(url, success).fail(function(xhr, status, er) {
			if (error) { error(er) }
		});
	}//	getList end

	function remove(reply, callback) {
		$.ajax({
			type: 'delete',
			url: `${contextPath}/Replies/${reply.rno}`,
			data: JSON.stringify(reply),
			contentType: "application/json;charset=utf-8",
			beforeSend: function(xhr) {	//ajax를 사용하면 spring security로 인해서 403에러가 나올때 csrf token을 사용하기위해
				xhr.setRequestHeader(header, token);
			},
			success: function(result, status, xhr) {
				if (callback) callback(result);
			},
			error: function(xhr, status, error) {
				console.log(error);
			}
		});
	}

	function update(reply, callback) {
		$.ajax({
			type: 'put',
			url: `${contextPath}/Replies/${reply.rno}`,
			data: JSON.stringify(reply),
			contentType: "application/json;charset=utf-8",
			beforeSend: function(xhr) {	//ajax를 사용하면 spring security로 인해서 403에러가 나올때 csrf token을 사용하기위해
				xhr.setRequestHeader(header, token);
			},
			success: function(result, status, xhr) {
				if (callback) callback(result)
			},
			error: function(xhr, status, er) {
				console.log(er);
			}
		})
	}

	//조회
	function get(rno, callback, error) {
		$.get(`${contextPath}/Replies/${rno}.json`, function(vo) {
			if (callback) callback(vo)
		}).fail(function(xhr, status, err) {
			if (error) error()
		})
	}

	return {
		add: add,
		getList: getList,
		remove: remove,
		update: update,
		get: get
	};
})();