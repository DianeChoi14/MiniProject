package com.miniproj.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class MemberVO {
	private String userId;
	private String userPwd;
	private String userName;
	private String gender;	
	private String mobile;
	private String email;
	private Timestamp registerDate;
	private String userImg;
	private int userPoint;
}
