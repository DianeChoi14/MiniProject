<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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
							<td>${board.postDate}</td>
							<td>${board.readCount}</td>

						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div>
  <button type="button" class="btn btn-outline-primary">Primary</button>
		</div>
		<c:import url="./../footer.jsp"></c:import>
</body>
</html>