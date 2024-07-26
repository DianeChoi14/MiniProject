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
	$(function(){
		// 웹문서가 로딩되면 모달창을 띄운다
		$('#myModal').show();
		$('.modalCloseBtn').click(function() 
		{
			//클래스가 modatCloseBtn인 태그를 클릭(실행)하면 실행되는 함수
			$('#myModal').hide(); // 태그를 숨기는 함수
		})
	});
</script>
</head>
<body>

	<div class="container">
		<c:import url="./header.jsp"></c:import>


		<div class="content">
			<h1>index.jsp</h1>
		</div>

		<c:import url="./footer.jsp"></c:import>
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
				<div class="modal-footer">
					<span> <input class="form-check-input" type="checkbox" id="ch_agree" />하루동안 공지 열지 않기</span>
					<button type="button" class="btn btn-danger modalCloseBtn"
						data-bs-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
