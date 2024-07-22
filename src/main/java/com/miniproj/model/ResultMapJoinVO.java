package com.miniproj.model;

import java.sql.Timestamp;

import lombok.Data;

//select h.boardNo, h.title, h.content, h.writer, h.postDate, h.readCount,
//f.*, m.username, m.email
//from hboard as h left outer join boardupfiles f 
//on h.boardNo = f.boardNo
//inner join member m
//on h.writer = m.userId
//where h.boardNo=24;


public class ResultMapJoinVO {
		
	@Data
	public class hboard {
		private int boardNo ;
		private String title;
		private String content;
		private String writer;
		private Timestamp postDate ;
		private int readCount;
	
	}
	
	@Data
	public class boardupfiles {
		
		private int boardUpFileNo ;
		private String newFileName;
		private String originalFileName;
		private String thumbFileName;
		private int ext;
		private int size;
		private String boardNo;
		private String base64Img;
	
	}
	
	@Data
	public class member {
		
		private String userName;
		private String email;

	}
	
	
}