<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<!-- include summernote css/js -->
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>
<script>
$(document).ready(function() {

  $('#summernote').summernote({maximumImageFileSize : 1024*1024*10});
})
</script>

</head>
<body>


	<link
		href="https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha1/css/bootstrap.min.css"
		rel="stylesheet">
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha1/js/bootstrap.min.js"></script>

	<div class="container">

		<c:import url="./../header.jsp"></c:import>

		<h2>게시판 글 작성</h2>
		<!-- multipart/form-data : 데이터를 여러 조각으로 나누어서 전송하는 방식, 수신되는 곳에서 재조립이 필요하다 -->
		<form action="saveBoard" method="post">
			<div class="mb-3">
				<label for="title" class="form-label">글제목</label> <input type="text"
					class="form-control" id="title" name="title"
					placeholder="글제목을 입력하세요">
			</div>
			<div class="mb-3">
				<label for="author" class="form-label">작성자</label> <input
					type="text" class="form-control" id="author" name="writer"
					value='${sessionScope.loginMember.userId}' readonly>
			</div>
			<div>
				<textarea id="summernote" name="content"></textarea>
			</div>


			<button type="submit" class="btn btn-primary" >저장</button>
			<button type="button" class="btn btn-warning" >취소</button>
			<!-- reset타입은 페이지를 재로딩할 뿐 서버의 데이터를 지우지 않기 때문에 사용X -->
		</form>

		<c:import url="./../footer.jsp"></c:import>

	</div>


</body>
</html>