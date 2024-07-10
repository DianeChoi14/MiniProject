package com.miniproj.service.hboard;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniproj.controller.HomeController;
import com.miniproj.model.HBoardVO;
import com.miniproj.persistence.HBoardDAO;

@Service // 아래의 클래스가 서비스객체임을 컴파일러에 고지
public class HBoardServiceImpl implements HBoardService 
{
	@Autowired
	private HBoardDAO bDao;
	
	@Override
	public List<HBoardVO> getAllBoard() throws Exception 
	{
		//logger.info("HBoardServiceImpl...............");
		System.out.println("HBoardServiceImpl...............");
	
		List<HBoardVO> lst = bDao.selectAllBoard(); // DAO단 호출
		
		return lst;
	}

}
