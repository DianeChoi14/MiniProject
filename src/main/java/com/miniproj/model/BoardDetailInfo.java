package com.miniproj.model;

import java.sql.Timestamp;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class BoardDetailInfo {
//	select h.boardNo, h.title, h.content, h.writer, h.postDate, h.readCount,
//	f.*, m.username, m.email
//	from hboard as h left outer join boardupfiles f 
//	on h.boardNo = f.boardNo
//	inner join member m
//	on h.writer = m.userId
//	where h.boardNo=24;
	
	private int boardNo;
	private String title;
	private String content;
	private String writer;
	private Timestamp postDate;
	private int readCount;
	
//	private int boardUpFileNo;
//	private String newFileName;
//	private String originalFileName;
//	private String thumbFileName;
//	private String ext;
//	private long size;
//	private String base64Img;
	private List<BoardUpFilesVODTO> fileList;

	private String userName;
	private String email;
	
	
}
