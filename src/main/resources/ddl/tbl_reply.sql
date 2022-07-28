CREATE SEQUENCE seq_tbl_reply;

CREATE TABLE tbl_reply (
    reply_no NUMBER(10),
    reply_text VARCHAR2(1000) NOT NULL,
    reply_writer VARCHAR2(50) NOT NULL,
    reply_date DATE default SYSDATE,
    board_no NUMBER(10),
    CONSTRAINT pk_reply PRIMARY KEY (reply_no),
    CONSTRAINT fk_reply_board
    FOREIGN KEY (board_no)
    REFERENCES tbl_board (board_no)
);

SELECT * FROM tbl_reply;

SELECT * FROM tbl_board
WHERE board_no = 300;

SELECT * FROM tbl_reply
WHERE board_no = 300;

commit;