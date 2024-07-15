<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>
	function validBoard() {
		let result = false;

		let title = $('#title').val();
		console.log(title);

		if (title == '' || title.length < 1 || title == null) {
			// 제목을 입력 하지 않을때
			alert("제목은 비울 수 없습니다.");
			$('#title').focus();
		} else {
			// 제목을 입력 했을때
			result = true;
		}

		// 유효성 검사 하는 자바스크립트에서는 마지막에 boolean 타입의 값을 반환하여 데이터가 백엔드 단으로 넘어갈지 말지를 결정해 줘야한다
		return result;
	}
	
	// 자바의 배열은 정적배열(사이즈가 정해짐) 자바스크립트는 동적배열
	
	let upfiles = new Array(); // 업로드되는 파일을 저장하는 배열로 사용
	
	$(function(){
		// 업로드파일영역에 파일을 drag&drop하는 것과 관련한 이벤트(파일의 경우 파일이 웹브라우저에서 실행되는 경우를 방지>이벤트캔슬링)
		$('.fileUploadArea').on("dragenter dragover", function(evt) {
			evt.preventDefault(); // 기본 이벤트 캔슬(이미지를 가져가면 웹브라우저로 열리는 이벤트)
		});
		$('.fileUploadArea').on("drop", function(evt){
			evt.preventDefault();
			console.log(evt.originalEvent.dataTransfer.files); // 업로드되는 파일 객체의 정보 > 이미지파일일 경우 미리보기
			
			for(let file of evt.originalEvent.dataTransfer.files){
				upfiles.push(file); // 배열에 담기

			//미리보기
				showPreview(file);
			}	
		});
	});
	
	// 넘겨진 file이 이미지파일이면 미리보기하여 출력한다.
	function showPreview(file){
		let imageType = ["image/jpeg", "image/png", "image/gif"];
		console.log(file);
		let fileType = file.type.toLowerCase();
		if (imageType.indexOf(fileType) != -1)) {
			// 이미지 파일이라면
			alert("이미지 파일입니다!");
		} else() {
			// 이미지 파일이 아니면
			let output =`<div><img src='/resources/images/noimage.png' /><span>\${file.name}</span>`
			output += `<span><img src='/resources/images/remove.png' width='20px' onclick="remFile(this);"/></span></div>`;
			$('.preview').html(output);
		}
	}
	
	function remFile(obj){
		let removedFileName = $(obj).parent().prev().html();
		for(let i=0 ; i <upfiles.length ; i++){
			if(upfiles[i].name == removedFileName){
				// 파일 삭제
				upfiles.splice(i, 1);//배열에서 삭제
				console.log(upfiles)
				$(obj).parent().parent().remove(); // 태그 삭제
			}	
		}
	}
</script>
<style>
.fileUploadArea {
	width: 100%;
	height: 300px;
	background-color: lightgray;
	text-align: center;
	line-height : 300px;
}
</style>
</head>
<body>


	<link
		href="https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha1/css/bootstrap.min.css"
		rel="stylesheet">
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha1/js/bootstrap.min.js"></script>

	<div class="container">

		<c:import url="./../header.jsp"></c:import>

		<h2>게시판 글 작성</h2>
		<!-- multipart/form-data : 데이터를 여러 조각으로 나누어서 전송하는 방식, 수신되는 곳에서 재조립이 필요하다 -->
		<form action="saveBoard" method="multipart/form-data">
			<div class="mb-3">
				<label for="title" class="form-label">글제목</label> <input type="text"
					class="form-control" id="title" name="title"
					placeholder="글제목을 입력하세요">
			</div>
			<div class="mb-3">
				<label for="author" class="form-label">작성자</label> <input
					type="text" class="form-control" id="author" name="writer"
					placeholder="작성자를 입력하세요">
			</div>
			<div class="mb-3">
				<label for="content" class="form-label">내용</label>
				<textarea class="form-control" id="content" name="content" rows="5"
					placeholder="내용을 입력하세요"></textarea>
			</div>

			<div class="fileUploadArea mb-3"">
				<p>업로드할 파일을 드래그 드랍하세요!</p>

			</div>
			<div class="preview"></div>

			<button type="submit" class="btn btn-primary"
				onclick="return validBoard();">저장</button>
		</form>

		<c:import url="./../footer.jsp"></c:import>

	</div>


</body>
</html>