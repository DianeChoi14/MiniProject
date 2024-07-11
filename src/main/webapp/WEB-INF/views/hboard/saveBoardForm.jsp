<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
 <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>


<link href="https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha1/css/bootstrap.min.css" rel="stylesheet">
<script src="https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha1/js/bootstrap.min.js"></script>

<div class="container">

	<c:import url="./../header.jsp"></c:import>

    <h2>게시판 글 작성</h2>

    <form action="saveBoard" method="post">
        <div class="mb-3">
            <label for="title" class="form-label">글제목</label>
            <input type="text" class="form-control" id="title" name="title" placeholder="글제목을 입력하세요">
        </div>
        <div class="mb-3">
            <label for="author" class="form-label">작성자</label>
            <input type="text" class="form-control" id="author" name="writer" placeholder="작성자를 입력하세요">
        </div>
        <div class="mb-3">
            <label for="content" class="form-label">내용</label>
            <textarea class="form-control" id="content" name= "content" rows="5" placeholder="내용을 입력하세요"></textarea>
        </div>
        <button type="submit" class="btn btn-primary" >저장</button>
    </form>

    <c:import url="./../footer.jsp"></c:import>
    
</div>


</body>
</html>