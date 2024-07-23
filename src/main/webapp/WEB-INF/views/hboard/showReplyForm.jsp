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
</head>
<body>

	<div class="container">

		<c:import url="./../header.jsp"></c:import>

		<h2>${param.boardNo}번 글의 답글 작성 페이지</h2>
		<!-- multipart/form-data : 데이터를 여러 조각으로 나누어서 전송하는 방식, 수신되는 곳에서 재조립이 필요하다 -->
		<form action="saveReply" method="post">
			<div class="mb-3">
				<label for="title" class="form-label">글제목</label> <input type="text"
					class="form-control" id="title" name="title"
					placeholder="글제목을 입력하세요">
			</div>
			<div class="mb-3">
				<label for="author" class="form-label">작성자</label> <input
					type="text" class="form-control" id="author" name="writer"
					placeholder="작성자를 입력하세요">
			</div>
			<div class="mb-3">
				<label for="content" class="form-label">내용</label>
				<textarea class="form-control" id="content" name="content" rows="5"
					placeholder="내용을 입력하세요"></textarea>
			</div>

			<!-- 			<div class="fileUploadArea mb-3"">
				<p>업로드할 파일을 드래그 드랍하세요!</p>

			</div> <div class="preview"></div>-->
			
			<div><input type="hidden" name="ref" value="${param.ref }"/></div>
			<div><input type="hidden" name="step" value="${param.step }"/></div>
			<div><input type="hidden" name="refOrder" value="${param.refOrder }"/></div>
			

			<button type="submit" class="btn btn-primary" onclick="">답글저장</button>
			<button type="button" class="btn btn-warning" onclick="">취소</button>
			<!-- reset타입은 페이지를 재로딩할 뿐 서버의 데이터를 지우지 않기 때문에 사용X -->
		</form> <!-- form태그를 submit하면 input태그중에서 name 속성이 있는 값만 데이터를 넘긴다!-->

		<c:import url="./../footer.jsp"></c:import>

	</div>
</body>
</html>