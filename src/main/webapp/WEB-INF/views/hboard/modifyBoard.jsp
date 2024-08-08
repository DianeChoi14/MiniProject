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
		function showPreview(obj) {
			if(obj.files[0].size > 1024*1024*10) {
				alert('10MB이하의 파일만 업로드할 수 있습니다...');
				obj.value=""; // 선택한 파일 초기화
				return ; // addRows(obj)의 showPreview(this)로 초기화...
			}
				
			console.log(obj.files[0].type); // 파일타입확인하고 .
			let imageType = ["image/jpeg", "image/jpg", "image/png", "image/gif"];
			//파일 타입 확인
			let fileType = obj.files[0].type;
			let fileName = obj.files[0].name;
			if(imageType.indexOf(fileType) != -1) { // 이미지타입이면 위치(숫자)를 반환하고, 아니면 -1을 반환
				let reader = new FileReader();//fileReadre객체 생성
				// reader객체에 의해 파일 읽기를 완료하면 실행되는 callback함수
				reader.onload = function(e) { 				
					let imgTag = `<div style='padding:10px;'><img src='\${e.target.result}' /></div>`;
					$(imgTag).insertAfter(obj);
				}
				reader.readAsDataURL(obj.files[0]); // 업로드된 파일을 읽어오는 메서드
			} else {
				let imgTag = `<div style='padding:10px;'><img src='/resources/images/noimage.png' /><span>\${fileName}</span></div>`;
				
				$(imgTag).insertAfter(obj);
			}
		
		}
		
		function cancelAddFile(obj) {
			let fileTag = $(obj).parent().prev().children().eq(0);
			$(fileTag).val(''); // 선택파일 초기화
			$(fileTag).parent().parent().remove(); 
		}
		
		function addRows(obj) {
			let rowCnt = $('.fileListTable tr').length;
			console.log(rowCnt + "로우카운트~");
			let row = $(obj).parent().parent(); //tr태그
			let inputFileTag = `<tr><td colspan='2'><input class='form-control' type='file' id='newFile_\${rowCnt}' onchange='showPreview(this)' name='modifyNewFile' multiple /></td>
								<td><input type="button" class="btn btn-danger " value="파일저장취소" onclick="cancelAddFile(this);"/></td></tr>`;
			
			$(inputFileTag).insertBefore(row); // cloneRow를 row위로 추가
		}
		
		
		function cancelRemoveFile() {
				$.ajax({
					url : '/hboard/cancelRemFiles', 	
					type : 'post', 
					dataType : 'json', 			
					async : false, 
					success : function(data) { 	
							if(data.msg == 'success') {
								$('.fileCheck').each(function(i,item){
									$(item).prop('checked',false);
									$('#' + $(item).attr('id')).parent().parent().css('opacity', 1);
									
								});
								$('.removeUpFileBtn').attr('disabled', true);
							}
					},
					error : function(data) {
							console.log(data);
					}
				});
		}
		
		function removeFile() {
			let removeFileArr = []; // 삭제할 파일(체크된)을 담는 배열
			$('.fileCheck').each(function(i, item){
				if($(item).is(':checked')){
					let tmp = $(item).attr('id'); //선택된 파일의 id값(fileBoardNo)
					removeFileArr.push(tmp); // id값을 배열에 저장
					console.log("삭제될 파일 : " + removeFileArr);
				}
			});
			
			$.each(removeFileArr, function(i, item){
				// 배열 안의 요소의 개수만큼 반복, i 만큼 반복하여 removeFileArr값을 item에 넣는다
				$.ajax({
					url : '/hboard/modifyRemoveFileCheck', 	
					type : 'post', 
					data : {"removeFileNo" : item },
					dataType : 'json', 			
					async : false, 
					success : function(data) { 	
						console.log(data);
						if(data.msg == "success") {
							$('#'+item).parent().parent().css('opacity', 0.2);
						}
					},
					error : function(data) {
							console.log(data);
					}
				});
			});
		}
		
		function removeFileCheck(fileId) {
			// alert('check' + fileId); fileCheck
			let chkCount = isCheckBoxChecked();
			if(chkCount>0) {
				$('.removeUpFileBtn').attr('disabled', false); // .removeAttr('disalbed')
				$('.removeUpFileBtn').val(chkCount + "개 파일을 삭제합니다!!!!");
			} else if(chkCount==0) {
				$('.removeUpFileBtn').attr('disabled', true);
				$('.removeUpFileBtn').val("선택된 파일 없음");
			}
			
		}

		function isCheckBoxChecked() {
			let result = 0;
			$('.fileCheck').each(function(i, item){
				if($(item).is(':checked')){
					result++;
				}
			});
			console.log(result);
			document.getElementsByClassName('fileCheck') // 클래스가 fileCheck인 것을 데려와서 배열로 만들어줌
			return result;
		}
		
		
	</script>
