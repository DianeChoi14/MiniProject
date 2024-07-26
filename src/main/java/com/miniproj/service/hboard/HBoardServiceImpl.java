package com.miniproj.service.hboard;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.miniproj.controller.HomeController;
import com.miniproj.model.BoardDetailInfo;
import com.miniproj.model.BoardUpFileStatus;
import com.miniproj.model.BoardUpFilesVODTO;
import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardVO;
import com.miniproj.model.HReplyBoardDTO;
import com.miniproj.model.PointLogDTO;
import com.miniproj.persistence.HBoardDAO;
import com.miniproj.persistence.MemberDAO;
import com.miniproj.persistence.PointLogDAO;

////Service단에서 해야할 작업
//1) Controller단에서 넘겨진 파라미터를 처리한 후(비즈니스 로직에 의해(트랜잭션처리를 통해))
//2) DB작업이라면 DAO단 호출
//3) DAO단에서 반환된 값을 Controller단으로 넘겨

@Service // 아래의 클래스가 서비스객체임을 컴파일러에 고지
public class HBoardServiceImpl implements HBoardService {

	@Autowired
	private HBoardDAO bDao;

	@Autowired
	private PointLogDAO pDao;

	@Autowired
	private MemberDAO mDao;

	@Override
	@Transactional(readOnly = true) // 트랜젝션이 발동하지 않음
	public List<HBoardVO> getAllBoard() throws Exception {
		// logger.info("HBoardServiceImpl...............");
		System.out.println("HBoardServiceImpl...............");

		List<HBoardVO> lst = bDao.selectAllBoard(); // DAO단 호출

		return lst;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class) // 실패하면
																														// 예외처리된다
	public boolean saveBoard(HBoardDTO newBoard) throws Exception {
		boolean result = false;
//		1. newBoard를 DAO단을 통해 insert해본다 
		if (bDao.insertNewBoard(newBoard) == 1) {
//			1-1. 위에서 저장된 게시글의 pk(boardNo)를 가져와야한다(select)
			int newBoardNo = bDao.getMaxBoardNo();

//			1-1-1 위에서 가져온 글 번호를 ref컬럼에 update
			bDao.updateBoardRef(newBoardNo);

//			1-2. 첨부된 파일이 있다면 첨부파일 또한 저장한다..(insert)
			for (BoardUpFilesVODTO file : newBoard.getFileList()) {
				file.setBoardNo(newBoardNo);
				bDao.insertBoardUpFile(file);
			}

//			2. 1이 성공했을 때 글작성자의 포인트를 부여한다. (select+insert)
			if (pDao.insertPointLog(new PointLogDTO(newBoard.getWriter(), "글작성", 0)) == 1) {
//				3. 작성자의 userPoint값을 update
				if (mDao.updateUserPoint(newBoard.getWriter()) == 1) {
					result = true;
				}
			}
		}

		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public List<BoardDetailInfo> read(int boardNo, String ipAddr) throws Exception {

		List<BoardDetailInfo> boardInfo = bDao.selectBoardByBoardNo(boardNo);

		for(BoardDetailInfo b : boardInfo) {
			System.out.println(b.toString());
		}
		for(int i=0 ; i < boardInfo.size() ; i++) {
			System.out.println(i+"번째"+ boardInfo.get(i).toString());
		}

		// 조회수 증가 > 글을 읽어오기 전에 조회수를 가져와서 글을 가져왔을 경우 조회수를 1 늘린다.
		if (boardInfo != null) {
			// ipAddr유저가 boardNo글을 조회한 적이 없다.
			if (bDao.selectDateDiff(boardNo, ipAddr) == -1) {
				if (bDao.saveBoardReadLog(boardNo, ipAddr) == 1) { // 조회내역 저장
					updateReadCount(boardNo, boardInfo);
				}
			} else if (bDao.selectDateDiff(boardNo, ipAddr) >= 1) {
				updateReadCount(boardNo, boardInfo);
				bDao.updateReadWhen(boardNo, ipAddr);
			}

		}
		return boardInfo;

	}
	
	@Override
	@Transactional(readOnly=true, rollbackFor=Exception.class)
	public List<BoardDetailInfo> read(int boardNo) throws Exception {
		List<BoardDetailInfo> boardInfo = bDao.selectBoardByBoardNo(boardNo);
		return boardInfo;
	}

	private void updateReadCount(int boardNo, List<BoardDetailInfo> boardInfo) {
		if (bDao.updateReadCount(boardNo) == 1) {
			for (BoardDetailInfo b : boardInfo) {
				b.setReadCount(b.getReadCount() + 1);
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public boolean saveReply(HReplyBoardDTO replyBoard) throws Exception {
		boolean result = false;
		// 부모글에 대한 다른 답글이 있는 상태에서, 부모글에 답글이 추가되는 경우 (자리확보를 위해) 기존 답글의 refOrder값을 수정
		bDao.updateBoardRef(replyBoard.getRef(), replyBoard.getRefOrder());

		// 1) 부모글의 boardNo를 ref값으로, 부모글의 step+1을 step에, 부모글의 refOrder+1값을 refOrder에 저장 +
		// 답글 데이터
		replyBoard.setStep(replyBoard.getStep() + 1);
		replyBoard.setRefOrder(replyBoard.getRefOrder() + 1);

		if (bDao.insertReplyBoard(replyBoard) == 1) {
			result = true;
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public List<BoardUpFilesVODTO> removeBoard(int boardNo) throws Exception {
		// 1) boardNo번 글의 첨부파일이 있다면 서버에서 삭제하기 전에 해당 글의 첨부파일 정보를 불러온다 > 여러개일 수도 있으므로 리스트에
		// 담는다.
		List<BoardUpFilesVODTO> fileList = bDao.selectBoardUpFiles(boardNo); // select

		// 2) boardNo번 글의 첨부파일이 있다면 첨부파일을 삭제해야한다.
		bDao.deleteAllBoardUpFiles(boardNo); // delete

		// 3) boardNo번 글을 삭제처리
		if (bDao.deleteBoardByBoardNo(boardNo) == 1) { // update
			return fileList;
		} else {
			return null; // 리스트객체가 생성되어 반환되기 때문에 첨부파일이 없어도 빈 리스트 객체를 반환함 > null이 아니다!
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public boolean modifyBoard(HBoardDTO modifyBoard) throws Exception {
		boolean result = false;
		// 1) 게시글 내용 update
		if(bDao.updateBoardbyBoardNo(modifyBoard)==1) {
			// 2) 첨부파일의 status가 INSERT면 insert, DELETE면 delete
			List<BoardUpFilesVODTO> fileList = modifyBoard.getFileList();
			for(BoardUpFilesVODTO file : fileList) {
				if(file.getFileStatus()==BoardUpFileStatus.INSERT) {
					file.setBoardNo(modifyBoard.getBoardNo()); // 저장되는 파일에 글번호를 수정되는 글의 글 번호로 세팅
					bDao.insertBoardUpFile(file);
				} else if (file.getFileStatus()==BoardUpFileStatus.DELETE) {
					bDao.deleteBoardUpFiles(file.getBoardUpFileNo());
				}
					
			}
			result = true;
		}
		return result;
	}



}
