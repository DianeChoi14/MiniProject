<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<meta charset="UTF-8">
<title>INDEX</title>
<script
   src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<!-- font -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
   href="https://fonts.googleapis.com/css2?family=Dongle:wght@300;400;700&display=swap"
   rel="stylesheet">
<!-- ------------------------------------------------------------------------------------- -->

<!-- chart library -->
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<!-- ------------------------------------------------------------------------------------- -->
</head>
<script>
   // 구글 차트 라이브러리 불러오기
   google.charts.load('current', {'packages':['bar']});
   google.charts.setOnLoadCallback(drawChart);
   // ---------------------------------------------------------------------------------------
   
   let students = new Array(); // 빈 배열을 선언해준다.
   students[0] = ["이름", "국어", "영어", "수학", "총점", "평균"];

   function score() {
      // 입력된 이름, 국어, 영어, 수학 점수를 얻어온다.
      $(".student").each(function(i, student) {
         let stuName = $(student).find('.stuName').val();
         let kor = $(student).find('.kor').val();
         let eng = $(student).find('.eng').val();
         let math = $(student).find('.math').val();

         // 총점, 평균을 계산한다.
         let tot = parseInt(kor) + parseInt(eng) + parseInt(math);
         let avg = tot/3;

         // 총점, 평균 출력
         $(student).find('.tot').html(tot);
         $(student).find('.avg').html(avg);

         // students 배열에 학생 1명을 또 다른 배열로 넣는다.
         students[i + 1] = [stuName, kor, eng, math, tot, avg];

         // console.log(students);
      });
      
      drawChart();
   }
   
   
   function drawChart() {
        var data = google.visualization.arrayToDataTable(students);

        var options = {
          chart: {
            title: '성적표',
          }
        };

        var chart = new google.charts.Bar(document.getElementById('columnchart_material'));

        chart.draw(data, google.charts.Bar.convertOptions(options));
      }
</script>

<style>
   * {
      font-family: "Dongle";
      font-size: x-large;
   }
</style>

<body>
   <div class="container">
      <jsp:include page="./header.jsp"></jsp:include>

      <div class="content">
      <h1>우리반 성적</h1>
      <table>
         <tr>
            <td>이름</td>
            <td>국어</td>
            <td>영어</td>
            <td>수학</td>
            <td>총점</td>
            <td>평균</td>
         </tr>
         <tr class="student">
            <td><input type="text" class="stuName"/></td>
            <td><input type="number" class="kor"/></td>
            <td><input type="number" class="eng"/></td>
            <td><input type="number" class="math"/></td>
            <td class="tot"></td>
            <td class="avg"></td>
         </tr>
         <tr class="student">
            <td><input type="text" class="stuName"/></td>
            <td><input type="number" class="kor"/></td>
            <td><input type="number" class="eng"/></td>
            <td><input type="number" class="math"/></td>
            <td class="tot"></td>
            <td class="avg"></td>
         </tr>
         <tr class="student">
            <td><input type="text" class="stuName"/></td>
            <td><input type="number" class="kor"/></td>
            <td><input type="number" class="eng"/></td>
            <td><input type="number" class="math"/></td>
            <td class="tot"></td>
            <td class="avg"></td>
         </tr>
      </table>
      <input type="button" value="성적 입력 완료" onclick="score();" />
      
      <div id="columnchart_material" style="width: 800px; height: 500px;"></div>
      
      </div>
      <jsp:include page="./footer.jsp"></jsp:include>
      <script src="https://kit.fontawesome.com/828eac3ad2.js" crossorigin="anonymous"></script>
   </div>
</body>
</html>
