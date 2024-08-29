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
<script type="text/javascript">
	$(function() {
		// window.onload()와 같다
		showBasicInfo();
		
		$('#msgContent').keyup(function(evt){
			let val = $(this).val();
			
			
			$('.curLength').html(val.length);
			
			if(evt.keyCode == 13) {
				
				alert(val);
			}
			
		});
	});

	function showBasicInfo() {
		$('.basicInfo').show();
		$('.pointInfo').hide();
		$('.myBoard').hide();
		$('.message').hide();
	}

	function showPointInfo() {
		$('.basicInfo').hide();
		$('.pointInfo').show();
		$('.myBoard').hide();
		$('.message').hide();
	}

	function showMyBoard() {
		$('.basicInfo').hide();
		$('.pointInfo').hide();
		$('.myBoard').show();
		$('.message').hide();
	}

	function showMessage() {
		$('.basicInfo').hide();
		$('.pointInfo').hide();
		$('.myBoard').hide();
		$('.message').show();
		
		getReceiveUsers();
		getReceivedMessage();
	}
	
	function getReceivedMessage(){
		// 나에게 도착한 쪽지를 가져 옴
		$.ajax({
			url : '/message/receive/' + '${sessionScope.loginMember.userId}', 	
			type : 'get', 
			dataType : 'json',									
			async : false, 
			success : function(data) { 	
				console.log(data);
				if(data.resultCode==200){
					outputMessage(data);
				}
			},
			error : function(data) {
				console.log(data);
			}
		});
	}
	
	function outputMessage(data) {
		let output = `<table class="table table-striped">`;
		output += `<thead><tr><th>#</th><th>보낸사람</th><th>내용</th><th>시간</th></tr></thead>`;
		output += `<tbody>`;
		
		$.each(data.data, function(i, msg){
			output += `<tr>`;
			output += `<td>\${msg.msgId}</td>`;
			output += `<td>\${msg.sender}</td>`;
			output += `<td>\${msg.msgContent}</td>`;
			output += `<td>\${msg.msgWrittenDate}</td>`;
			output += `</tr>`;			
		});

		output += `</tbody></table>`;
	    
		$('.outputMsgArea').html(output)
	}
	
	function getReceiveUsers() {
		// 쪽지를 보낼 수 있는 유저의 목록을 가져옴
		$.ajax({
			url : '/message/getFriends/' + '${sessionScope.loginMember.userId}', 	
			type : 'get', 
			dataType : 'json',									
			async : false, 
			success : function(data) { 	
				console.log(data);
				makeReceiveUserList(data);
				
			},
			error : function(data) {
				console.log(data);
			}
		});
	}
	
	function makeReceiveUserList(data) {
		let output = "";
		$.each(data.data, function(i, friend){
			output += `<option value='\${friend.friendId}'>\${friend.friendName}</option>`;
		});
		$('#receiveUser').html(output);
		
	}
	
	function send() {
		let sender = '${sessionScope.loginMember.userId}';
		let receiver = $('#receiveUser').val();
		let msgContent = $('#msgContent').val();
		let message = {
				"sender" : sender,
				"receiver" : receiver,
				"msgContent" : msgContent
		}
		
		console.log(message);
		
		$.ajax({
			url : '/message/send', 	
			type : 'post', 
			data : JSON.stringify(message),
			dataType : 'json',				
			headers : {
				'Content-Type' : 'application/json'
			},
			async : false, 
			success : function(data) { 	
				console.log(data);
				if(data.resultCode==200) {
					alert("쪽지 발송 성공");
				}
			},
			error : function(data) {
				console.log(data);
			}
		});
	}
</script>
<style>
.msgInputArea {
	flex: 1;
	margin-top: 10px;
	padding: 10px;
	display: flex;
	flex-direction: row;
	align-items: center;
	border: 1px solid #dee2e6;
	border-radius: 0.375rem;
}

.msgInputArea input {
	width: 80%;
}

.msgInputArea img {
	margin-left: 5px;
	border: 2px solid rgba(0, 0, 255, 0.4);
	border-radius: 5px;
}
</style>
</head>
<body>
	<div class="container">
		<jsp:include page="../header.jsp"></jsp:include>


		<div class="content">
			<h1>My Page</h1>
			<ul class="nav nav-tabs">
				<li class="nav-item"><a href="#" class="nav-link"
					onclick="showBasicInfo();">회원 기본 정보</a>
					<div class="basicInfo">
						<h4>회원 기본 정보</h4>
						<div>${memberInfo }</div>

					</div></li>
				<li class="nav-item"><a href="#" class="nav-link"
					onclick="showPointInfo();">포인트 내역</a>
					<div class="pointInfo">포인트내역</div></li>

				<li class="nav-item"><a href="#" class="nav-link"
					onclick="showMyBoard();">내가 쓴 글</a>
					<div class="myBoard">내가 쓴 글</div></li>
				<li class="nav-item"><a href="#" class="nav-link"
					onclick="showMessage();">쪽지</a>
					<div class="message">
						<div class="outputMsgArea">
						</div>
						
						<div class="msgInputArea">
						<div class="msgContentLength"><span class="curLength"></span> / 100 </div>
						<div class="msgInputBody">
							<select id="receiveUser">
								<!-- 동적 바인딩 :  -->
							</select> 
							<input type="text" class="form-control" id="msgContent" placeholder="메시지 내용..." />
							<img src="/resources/images/sendMessage.png" onclick="send()" />
						</div>		
						</div>
					</div></li>
			</ul>
		</div>

		<jsp:include page="../footer.jsp"></jsp:include>
	</div>
</body>
</html>