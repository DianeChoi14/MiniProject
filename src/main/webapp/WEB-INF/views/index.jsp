<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>

	function outputPopBoards(data) {
		let output = '<table class="table table-hover popBoard">';
		$.each(data, function(i,e){
			output += `<tr onclick="location.href='/hboard/viewBoard?boardNo=\${e.boardNo}';">`;
			output += `<td>\${e.title}</td>`;
			let postDate = new Date(e.postDate).toLocaleDateString();
			output += `<td>\${postDate}</td>`;
			output += '</tr>';
		});
		output += '</table>';
		$('.top5Board').html(output);
	}
	function getTop5Board() {
		$.ajax({
			url : '/get5Board',
			type : 'GET',
			dataType : 'json',
			success : function(data) {
				console.log(data);
				outputPopBoards(data);
			}
		});
	}
	function checkCookie() {
		$.ajax({
			url : '/readCookie',
			type : 'GET',
			dataType : 'json',
			success : function(data) {
				console.log(data);
				if (data.msg == 'fail') {
					$('#myModal').show(200);
				}
			}
		});
	}
	$(function() {
		checkCookie(); // 쿠키를 읽어서 쿠키가 없으면 모달창을 띄운다
		getTop5Board()
		$('.modalCloseBtn').click(function() {
			if ($('#ch_agree').is(':checked')) {
				$.ajax({
					url : '/saveCookie',
					type : 'GET',
					dataType : 'text',
					success : function(data) {
						console.log(data);
					}
				});
			} else {
				alert("쿠키를 저장하지 않습니다!!!")
			}
			//클래스가 modatCloseBtn인 태그를 클릭(실행)하면 실행되는 함수
			$('#myModal').hide(); // 태그를 숨기는 함수
		})
	});
</script>
<style>
.modalFooter {
	display: flex;
	justify-content: space-between;
	padding: 1rem;
}

</style>
</head>
<body>

	<div class="container">
		<jsp:include page="./header.jsp"></jsp:include>


		<div class="content">
			<h1>index.jsp</h1>
			<div class="top5Board"></div>
		</div>

		<jsp:include page="./footer.jsp"></jsp:include>
	</div>


	<!-- The Modal -->
	<div class="modal" id="myModal" style="display: none;">
		<div class="modal-dialog">
			<div class="modal-content">

				<!-- Modal Header -->
				<div class="modal-header">
					<h4 class="modal-title">MiniProject</h4>
					<button type="button" class="btn-close modalCloseBtn"
						data-bs-dismiss="modal"></button>
				</div>

				<!-- Modal body -->
				<div class="modal-body">
					<div>사이트가 개편중입니다...</div>
					<div>빠른 시일 안에 개편을 완료하도록 하겠습니다...</div>
				</div>

				<!-- Modal footer -->
				<div class="modalFooter">
					<div>
						<input class="form-check-input" type="checkbox" id="ch_agree" />
						하루동안 공지 열지 않기
					</div>
					<div>
						<button type="button" class="btn btn-danger modalCloseBtn"
							data-bs-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
