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
where h.boardNo=24;

-- 게시글과 첨부파일을 함께 출력해보자
select h.boardNo, h.title, h.content, h.writer, h.postDate, h.readCount,
f.*, m.username, m.email
from hboard as h left outer join boardupfiles f 
on h.boardNo = f.boardNo
inner join member m
on h.writer = m.userId
where h.boardNo=24;

select *
from hboard as h left outer join boardupfiles f 
on h.boardNo = f.boardNo;
-- 오른쪽 테이블의 누락된 정보를 쓰려면 right outer join, 양 테이블의 정보를 모두 가져오려면 full outer join


