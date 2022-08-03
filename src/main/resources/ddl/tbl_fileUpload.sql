-- 첨부파일 정보를 가지는 테이블 생성
CREATE TABLE file_upload (
    file_name VARCHAR2(150), -- /2022/08/01/fsfsawfwefwfw_상서.jsp 파일 경로 = 절대 안겹침(파일)
    reg_date DATE DEFAULT SYSDATE, -- origin file_name, file_size, extention(확장자), 만료기간 정보등 저장하는게 좋음
    bno NUMBER(10) NOT NULL
);

-- PK, FK 부여
ALTER TABLE file_upload
ADD CONSTRAINT pk_file_name
PRIMARY KEY (file_name);

ALTER TABLE file_upload
ADD CONSTRAINT fk_file_upload
FOREIGN KEY (bno)
REFERENCES tbl_board (board_no)
ON DELETE CASCADE; -- 게시글 지워지면 같이 사라진다