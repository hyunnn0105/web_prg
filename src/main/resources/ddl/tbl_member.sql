-- 회원 관리 테이블
CREATE TABLE tbl_member (
    account VARCHAR2(50),
    password VARCHAR2(150) NOT NULL,
    -- password DB저장은 평문으로 X -> 암호화해서 저장
    -- => 암호화하면 길이가 완전 길어지니까 길게 잡음
    name VARCHAR2(50) NOT NULL,
    email VARCHAR2(100) NOT NULL UNIQUE,
    -- account == email을 동일하게 사용하는 추세?
    auth VARCHAR2(20) DEFAULT 'COMMON',
    reg_date DATE DEFAULT SYSDATE,
    CONSTRAINT pk_member PRIMARY KEY (account)
);

-- 실무
-- 로그인 이력 테이블 넣기 회원(1):로그인이력(다)
-- 마지막으로 로그인 한 정보

SELECT * FROM tbl_member;

-- 자동로그인 DB
ALTER TABLE tbl_member ADD session_id VARCHAR2(200) DEFAULT 'none';
ALTER TABLE tbl_member ADD limit_time DATE;

commit;