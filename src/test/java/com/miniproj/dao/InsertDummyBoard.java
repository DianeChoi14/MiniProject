package com.miniproj.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.miniproj.model.HBoardDTO;
import com.miniproj.persistence.HBoardDAO;
import com.miniproj.service.hboard.HBoardService;

@RunWith(SpringJUnit4ClassRunner.class)// test패키지에서 SpringContainer에 접근할 수 있도록 함
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"}) // 파일의 위치를 제공하여 파일에 접근할 수 있도록
public class InsertDummyBoard {
	@Autowired
	private HBoardDAO dao;
	
	@Autowired
	private HBoardService service;
	
	
	@Test
	public void insertDummyDataToBoard() throws Exception {
		
		for(int i= 0 ; i<300; i++)  {
			HBoardDTO dto = HBoardDTO.builder()
				.title("dummy data"+i+"...")
				.content("dummy content" + i)
				.writer("dooly")
				.build();
		
			if (dao.insertNewBoard(dto) == 1) {
//				1-1. 위에서 저장된 게시글의 pk(boardNo)를 가져와야한다(select)
				int newBoardNo = dao.getMaxBoardNo();

//				1-1-1 위에서 가져온 글 번호를 ref컬럼에 update
				dao.updateBoardRef(newBoardNo);
			}
		}
		
	}
}
