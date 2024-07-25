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
<script>
	function showRemoveModal() {
		$(function() {
			// Close, X 버튼 클릭하면 모달창 종료
			$('.modalCloseBtn').click(function() {
				$('#myModal').hide(100);
			});
		});
		let boardNo = $('#boardNo').val();
		$('.modal-body').html(boardNo + "번 글을 정말로 삭제하시겠습니까?");
		$('#myModal').show(500); // .show() 괄호 안에 숫자를 넣으면 m/s단위로 애니메이션 추가됨
	}
</script>
<style>
.content {
	margin-top: 10px;
	margin-bottom: 10px;
	border: 1px solid #dee2e6;
	border-raidus: 0.375rem;
	padding: 10px;
}
</style>
</head>
<body>

	<div class="container">
		<c:import url="../header.jsp"></c:import>


		<div class="content">
			<h1>게시글 상세 페이지</h1>

			<c:forEach var="board" items="${boardDetailInfo}">
				<c:if test="${board.isDelete =='Y' }">
					<c:redirect url="/hboard/listAll?status=wrongAccess"></c:redirect>
				</c:if>
				<div class="boardInfo">
					<div class="mb-3">
						<label for="boardNo" class="form-label">글 번호</label> <input
							type="text" class="form-control" id="boardNo"
							value="${board.boardNo}" readonly>
					</div>
					<div class="mb-3">
						<label for="title" class="form-label">글제목</label> <input
							type="text" class="form-control" id="title"
							value="${board.title}" readonly>
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
						<div class="content">${board.content}</div>
					</div>

				</div>

				<table class="table table-hover">
					<thead>
						<tr>
							<th>#</th>
							<th>uploadedFiles</th>
							<th>fileName</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<c:forEach var="file" items="${board.fileList }">
								<c:if test="${file.boardUpFileNo !='0' }">
									<tr>
										<td>
										<input class="form-check-input" type="checkbox" id="" name="" value="" >
										</td>
										<td>
											<c:choose>
												<c:when test="${file.thumbFileName !=null }">
												<!--이미지파일이라면 -->
												<img src="/resources/boardUpFiles/${file.newFileName }" width="40px" />
												</c:when>
												<c:when test="${file.thumbFileName == null }">
												<!-- 이미지파일이 아니면 -->
												<a href="/resources/boardUpFiles/${file.newFileName }"> <img
													src="/resources/images/noimage.png"> ${file.newFileName }
												</a>
												</c:when>
												
											</c:choose>
										</td>	
										<td>
											${file.newFileName }
										</td>
									</tr>
								</c:if>
							</c:forEach>
						</tr>
					</tbody>
				</table>


				<div class="btns">
					<button type="button" class="btn btn-info"
						onclick="location.href='/hboard/showReplyForm?boardNo=${board.boardNo}&ref=${board.ref }&step=${board.step }&refOrder=${board.refOrder }';">답글달기</button>
					<button type="button" class="btn btn-primary"
						onclick="location.href='/hboard/modifyBoard?boardNo=${board.boardNo}';">글수정</button>
					<button type="button" class="btn btn-danger"
						onclick="showRemoveModal()">글삭제</button>
					<button type="button" class="btn btn-secondary"
						onclick="location.href='/hboard/listAll';">리스트페이지로</button>
				</div>
			</c:forEach>
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


		<c:import url="../footer.jsp"></c:import>

	</div>
</body>
</html>