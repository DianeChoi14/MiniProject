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
	private String[] hobby;
	private String hobbies;
	private Timestamp registerDate;
	private String userImg;
	private int userPoint;
	private String sesid;
	private Timestamp allimit;
	private String islock;
	
	
}
