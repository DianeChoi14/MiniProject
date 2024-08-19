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
	// 함수 내부에서 만드는 변수는 지역변수, 외부에서는 전역변수
	let pageNo = 1;
	
	// 웹문서가 로딩되면 실행되는 달라펑션 $(document).ready(function(){}) 이 원형,
	// 제이쿼리를 안 썼을 때 window.onload = function(){}와 같은 의미
	$(function() {
		getAllReplies();
		// Close, X 버튼 클릭하면 모달창 종료
		$('.modalCloseBtn').click(function() {
			$('#myModal').hide(100);
		});
	});
	
	function getAllReplies() {
		$.ajax({
			url : "/reply/all/${param.boardNo}/" + pageNo, 	
			type : 'get', 
			dataType : 'json', 			
			async : false, // 비동기식 통신 요청 > 동기식
			success : function(data) { 	
				console.log(data);
				if(data.resultCode == 200 || data.resultMsg == "SUCCESS"){
					outputReplies(data);
				}
				
			},
			error : function(data) {
				console.log(data);
				alert("댓글을 불러오지 못 했습니다..ㅜㅡ");
			}
		});		
	}
	
	function outputReplies(replies) {
		let output = `<div class="list-group">`;
		
		$.each(replies.data.replyList, function(i, reply){
			output +=`<a href="#" class="list-group-item list-group-item-action reply">`;
			output += `<div class='replyBody'>`;
			output += `<div class='replyerProfile'>`;
			output += `<img src='/resources/userImg/\${reply.userImg}'/>`;
			output += `</div>`;	
			output += `<div class='replyBodyArea'>`;
			output += `<div class='replyContent'>\${reply.content}</div>`;		
			output += `<div class='replyInfo'>`;
			let bwnTime = processPostDate(reply.regDate);
			output += `<div class='regDate'>\${bwnTime}</div>`;
			output += `<div class='replyer' onmouseover='showReplyInfo(this);' onmouseout='hideReplyInfo(this);'>\${reply.replyer}</div>`;
			output += `<div class='replyerInfo'>\${reply.userName}(\${reply.email})</div>`;
			output += `</div>`;		
			output += `</div>`;
			output += `</div>`;
			output += `</a>`;
			
		});
		  
		output += `</div>`;
		$(".replyList").html(output);
	}
	// 댓글작성일시를 방금전, n분 전, n시간 전.. 의 형식으로 출력
	function processPostDate(writtenDate) {
		const postDate = new Date(writtenDate); // 댓글작성시간
		const now = new Date(); // 현재시간
		
		let diff = (now-postDate) / 1000; // timestamp가 m/s 값이므로, 시간차이를 초단위로 구하기 위함
		
		const times = [
			{name : "일", time : 60*60*24},
			{name : "시간", time : 60*60},
			{name : "분", time : 60}
		];
		for(let val of times) {
			let bwnTime = Math.floor(diff / val.time);
			console.log(diff, bwnTime);
			
			if(bwnTime>0 && val.name =="일") {
				return bwnTime + val.name + "전";
			} else if (bwnTime>0 && val.name !="일") {
				return postDate.toLocaleDateString();
			}
			
			return "방금 전";
		}
	}
	function hideReplyInfo(obj) {
		$(obj).next().hide();
	}
	function showReplyInfo(obj) {
		$(obj).next().show();
	}
	
	function showRemoveModal() {	
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
.replyList {
	margin-top : 15px;
	padding : 10px;
	
}
.replyBody {
	display : flex;
	justify-content: space-between;
	flex-direction: row;
	align-items: center;
	color : rgba(0,0,0,0.8);
	
}
.replyBodyArea {
	flex : 1; 
	margin-left: 10px;
}
.replyerProfile img {
	width :50px;
	border-radius: 25px;
	border: 1px solid green;
}
.replyInfo {
	display: flex;
	flex-direction: row;
	justify-content: space-between;
	font-size : 0.8rem;
	color : rgba(0,0,0,0.6);
}
.replyerInfo {
	color : white;
	background-color: #333;
	padding: 5px;
	border-radius: 4px;
	display : none;
}

</style>
</head>
<body>

	<div class="container">
		<c:import url="../header.jsp"></c:import>


		<div class="content">
			<h1>게시글 상세 페이지</h1>

			
<%-- 				<c:if test="${board.isDelete =='Y' }">
					<c:redirect url="/hboard/listAll?status=wrongAccess"></c:redirect>
				</c:if> --%>
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



				<div class="btns">
					<button type="button" class="btn btn-primary"
						onclick="location.href='/rboard/modifyBoard?boardNo=${board.boardNo}';">글수정</button>
					<button type="button" class="btn btn-danger"
						onclick="showRemoveModal()">글삭제</button>
					<button type="button" class="btn btn-secondary"
						onclick="location.href='/rboard/listAll';">리스트페이지로</button>
				</div>
			
				<div class="replyList">
					
				</div>
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