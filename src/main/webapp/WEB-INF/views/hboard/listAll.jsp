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
		// 웹문서가 로딩이 완료되면 아래에 선언된 함수를 실행하도록
		$(function()
		{
			timediifPostDate(); // 함수 호출
		}) 
		
		// window.onload=function(){}, 제이쿼리 $(function(){}) : 웹브라우저 로딩이 끝나면 아래 함수를 실행하라
		// 게시글의 글작성일을 얻어와서 2시간이내에 작성한 글이라면 new.png이미지를 붙여 출력한다.
		function timediifPostDate() 
		{
	        console.log($(".postDate"));
	        $(".postDate").each(function(i, e) 
			{
            	// $(e) td태그
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

			<table class="table table-hover">
				<thead>
					<tr>
						<th>#</th>
						<th>Title</th>
						<th>Writer</th>
						<th>PostDate</th>
						<th>ReadCount</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="board" items="${boardList}">
						<tr>
							<td>${board.boardNo}</td>
							<td>${board.title}</td>
							<td>${board.writer}</td>
							<td class="postDate">${board.postDate}</td>
							<td>${board.readCount}</td>

						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div>
			<button type="button" class="btn btn-outline-primary">글쓰기</button>
		</div>
		<c:import url="./../footer.jsp"></c:import>
</body>
</html>