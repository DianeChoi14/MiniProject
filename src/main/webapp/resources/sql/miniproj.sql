use webdiane

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
