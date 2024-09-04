<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=538f5a0703238f35c77384fc3289d2f7"></script>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>
<script>
	$(function(){
		$('#searchBtn').click(function(){
			let searchValue = $('#searchValue').val();
			if(searchValue != ''){
				$.ajax({
					url : '/searchBook',
					type : 'GET',
					data : {
						"searchValue" : searchValue
					},
					dataType : 'json',
					success : function(data) {
						console.log(data);
					}
				});
			}
				
		});
		
	});
</script>
</head>


<body>

	<div class="container">
		<c:import url="./header.jsp"></c:import>


		<div class="content">
			<h1>네이버 API 검색기능 연습</h1>
			<input type="text" placeholder="검색할 책을 입력하세요.." id="searchValue"/>
			<button id="searchBtn">검색</button>
		</div>
		<c:import url="./footer.jsp"></c:import>

	</div>
</body>
</html>
