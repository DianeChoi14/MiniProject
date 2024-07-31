<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<title>회원 가입 페이지</title>
<script>
	// 웹페이지가 로딩된 다음 실행할 함수
	$(function(){
		// keyup 키보드가 눌려졌을 때 발생하는 이벤트
		$('#userId').keyup(function(evt){
			let tmpUserId = $('#userId').val();
			if(tmpUserId.length<4||tmpUserId.length>8){
				outputError("아이디는 4~8자로 입력하세요..", $('#userId'));
				setTimeout(()=> {
					$('.error').remove();
				},1000);
				$('#idValid').val('');
			} else {
				$.ajax({
					url:'/member/isDuplicate',
					type:'post',
					dataType:'json',
					data :{"tmpUserId" : tmpUserId},
					success:function(data){
						console.log(data);
						if(data.msg=='duplicate') {
							outputError('중복된 아이디입니다', $('#userId'));
							$('#idValid').val('');
							$('#userId').focus();
						} else if (data.msg=='not duplicate') {
							clearError($('#userId')); 
							
							// 최종 아이디 통과
							$('#idValid').val('checked');
						}
					},
					error : function(data) {
						console.log(data);
					}
				});
			}
		});
	});
	
	function outputError(msg, obj){
		let errorTag = `<div class="error">\${msg}</div>`;
		$(errorTag).insertAfter(obj);
		$(obj).css('border', '2px, solid red'); // 에러난 태그의 테두리를 빨간색으로

	}
	// obj 다음 이웃 태그(에러메시지 div)를 지운다
	   function clearError(obj) {
	      $('.error').remove(); //'.error' 클래스의 태그를 찾아 지운다..
	      $(obj).css('border', '') // css를 원래상태로..
	   }
	   function isValid() {

		// 아래의 조건에 만족할 때 회원가입을 진행(return true), 만족하지 않으면 회원가입이 되지 않도록(return false)
		// 1) 아이디는 필수, 4~8자, 아이디는 중복 불가
		// 2) 비밀번호는 필수, 4~8자, 비밀번호 확인과 동일해야한다..
		      let idCheck = idValid();
		      let pwdCheck = pwdValid();
		      

		      if (idCheck) {
		            return true;
		        } else {
		            return false;
		        }

		
	}
	function pwdValid() {
		// 비밀번호는 필수, 4~8자, 비밀번호 확인과 동일
		let result =false;
		
		return result;
	}
	
	function idValid() {
		// 아이디는 필수, 4~8자, 아이디는 중복 불가
		let result = false;
		 if ($('#idValid').val() == 'checked') {
		 	result = true;
		 }

		return result;
	}
</script>
<style>
.error {
	color: #990000;
	font-size: .8em;
	padding: 5px;
	border: 1px solid #990000;
	border-radius: 5px;
	margin: 5px 0px;
}
</style>
</head>
<body onload="">
	<c:import url="../header.jsp" />

	<div class="container">
		<h1>회원가입페이지</h1>

		<form method="post" action="/member/register"
			enctype="multipart/form-data">
			<!-- 파일을 보낼 때 enctype -->
			<div class="mb-3 mt-3">
				<label for="userId" class="form-label">아이디: </label> <input
					type="text" class="form-control" id="userId"
					placeholder="아이디를 입력하세요..." name="userId" /> <input type="hidden"
					id="idValid" />
			</div>

			<div class="mb-3 mt-3">
				<label for="userPwd1" class="form-label">패스워드: </label> <input
					type="password" class="form-control" id="userPwd1"
					placeholder="비밀번호를 입력하세요..." name="userPwd" />
			</div>

			<div class="mb-3 mt-3">
				<label for="userPwd1" class="form-label">패스워드 확인: </label> <input
					type="password" class="form-control" id="userPwd2"
					placeholder="비밀번호를 확인하세요..." />
			</div>

			<div class="mb-3 mt-3">
				<label for="userEmail" class="form-label">이메일: </label> <input
					type="text" class="form-control" id="userEmail" name="userEmail" />
			</div>

			<div class="form-check">
				<input type="radio" class="form-check-input" id="radio1"
					name="optradio" value="option1" checked>여성<label
					class="form-check-label" for="radio1"></label>
			</div>
			<div class="form-check">
				<input type="radio" class="form-check-input" id="radio2"
					name="optradio" value="option2">남성<label
					class="form-check-label" for="radio2"></label>
			</div>


			<div class="mb-3 mt-3">
				<label for="mobile" class="form-label">휴대전화: </label> <input
					type="text" class="form-control" id="mobile"
					placeholder="전화번호를 입력하세요..." name="userMobile" />
			</div>

			<div class="mb-3 mt-3">
				<form name="form" id="form" method="post">

					<table>
						<colgroup>
							<col style="width: 20%">
							<col>
						</colgroup>
						<tbody>
							<tr>
								<th>우편번호</th>
								<td><input type="hidden" id="confmKey" name="confmKey"
									value=""> <input type="text" id="zipNo" name="zipNo"
									readonly style="width: 100px"> <input type="button"
									value="주소검색" onclick="goPopup();"></td>
							</tr>
							<tr>
								<th>도로명주소</th>
								<td><input type="text" id="roadAddrPart1"
									style="width: 85%"></td>
							</tr>
							<tr>
								<th>상세주소</th>
								<td><input type="text" id="addrDetail" style="width: 40%"
									value=""> <input type="text" id="roadAddrPart2"
									style="width: 40%" value=""></td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>

			<div class="mb-3 mt-3">
				<label for="memberProfile" class="form-label">회원 프로필: </label> <input
					type="file" class="form-control" id="userProfile"
					name="memberProfile" />

			</div>


			<div class="form-check">
				<input class="form-check-input" type="checkbox" id="agree"
					name="agree" value="Y" /> <label class="form-check-label">회원
					가입 조항에 동의합니다</label>
			</div>

			<!-- form 태그는 항상 submit / reset 버튼과 함께 사용 -->
			<input type="submit" class="btn btn-success" value="회원가입"
				onclick="return isValid();" /> <input type="reset"
				class="btn btn-danger" value="취소" />
		</form>

	</div>

	<c:import url="../footer.jsp" />
</body>
</html>