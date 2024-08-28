use webdiane;

-- 회원 테이블 생성
CREATE TABLE `webdiane`.`member` (
  `userId` VARCHAR(8) NOT NULL,
  `userPwd` VARCHAR(200) NOT NULL,
  `userName` VARCHAR(12) NULL,
  `mobile` VARCHAR(13) NULL,
  `email` VARCHAR(50) NULL,
  `registerDate` DATETIME NULL DEFAULT now(),
  `userImg` VARCHAR(50) NOT NULL DEFAULT 'avatar.png',
  PRIMARY KEY (`userId`),
  UNIQUE INDEX `mobile_UNIQUE` (`mobile` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
COMMENT = '회원테이블';

-- 회원 테이블 수정(회원포인트점수 컬럼 부여)
ALTER TABLE `webdiane`.`member` 
ADD COLUMN `userPoint` INT NULL DEFAULT 100 AFTER `userImg`;

-- DB서버의 현재 날자와 현재 시간을 출력하는 쿼리문
SELECT now();

-- MY SQL의 암호화 기법
SELECT md5('1234');
SELECT sha1('1234');
SELECT md5(sha1('1234'));

-- Member테이블에 회원을 insert하는 쿼리문
INSERT INTO member(userId, userPwd, userName, mobile, email)
VALUES(?, sha1(md5(?)), ?, ?, ?);

-- userId로 해당 유저의 정보를 검색하는 쿼리문 
SELECT * FROM member WHERE userId=?;

-- member테이블에 모든 정보를 검색하는 쿼리문
SELECT * FROM member;

-- dooly회원의 이메일을 수정하는 쿼리문
UPDATE member SET email='dooly@dooly.com' WHERE userId='dooly';

-- ?회원이 전화번호를 변경할 때 쿼리문
update member set mobile=? where userId=?;

-- userId가 ?인 회원 정보 삭제 쿼리문
delete from member where userId=?;

-- 계층형 게시판 생성 퀴리문
select * from hboard order by boardNo desc;

-- 계층형 게시판에 게시글을 등록하는 쿼리문
insert into hboard(title, content, writer) values('일등 놓쳤네', '내용무...0', 'gildong');
insert into hboard(title, content, writer) values(?, ?, ?);

-- 유저에게 지급하는 포인트를 정의한 테이블 생성 쿼리문pointdef
CREATE TABLE `webdiane`.`pointdef` (
  `pointwhy` VARCHAR(20) NOT NULL,
  `pointScore` INT NULL,
  PRIMARY KEY (`pointwhy`))
COMMENT = '유저에게 적립할 포인트에 대해 정의한 테이블\n어떤 사유로 몇 포인트를 지급하는지에 대해 정의';

-- pointdef 수정
ALTER TABLE `webdiane`.`pointdef` 
ADD COLUMN `pointdefNo` INT NOT NULL AUTO_INCREMENT FIRST,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`pointdefNo`);
;

-- pointdef 테이블의 기초데이터
INSERT INTO `webdiane`.`pointdef` (`pointwhy`, `pointScore`) VALUES ('회원가입', '100');
INSERT INTO `webdiane`.`pointdef` (`pointwhy`, `pointScore`) VALUES ('로그인', '1');
INSERT INTO `webdiane`.`pointdef` (`pointwhy`, `pointScore`) VALUES ('글작성', '10');
INSERT INTO `webdiane`.`pointdef` (`pointwhy`, `pointScore`) VALUES ('댓글작성', '2');
INSERT INTO `webdiane`.`pointdef` (`pointwhy`, `pointScore`) VALUES ('게시글신고', '-10');

-- 유저의 포인트 적립내역을 기록하는 pointlog테이블 생성pointlog
CREATE TABLE `webdiane`.`pointlog` (
  `pointLogNo` INT NOT NULL AUTO_INCREMENT,
  `pointWho` VARCHAR(8) NOT NULL,
  `pointWhen` DATETIME NULL DEFAULT now(),
  `pointWhy` VARCHAR(20) NOT NULL,
  `pointScore` INT NOT NULL,
  PRIMARY KEY (`pointLogNo`),
  CONSTRAINT `member_pointlog_who_fk`
    FOREIGN KEY (`pointWho`)
    REFERENCES `webdiane`.`member` (`userId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
COMMENT = '어떤 유저에게 어떤 사유로 몇 포인트가 언제 지급되었는지 기록하는 테이블';

-- 게시판의 글을 삭제하는 쿼리문 
DELETE FROM hboard WHERE boardNo=?;

-- 유저에게 포인트를 지급하는 쿼리문 (회원가입시)
insert into pointlog(pointWho, pointWhy, pointScore) 
values(?, ?,
	(
		select pointScore from pointdef where pointWhy='회원가입'
    )
 );
 
 -- 유저에게 지금된 포인트를 update하는 쿼리문
 update member set userPoint = userPoint + (select pointScore from pointdef where pointwhy='글작성' )where userId=?;
 
 
 -- 게시글의 첨부파일을 저장하는 테이블 생성
 CREATE TABLE `webdiane`.`boardimg` (
  `boardImgNo` INT NOT NULL AUTO_INCREMENT,
  `newFileName` VARCHAR(50) NULL,
  `originalFileName` VARCHAR(50) NULL,
  `ext` VARCHAR(4) NULL,
  `size` INT NOT NULL,
  `boardNo` INT NULL,
  `base64Img` TEXT NULL,
  INDEX `board_boardNo_fk_idx` (`boardNo` ASC) VISIBLE,
  PRIMARY KEY (`boardImgNo`),
  CONSTRAINT `board_boardNo_fk`
    FOREIGN KEY (`boardNo`)
    REFERENCES `webdiane`.`hboard` (`boardNo`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
COMMENT = '게시판에 업로드되는 파일을 기록하는 테이블';

-- 게시글 첨부파일 테이블 수정
ALTER TABLE `webdiane`.`boardimg` 
ADD COLUMN `thumbFileName` VARCHAR(60) NULL AFTER `originalFileName`;

-- 게시글 첨부파일 테이블 이름 변경
ALTER TABLE `webdiane`.`boardimg` 
RENAME TO  `webdiane`.`boardupfiles` ;
-- 게시글 첨부파일 테이블 컬럼명 변경
ALTER TABLE `webdiane`.`boardupfiles` 
CHANGE COLUMN `boardImgNo` `boardUpFileNo` INT NOT NULL AUTO_INCREMENT ;
-- 컬럼 사이즈 변경
ALTER TABLE `webdiane`.`boardupfiles` 
CHANGE COLUMN `ext` `ext` VARCHAR(20) NULL DEFAULT NULL ;

-- 방금 insert된 글의 글번호를 가져오는 쿼리문
select max(boardNo) from hboard;

-- 유저가 게시글을 저장할 때 파일을 업로드하는 쿼리문
insert into boardupfiles(newFileName, originalFileName, thumbFileName, ext, size, boardNo, base64Img)
values (?, ?, ?, ?, ?, ?, ?);

-- 게시판 상세페이지 출력
select * from hboard where boardNo=24;
select * from boardupfiles where boardNo=24;

-- 게시판 상세페이지에서 그 게시글을 작성한 유저의 정보까지 출력
select h.boardNo, h.title, m.userId, m.userName
from hboard as h inner join member as m
on h.writer = m.userId
where h.boardNo=29;

-- 게시글과 첨부파일을 함께 출력해보자
select h.boardNo, h.title, h.content, h.writer, h.postDate, h.readCount,
f.*, m.username, m.email
from hboard as h left outer join boardupfiles f 
on h.boardNo = f.boardNo
inner join member m
on h.writer = m.userId
where h.boardNo=39;

select *
from hboard as h left outer join boardupfiles f 
on h.boardNo = f.boardNo;
-- 오른쪽 테이블의 누락된 정보를 쓰려면 right outer join, 양 테이블의 정보를 모두 가져오려면 full outer join


-- 게시글의 조회수를 증가하는 쿼리문
update hboard set readCount = readCount+1 where boardNo=?;

-- 게시글을 조회할 때 그 내역을 기록하는 쿼리문 " readwho가 ip주소이고, boardNo가 ?이고, 
-- 1. 같은 아이피 주소로 읽은 내역이 있는지 먼저 검사, 
select * from boardreadlog;
select readWhen from boardreadlog where readWho=? and boardNo=?;
-- 2. 위 결과가 null이 면 insert 
insert into boardreadlog(readWho, boardNo) values(?, ?);
-- 3. 위 결과가 null이 아니면 현재날짜와 이전에 읽은 날짜의 차이를 구해야한다 (1번+3번 서브쿼리와 함수이용 )
select ifnull( 
	DATEDIFF(now(),
		(select readWhen from boardreadlog where readWho='0:0:0:0:0:0:0:1' and boardNo=29)
		), -1) as datediff;
-- 4. datediff 값이 1보다 같거나 크면 기존 readDate를 update한다. 
update boardreadlog set readWhen = now() where readWho=? and boardNo=?;


CREATE TABLE `webdiane`.`boardreadlog` (
  `boardReadLogNo` INT NOT NULL AUTO_INCREMENT,
  `readWho` VARCHAR(130) NOT NULL,
  `readWhen` DATETIME NULL DEFAULT now(),
  `boardNo` INT NOT NULL,
  PRIMARY KEY (`boardReadLogNo`))
COMMENT = '게시글 조회내역을 기록하는 테이블';

select * from webdiane.hboard order by ref desc, refOrder asc;
-- 계층형 게시판을 만드는 쿼리문
-- 1) 기존 게시글의 ref컬럼 값을 boardNo로 업데이트(기존의 글들은 모두 부모글이므로)
-- 2) 앞으로 저장될 게시글에도 ref컬럼값을 boardNo로 업데이트
update hboard set ref=? where boardNo=?;
-- 2-1) 부모글에 대한 다른 답글이 있는 상태에서, 부모글에 답글이 추가되는 경우 (자리확보를 위해) 기존 답글의 refOrder값을 수정(+1해서 뒤로 미뤄줌)
update hboard set refOrder=refOrder+1 where ref= ? and refOrder> ? ;
-- 3) 부모글 정보와 답글 정보를 모두 가져와서 저장하는 메서드
insert into hboard(title, content, writer, ref, step, refOrder)
values (?, ?, ?, ?, ?, ?);


-- ============================= 게시글 삭제 작업 ================================
-- hboard 테이블에 삭제한 글을 표현하는(Y/N) 컬럼 추가
alter table `webdiane`.`hboard`
add column `isDelete` char(1) null default 'N' after `refOrder`;

-- 첨부파일이 있다면 서버에서 삭제하기 전에 해당 글의 첨부파일 정보를 불러온다
select * from boardupfiles where boardNo=?;
-- boardupfiles에서 첨부파일을 삭제하는 쿼리문
delete from boardupfiles where boardNo=?;
-- boardNo번 글을 삭제처리 :
-- 	 	delete문을 실행하면 계층형게시판 정렬을 위해 만들어놓은 ref, step, refOrder 컬럼 정보또한 삭제되므로
-- 		실제로는 update문을 실행해야한다... > 삭제처리된 boardNo번 글에 접근하지 못 하도록 한다.
-- 		유저가 입력한 내용(title, content)을 null로 바꾼다
update hboard set isDelete='Y', title='', content='' where boardNo=? ;
-- view단에서 지워진파일에 접근하지 못하도록 함 (isDelete 컬럼활용)

-- =========================== 게시글 수정 작업 ========================================
-- 파일 수정이 문제
-- 수정가능한 column값을 화면에 출력 (select)
select * from hboard where boardNo=?;
-- 게시글 내용을 update 
update hboard set title=?, content=? where boardNo=?;

-- 첨부파일을 pk로 삭제하는 쿼리문 
delete from boardupfiles where boardUpFileNo=?; 

-- 인기글 가져오기
-- 삭제되지 않은 글 중에서 조회수가 높은 순, 최신글 순 5개 가져오기
use webdiane;
select boardNo, title, postDate from hboard where isDelete='N' order by readCount desc, boardNo desc limit 5 ;

-- =============================페이징===============================================
-- 최정적으로 mysql에서 페이징을 위해 필요한 쿼리문 :
select * from webdiane.hboard order by ref desc, refOrder asc limit 보여줄 페이지의 rowIndex번호, 1페이징당 보여줄 글 갯수; 
-- 1) 게시판의 전체 데이터 수를 출력하는 쿼리문
select count(*) from hboard;
-- 2) 한 페이지 당 보여줄 데이터의 갯수가 10이라면, 1)에 나온 결과를 10으로 나누었을 때 몫이 페이지 수 이며, 나누어 떨어지지 않는다면 +1을 한다..
-- 전체페이지 수 = 전체 데이터 수 / 1페이지당 보여줄 글의 갯수 ? 나누어 떨어지면 몫 : 나누어떨어지지 않으면 몫 +1 
-- 3) n번째 글에서 보여주기 시작할 글의 index번호를 구하는 것이 핵심
-- 4) 1페이지 : 0, 2페이지 :10, 3페이지 :20 > (현재 페이지 번호-1) * 1페이지당 보여줄 글의 갯수 > n번째 글에서 보여주기 시작할 글의 index번호

-- 페이징 블럭 만들기(1개 페이징 블럭에서 보여줄 페이지 수 : 10페이지씩)
-- 1) 현재 페이징 블럭에서 출력시작할 페이지 번호 e.g. 7페이지 > 1, 14페이지 >11, 28페이지>21페이지 :: (n/1개 페이징 블럭에서 보여줄 페이지 수) * 1개 페이징 블럭에서 보여줄 페이지 수 +1 
-- 1-2) 현재 페이지가 속한 페이징블럭의 번호
-- 현재 페이지번호 / 1개의 페이징 블럭에서 보여줄 페이질 수 > 가 나누어떨어지지 않으면 올림, 나누어 떨어지면 그 값으로 함
-- 2) 현재 페이징 블럭에서 출력 시작할 페이지번호 
-- (현재 페이징 블럭번호 -1) * 1개 페이징 블럭에서 보여줄 페이지 수 + 1
-- 2) 페이징 블럭에서 마지막에 나올 번호 : 1)에서 나온 값 + 1개 페이징 블럭에서 보여줄 페이지 수 -1

-- ===============================검색기능=============================================
use webdiane;
-- 제목으로 검색
-- like 검색과 함께 사용하는 와일드카드 
-- % : 몇자라도, 
-- '_' : 한 글자
select * from hboard where title like '%data%' ;
-- 작성자로 검색
select * from hboard where writer like '%do%' order by ref desc, refOrder asc limit 0, 10;
-- 내용으로 검색
select * from hboard where content like '%kil%' order by ref desc, refOrder asc ;
-- 검색어가 있을 때 게시물의 데이터 수를 얻어오는 쿼리문
select count(*) from hboard where title like '%data%';
select count(*) from hboard where writer like '%do%' ;
select count(*) from hboard where content like '%123%';

-- ================================회원가입기능구현==================================
-- 회원아이디 중복 여부 ( 
select count(*) from member where userId ='dooly';
-- 취미 배열 추가
ALTER TABLE `webdiane`.`member` 
ADD COLUMN `hobby` VARCHAR(60) NULL AFTER `email`;

-- member테이블에 회원가입내용 저장
use webdiane;
-- 프로필 파일 올렸을 때(userImg!=null)
insert into member(userId, userPwd, userName, gender, mobile, email, hobby, userImg)
values(?, ?, ?, ?, ?, ?, ?, ?);
-- 프로필 파일을 올리지 않았을 때(userImg = null)
insert into member(userId, userPwd, userName, gender, mobile, email, hobby)
values(?, ?, ?, ?, ?, ?, ?);

-- ============================로그인 ==============================================
use webdiane;
select * from hboard;

-- ?번 글의 작성자(userId) 얻어오는 쿼리문
select writer from hboard where boardNo = 940;
-- 자동로그인 기능 구현 : 자동로그인을 체크한 경우 아래 두 컬럼에 세션값과 만료일을 저장 향후에 쿠키에 있는 자동로그인 정보와 DB의 아래 컬럼에 있는 자동로그인 정보가 일치하면 자동로그인을 실행한다
ALTER TABLE `webdiane`.`member` 
ADD COLUMN `sesid` VARCHAR(40) NULL AFTER `userPoint`,
ADD COLUMN `allimit` DATETIME NULL AFTER `sesid`;

-- 자동로그인 정보 저장
update member set sesid=? , allimit=? where userId=?;
-- 쿠키에 자동로그인 체크정보가 저장되어있을 때 자동로그인하는 쿼리문
select * from member where sesid=? and allimit > now();

-- ===========계층형게시판과 댓글형게시판의 테이블을 함께 사용하기 위해 게시판을 구분하는 용도의 컬럼추가 =============
use webdiane;
ALTER TABLE `webdiane`.`hboard` 
ADD COLUMN `boardType` VARCHAR(10) NULL AFTER `isDelete`;
-- 기존의 글들을 계층형게시판 타입의 글로 update 처리
update hboard set boardType='hboard' where boardNo<947;
select * from hboard;
-- 계층형게시판 = 'hboard', 댓글형게시판 = 'rboard'
-- boardreadlog테이블에 boardType 컬럼 추가
ALTER TABLE `webdiane`.`boardreadlog` 
ADD COLUMN `boardType` VARCHAR(10) NULL AFTER `boardNo`;
-- 게시글 content 사이즈 변경
ALTER TABLE `webdiane`.`hboard` 
CHANGE COLUMN `content` `content` LONGTEXT NULL DEFAULT NULL ;

select h.boardNo, h.title, h.content, 
		h.writer,
		h.postDate, h.readCount, h.isDelete,
		m.username, m.email
		from hboard h
		inner join member m
		on h.writer = m.userId
		where h.boardNo=951 and boardType='rboard';
        
-- boardreadlog테이블의 boardType컬럼 삭제
ALTER TABLE `webdiane`.`boardreadlog` 
DROP COLUMN `boardType`;
select * from hboard limit 5;
-- -------------------- 댓글기능구현 ---------------------
use webdiane;
select * from hboard where boardType='rboard' order by boardNo desc;
-- 하나의 게시글에 여러개의 댓글 > 일대다 관계
CREATE TABLE `webdiane`.`replyboard` (
  `replyNo` INT NOT NULL AUTO_INCREMENT,
  `replyer` VARCHAR(8) NULL,
  `content` VARCHAR(200) NULL,
  `regDate` DATETIME NULL DEFAULT now(),
  `boardNo` INT NOT NULL,
  PRIMARY KEY (`replyNo`))
COMMENT = '댓글을 저장하는 테이블';
-- replyboard FK설정
-- 수정테이블, 제약조건 이름, 참조테이블 및 컬럼
alter table replyboard
add constraint replyer_member_fk foreign key (replyer) references member(userId)
on delete cascade;
alter table replyboard
add constraint boardNo_hboard_fk foreign key (boardNo) references hboard(boardNo);

-- 댓글 등록해보기..
insert into replyboard (replyer, content, boardNo)
values ('gildong2', '분 표시하는 기능 테스트!!', 949);

-- ~번 글에 대한 모든 댓글을 얻어오는 쿼리문
select * from replyboard where boardNo=952;

-- ?번 글에 대한 게시글과 모든 댓글을 얻어오는 쿼리문
select * from hboard h inner join replyboard r on h.boardNo = r.boardNo where h.boardNo=952 and boardType='rboard';
-- 조인문은 from부터 쓰기 시작하기...
-- inner join에서... on 다음에는'=' 이퀄 조건으로 쓸 수 있는 것, 그 밖의 조건은 where
-- outer join에서... 한쪽 테이블(left/right outer)에 누락된 데이터가 있을 때 

-- ?번 글의 댓글 얻어오기
select * from replyboard where boardNo=951;

-- 댓글갯수 얻어오기
select count(*) from replyboard where boardNo=?;
-- 댓글 페이징을 위한 쿼리문 
select * from replyboard where boardNo=949 order by replyNo limit 1, 5;

-- 모든 댓글 + 댓글 작성자의 프로필
select r.*, m.userImg, m.userName, m.email from replyboard r 
inner join member m
on r.replyer = m.userId
where boardNo=949 ;

-- 수정 댓글 
update replyboard set content=? , regDate=now() where replyNo=?;

-- ?번 댓글 삭제
delete from replyboard where replyNo=?;


use webdiane;
select * from member;
commit;

-- 계정 비활성화 기능을 위한 'islock'컬럼 추가
ALTER TABLE `webdiane`.`member` 
ADD COLUMN `islock` VARCHAR(1) NULL DEFAULT 'N' AFTER `allimit`;

-- 계정을 비활성화하는 쿼리문
update member set islock ='Y' where userId='doolyk';