<style>
.fileBtns {
	display: flex;
	justify-content: flex-end;
}

.fileBtns input, button {
	margin-left: 5px;
}
.btns {
	display : flex;
	justify-content:center;
}
</style>
</head>
<body>

	<div class="container">
		<jsp:include page="../header.jsp"></jsp:include>


		<div class="content">
			<h1>게시글 수정 페이지</h1>

			<c:forEach var="board" items="${boardDetailInfo}">
			<form action="/hboard/modifyBoardSave" method="post" enctype="multipart/form-data">
				<div class="boardInfo">
					<div class="mb-3">
						<label for="boardNo" class="form-label">글 번호</label> <input
							type="text" class="form-control" id="boardNo" name="boardNo"
							value="${board.boardNo}" readonly>
					</div>
					<div class="mb-3">
						<label for="title" class="form-label">글제목</label> <input
							type="text" class="form-control" id="title" name="title"
							value="${board.title}">
					</div>
					<div class="mb-3">
						<label for="writer" class="form-label">작성자</label> <input
							type="text" class="form-control" id="writer"
							value="${board.writer}(${board.email})" readonly>
					</div>
					<div class="mb-3">
						<label for="postDate" class="form-label">작성일</label> <input
							type="text" class="form-control" id="postDate"
							value="${board.postDate}" readonly>
					</div>
					<div class="mb-3">
						<label for="readCount" class="form-label">조회수</label> <input
							type="text" class="form-control" id="readCount"
							value="${board.readCount}" readonly>
					</div>
					<div class="mb-3">
						<label for="content" class="form-label">내용</label>
						<textarea class="form-control" id="content" name="content" rows="5">${board.content}</textarea>
					</div>
					<div>${board.ref }${board.step }${board.refOrder }</div>
				</div>

				<table class="table table-hover fileListTable">
					<thead>
						<tr>
							<th>#</th>
							<th>uploadedFiles</th>
							<th>fileName</th>
						</tr>

					</thead>
					<tbody>
						<c:forEach var="file" items="${board.fileList}">
							<c:if test="${file.boardUpFileNo != '0' }">
								<tr>
									<td><input class="form-check-input fileCheck"
										type="checkbox" id="${file.boardUpFileNo}"
										onclick="removeFileCheck(this.id);"></td>
									<td><c:choose>
											<c:when test="${file.thumbFileName != null}">
												<!-- 이미지 파일이라면 -->
												<img src="/resources/boardUpFiles/${file.newFileName}"
													width="10px" />
											</c:when>
											<c:when test="${file.thumbFileName == null }">
												<a href="/resources/boardUpFiles/${file.newFileName}"> <img
													src="/resources/images/noimage.png" /> ${file.newFileName}
												</a>
											</c:when>
										</c:choose></td>
									<td>${file.newFileName}</td>
								</tr>
							</c:if>
						</c:forEach>
						<tr>
							<td colspan="3" style="text-align: center;"><img
								src="/resources/images/add.png" onclick="addRows(this);" /></td>
						</tr>
					</tbody>
				</table>

				<div class="fileBtns">
					<div>
						<input type="button" class="btn btn-info cancelRemove"
							value="파일삭제 취소" onclick="cancelRemoveFile();" /> <input
							type="button" class="btn btn-danger removeUpFileBtn"
							value="선택한 파일 삭제" onclick="removeFile();" disabled />
					</div>
				</div>
				<p>
				<div class="btns">
					<button type="button" class="btn btn-secondary" onclick="location.href='/hboard/viewBoard?boardNo=${board.boardNo}';">취소</button>
					<button type="submit" class="btn btn-primary" onclick="">수정저장</button>
				</div>
				
		</div>

		</form>
		</c:forEach>
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
					<button type="button" class="btn btn-info" data-bs-dismiss="modal"
						onclick="location.href='/hboard/removeBoard?boardNo=${param.boardNo}';">삭제</button>

					<button type="button" class="btn btn-danger modalCloseBtn"
						data-bs-dismiss="modal">취소</button>
				</div>
			</div>
		</div>
	</div>


	<jsp:include page="../footer.jsp"></jsp:include>

	</div>
</body>
</html>