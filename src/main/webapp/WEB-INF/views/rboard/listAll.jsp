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
		// 검색버튼을 눌렀을 때 searchType가 -1이거나 searchWord가 '' 이면 검색어가 제대로 입력되지 않았으므로
		//백엔드단으로 데이터를 넘기지 않는다.
		function isValid() {
			let result=false;
			if($('#searchType').val() == -1 || $('#searchWord').val() == '') {
				alert("검색조건과 검색어를 지정해주세요!");
				$('#searchType').focus();
			} else {
				result= true;
			}
			
			return result;
		}
		// 웹문서가 로딩이 완료되면 아래에 선언된 함수를 실행하도록
		$(function()
		{	
			// 파라미터에서 페이징 사이즈 받아오기 
			let pagingSize = '${param.pagingSize}'
			if(pagingSize == '') {
				pagingSize==10;
			} else {
				pagingSize=parseInt(pagingSize);
			}
			$('.pagingSize').val(pagingSize);
			
			// 유저가 페이징사이즈를 선택하면..
			$('.pagingSize').change(function(){
				console.log($(this).val()); // this=select태그 자체
				let pageNo ='${param.pageNo}';
				if(pageNo=='') {
					pageNo=1;
				} else {
					pageNo = parseInt('${param.pageNo}');
				}
				location.href="/hboard/listAll?pagingSize=" + $(this).val() + "&pageNo=" + pageNo + "&pagingSize=${param.pagingSize}&searchType=${search.searchType}&searchWord=${searchWord}";
			})
/* 			// 현재 페이지번호를 가져와서 페이지번호의 pagination의 li태그에 부트스트랩의 active클래스 부여
			let pageNo ='${param.pageNo}';
			if(pageNo=='') {
				pageNo=1;
			} else {
				pageNo = parseInt('${param.pageNo}');
			}
			$(`#\${pageNo}`).addClass('active'); // .addClass 클래스명 부여 */
			
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
	<div class="container"></div>

	<c:import url="./../header.jsp"></c:import>


	<div class="content">
		<h1>계층형 게시판 전체 리스트 페이지</h1>

		<div class="boardControl">
			<select class="form-select pagingSize" id="pagingSize"
				style="width: 50%">
				<option value="10">10개씩 보기</option>
				<option value="20">20개씩 보기</option>
				<option value="40">40개씩 보기</option>
				<option value="80">80개씩 보기</option>
			</select>
		</div>

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
<%-- 								<c:when test="${board.isDelete=='Y' }">
									<tr>
										<td>${board.boardNo}</td>
										<td>삭제된 글입니다......</td>
										<td></td>
										<td class="postDate"></td>
										<td>${board.isDelete}</td>
									</tr>
								</c:when> --%>
							</c:choose>


						</c:forEach>
					</tbody>
				</table>
			</c:when>

		</c:choose>


	</div>
	<div class="pagenation">
		<ul class="pagination justify-content-center" margin="center">
			<c:if test="${param.pageNo >1 }">
				<li class="page-item"><a class="page-link"
					href="/rboard/listAll?pageNo=${param.pageNo-1}&pagingSize=${param.pagingSize}&searchType=${search.searchType}&searchWord=${searchWord}">Previous</a></li>
			</c:if>
			<c:forEach var="i" begin="${pagingInfo.startPageNoCurBlock}"
				end="${pagingInfo.endPageNoCurBlock}">
				<c:choose>

					<c:when test="${param.pageNo == i }">
						<li class="page-item active" id="${i}"><a class="page-link"
							href="/hboard/listAll?pageNo=${i}&pagingSize=${param.pagingSize}&searchType=${search.searchType}&searchWord=${searchWord}">${i}</a></li>
					</c:when>
					<c:otherwise>
						<li class="page-item" id="${i}"><a class="page-link"
							href="/hboard/listAll?pageNo=${i}&pagingSize=${param.pagingSize}&searchType=${search.searchType}&searchWord=${searchWord}">${i}</a></li>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<c:if test="${param.pageNo < pagingInfo.totalPageCnt }">
				<li class="page-item"><a class="page-link"
					href="/hboard/listAll?pageNo=${param.pageNo+1}&pagingSize=${param.pagingSize}&searchType=${search.searchType}&searchWord=${searchWord}">Next</a></li>
			</c:if>
		</ul>
	</div>

	<div style="float: right; margin-right: 5px">
		<button type="button" class="btn btn-outline-primary"
			onclick="location.href = '/rboard/showSaveBoardForm';">글쓰기</button>
	</div>

	<form class="searchBar" action="/hboard/listAll" method="post"
		style="display: flex; flex-direction: row; clear: right; align-items: center; justify-content: center;">
		<div class="input-group mt-3 mb-3" style="width: 80%">
			<select class="form-select" name="searchType" id="searchType">
				<option value="-1">==검색조건==</option>
				<!--검색조건이 없을 경우를 설정 > 유효성 검사-->
				<option value="title">제목</option>
				<option value="writer">작성자</option>
				<option value="content">내용</option>
			</select> <input type="text" class="form-control" name="searchWord"
				id="searchWord" placeholder="검색어를 입력하세요.."> <input
				type="hidden" name="pageNo" value="${param.pageNo}" /> <input
				type="hidden" name="pagingSize" value="${param.pagingSize}" />


			<button type="submit" class="btn btn-primary"
				onclick="return isValid();">검색</button>
			<!-- submit버튼은 onclick에서 retrun반드시! : boolean값을 return하여 인풋데이터를 보낼지말지 여부를 결정 -->
		</div>
	</form>



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