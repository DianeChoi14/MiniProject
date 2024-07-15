<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>News</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script>
	function getNewsData() {
		$.ajax({
			url : 'https://mbn.co.kr/rss/enter/', // 데이터가 송수신될 서버의 주소
			type : 'GET', // 통신 방식 : GET, POST, PUT, DELETE, PATCH   
			dataType : 'xml', // 수신 받을 데이터의 타입 (text, xml, json)
			success : function(data) { // 비동기 통신에 성공하면 자동으로 호출될 callback function
				console.log(data);
				outputNews(data);
			}
		});
	}
	function outputNews(data) {
		let channel = data.getElementsByTagName('channel')[0]; // 
		console.log(channel);
		// getElementById() // id속성으로 태그(단일)를 얻어오는 메서드
		// getElementsClassName() // class속성으로 태그(복수)를 얻어오는 메서드

		let title = channel.getElementsByTagName('title')[0].innerHTML.replace(
				"<![CDATA[", "");
		title = title.replace("]]>", "");
		$('.title').html(title);

		let newsLink = channel.getElementsByTagName('link')[0].innerHTML;
		console.log(newsLink);
		$('#newsLink').attr('href', newsLink); // =document.getElementById('newsLink')

		let description = removeCDATA(channel.getElementsByTagName('description')[0].innerHTML);
		$('.desc').html(description);

		let items = channel.getElementsByTagName('item');
		console.log(items);

		// $.each()와 부트스트랩의 list group이용하여 출력 타이틀을 출력하고, 클릭하면 디스크립션
		let output='';
		$.each(items, function(i,e){
				output += '<div class="card">';
				let title = removeCDATA($(e).children().eq(0).html());
				output += `<div class='card-body' data-bs-toggle="collapse" data-bs-target="#demo\${i}">\${title}</div>`; // ``백틱 안에 자바스크립트 객체로 ${}표현식 사용 가능하나 이엘표현식과 구분하기 위해 '\'백스페이스를 앞에 붙여준다(자바에서 실행되지 않도록)
				let desc = removeCDATA($(e).children().eq(3).html());
				output += `<div id='demo\${i}' class='collapse'>\${desc}</div>`;
				output += '</div>';
			});
			$('.newsList').html(output);
	}

/* 	function newsList(items) {
		$.each(items,function(i, e) {
							let news = getNewsData();
							// console.log("뉴스리스트"+e);

							$('.collapseTitle').html(removeCDATA(e.getElementsByTagName('title')[i].innerHTML));
							//console.log("뉴스타이틀" + newsTitle);
							$('.collapse').html(removeCDATA(e.getElementsByTagName('description')[i].innerHTML));
						});
	} */
	function removeCDATA(str) {
		str = str.replace("<![CDATA[", "");
		return str.replace("]]>", "");
	}

	$(function() {
		getNewsData();

	});
</script>
<style>
	.collapse {
		padding : 8px;
		font-sizm : 0.8em;
	}
</style>

</head>


<body>

	<div class="container">
		<c:import url="./header.jsp"></c:import>


		<div class="content">
			<h1>NEWS!</h1>
			<h4>비동기 데이터통신을 이용하여 xml파일을 전송받아 파싱하여 출력해보자</h4>
			<h5 class="title"></h5>
			<a id='newsList'><h6 class='desc'></h6></a>
		</div>
 <!--
		<div class="card">
			<h2 class="card-body" data-bs-toggle="collapse" data-bs-target="#demo"></h2>
			<div id="demo" class="collapse"></div>
		</div>
 -->
		<c:import url="./footer.jsp"></c:import>

	</div>
</body>
</html>
