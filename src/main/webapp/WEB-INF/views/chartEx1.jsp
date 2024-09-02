<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>
<script src="https://kit.fontawesome.com/f1329db3c1.js" crossorigin="anonymous"></script>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Orbit&display=swap"
	rel="stylesheet">

    <script type="text/javascript">
      google.charts.load("current", {packages:["corechart"]});
      google.charts.setOnLoadCallback(drawChart); // 페이지가 로딩되면 drawChart함수 호출
      function drawChart() {
    	  // 하나의 배열 안에 또다른 배열이 있는 것 : 2차원 배열
        var data = google.visualization.arrayToDataTable([ // 차트의 데이터
          ['통학수단', '카운트'], // [x축, y축]
          ['도보',     2],
          ['버스',      2],
          ['지하철',  2]
        ]);

        var options = { // 차트 옵션 설정
          title: '우리반 통학수단 차트',
          is3D: true,
        };

        var chart = new google.visualization.PieChart(document.getElementById('piechart_3d')); // 차트 객체 생성
        chart.draw(data, options); // 차트를 data와 option을 가지고 그린 메서드
      }
    </script>
<style>
.weight {
	font-family: "Orbit", sans-serif; <!-- 띄어쓰기가 특수문자가 있을 수 있으므로 ""로 묶임, 앞의 폰트가 사용할 수 없을 경우 뒤으 폰트로 적용됨-->
	font-weight: 800;
	font-style: normal ;
}
</style>
</head>
<body>

	<div class="container">
		<jsp:include page="./header.jsp"></jsp:include>


		<div class="content">
			<h1>자바스크립트 차트 예제</h1>
			<div class="weight">가나다라마바사</div>
			<i class="fa-solid fa-chart-simple"></i>
			<div id="piechart_3d" style="width: 900px; height: 500px;"></div>
		</div>

		<jsp:include page="./footer.jsp"></jsp:include>
	</div>

</body>
</html>
