package com.miniproj.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class SendMailService {
	private String username = "miseol93";
	private String password = "BVHZLD9GW9M7"; // 이후에 properties파일로 저장하여 git에 올리지 않기
	
	public void sendMail(String emailAddr, String activationCode) throws AddressException, MessagingException {
		String subject = "miniproject.com에서 보내는 회원가입 이메일 인증번호 입니다.";
		String message = "회원가입을 환영합니다.. \n 인증번호 : " + activationCode + "를 입력하시고 회원가입을 완료하세요.";
	
		// naver 이메일서버의 메일 환경 설정
		// Properties객체 : 부모가 Map, key와 value로 데이터를 관리, 파일로 저장하거나 읽어올 수 있다
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.naver.com");  // smtp 호스트 주소 등록
		props.put("mail.smtp.port", "465"); // naver smtp의 포트번호
		props.put("mail.smtp.starttls.required", "true"); // 동기식 전송을 위해 설정
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		props.put("mail.smtp.ssl.enable", "true");  // SSL 사용	
		props.put("mail.smtp.auth", "true"); // 인증 과정을 거치겠다.
		
		Session mailSession = Session.getInstance(props, new Authenticator() {
			
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		}); // 통신이 연결됐을 때 
		System.out.println(mailSession.toString());
		if(mailSession!=null) {
			MimeMessage mime = new MimeMessage(mailSession);
			mime.setFrom(new InternetAddress("miseol93@naver.com")); // 보내는사람 메일주소
			mime.addRecipient(RecipientType.TO, new InternetAddress(emailAddr));
			// 메일 제목과 본문설정
			mime.setSubject(subject);
			mime.setText(message);
			
			Transport trans = mailSession.getTransport("smtp");
			trans.connect(username, password);
			trans.send(mime);
			trans.close();
		}
	}	
}
