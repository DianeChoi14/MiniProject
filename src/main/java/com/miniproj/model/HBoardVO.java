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
public class HBoardVO
{
	private int boardNo;
	private String title;
	private String content;
	private String writer;
	private Timestamp postDate;
	private int readCount;
	private int ref ;
	private int step ;
	private int refOrder ;
}
