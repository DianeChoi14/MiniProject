<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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

	<div class="container">
		<jsp:include page="../header.jsp"></jsp:include>


		<div class="content">
			<h1>게시글 수정 페이지</h1>

			<form action="/rboard/modifyBoardSave" method="post" enctype="multipart/form-data">
				<div class="boardInfo">
					<div class="mb-3">
						<label for="boardNo" class="form-label">글 번호</label> <input
							type="text" class="form-control" id="boardNo" name="boardNo"
							value="${board.boardNo}" readonly>
					</div>
					<div class="mb-3">
						<label for="title" class="form-label">글제목</label> <input
							type="text" class="form-control" id="title" name="title"
							value="${board.title}">
					</div>
					<div class="mb-3">
						<label for="writer" class="form-label">작성자</label> <input
							type="text" class="form-control" id="writer"
							value="${board.writer}(${board.email})" readonly>
					</div>
					<div class="mb-3">
						<label for="postDate" class="form-label">작성일</label> <input
							type="text" class="form-control" id="postDate"
							value="${board.postDate}" readonly>
					</div>
					<div class="mb-3">
						<label for="readCount" class="form-label">조회수</label> <input
							type="text" class="form-control" id="readCount"
							value="${board.readCount}" readonly>
					</div>
					<div class="mb-3">
						<label for="content" class="form-label">내용</label>
						<textarea id="summernote" name="content">${board.content}</textarea>
					</div>
				</div>

				<p>
				<div class="btns">
					<button type="button" class="btn btn-secondary" onclick="location.href='/rboard/viewBoard?boardNo=${board.boardNo}';">취소</button>
					<button type="submit" class="btn btn-primary" onclick="">수정저장</button>
				</div>
				
		</div>

		</form>
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
				<div class="modal-body"></div>

				<!-- Modal footer -->
				<div class="modal-footer">
					<button type="button" class="btn btn-info" data-bs-dismiss="modal"
						onclick="location.href='/hboard/removeBoard?boardNo=${param.boardNo}';">삭제</button>

					<button type="button" class="btn btn-danger modalCloseBtn"
						data-bs-dismiss="modal">취소</button>
				</div>
			</div>
		</div>
	</div>


	<jsp:include page="../footer.jsp"></jsp:include>

	</div>
</body>
</html>