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
public class ReplyVO {
	private int replyNo;
	private String replyer;
	private String content;
	private Timestamp regDate;
	private int boardNo;
}
