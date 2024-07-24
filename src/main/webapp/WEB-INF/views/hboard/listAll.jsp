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

	</script>
<script>
		// 웹문서가 로딩이 완료되면 아래에 선언된 함수를 실행하도록
		$(function()
		{
			showModalAccordingToStatus(); // 모달창 함수 호출
			timediifPostDate(); // 함수 호출

			$('.modalCloseBtn').click(function() 
		{
			//클래스가 modatCloseBtn인 태그를 클릭(실행)하면 실행되는 함수
			$('#myModal').hide(); // 태그를 숨기는 함수
		})
		}) 

		// 데이터 로딩 상태에 따라 모달창을 띄우는 함수
		function showModalAccordingToStatus() {
			 // url주소창에서 status쿼리스트링의 값을 가져와 변수에 저장
			 let status = '${param.status}';
			console.log(status);
			
			if(status =='success'){
				// 글 성공 모달창을 띄움
				$('.modal-body').html("<h5>글 저장 성공</h5>");
				$('#myModal').show();
				
			} else if (status == 'fail') {
				$('.modal-body').html("<h5>실패실패</h5>");
				$('#myModal').show();
			} 
			
			let except = '${exception}';
			if(except=='error'){
				$('.modal-body').html("<h5>게시글이 없거나 문제가 발생해 데이터를 불러오지 못 했습니다.</h5>");
				$('#myModal').show();
			}
		}
		
		// window.onload=function(){}, 제이쿼리 $(function(){}) : 웹브라우저 로딩이 끝나면 아래 함수를 실행하라
		// 게시글의 글작성일을 얻어와서 2시간이내에 작성한 글이라면 new.png이미지를 붙여 출력한다.
		function timediifPostDate() 
		{
	        console.log($(".postDate"));
	        $(".postDate").each(function(i, e) 
			{
            	//console.log(i+'번째 태그:' + $(e).html());
            	// .html() 매개변수가 없으면 getter, 있으면 setter
            	let postDate = new Date($(e).html());   //글 작성일 저장 (Date객체로 변환후)
            	let curDate = new Date();   //현재 날짜 현재 시간 객체 생성
           		console.log(postDate, curDate);
           		//console.log("현재 시간과의 차이 :" + (curDate - postDate));
            	// 아래의 시간차이는 timestamp값(1970년 1월1일0시0분0초 부터 지금까지 흘러온 시간을 정수로 표현한값)
           		// 단위는 ms이므로 시간 차이로 바꾸면
            	let diff = (curDate-postDate) / 1000 / 60 / 60;  //시간 차이의 값
               								// /1000 초단위 /60 분단위 /60 시간단위 /24 일단위
            	console.log(diff);
               
				let title = $(e).prev().prev().html(); 
				console.log(title);
				if(diff < 2) //2시간 이내에 작성한 글 이라면...글 제목 앞에 new 이미지 태그를 넣어 출력
				{ 
					let output = "<span><img src='/resources/images/new.png' width=40></span>";
					$(e).prev().prev().html(output + title);
				}

			});
		}; 
	</script>

</head>
<body>
	<div class="container">
		<c:import url="./../header.jsp"></c:import>


		<div class="content">
			<h1>계층형 게시판 전체 리스트 페이지</h1>
			<c:choose>
				<c:when test="${boardList!=null}">
					<table class="table table-hover">
						<thead>
							<tr>
								<th>#</th>
								<th>Title</th>
								<th>Writer</th>
								<th>PostDate</th>
								<th>ReadCount</th>
								<th>isDelete</th>
							</tr>
						</thead>
						<tbody>

							<c:forEach var="board" items="${boardList}">
								<c:choose>
									<c:when test='${board.isDelete=="N"}'>

										<tr
											onclick="location.href='/hboard/viewBoard?boardNo=${board.boardNo}';">
											<td>${board.boardNo}</td>
											<td><c:forEach var="i" begin="1" end="${board.step}">
													<img src="/resources/images/reply.png" />
												</c:forEach> ${board.title}</td>
											<td>${board.writer}</td>
											<td class="postDate">${board.postDate}</td>
											<td>${board.readCount}</td>
											<td>${board.isDelete}</td>
										</tr>
									</c:when>
									<c:when test="${board.isDelete=='Y' }">
										<tr>
											<td>${board.boardNo}</td>
											<td>삭제된 글입니다......</td>
											<td></td>
											<td class="postDate"></td>
											<td>${board.isDelete}</td>
										</tr>
									</c:when>
								</c:choose>


							</c:forEach>
						</tbody>
					</table>
				</c:when>

			</c:choose>


		</div>
		<div>
			<button type="button" class="btn btn-outline-primary"
				onclick="location.href='/hboard/saveBoard';">글쓰기</button>
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
						<button type="button" class="btn btn-danger modalCloseBtn"
							data-bs-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>

		<c:import url="./../footer.jsp"></c:import>
</body>
</html>