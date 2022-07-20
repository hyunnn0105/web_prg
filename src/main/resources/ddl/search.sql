

SELECT  *
FROM (SELECT ROWNUM rn, v_board.*
-- 로우넘
        FROM (
-- 정렬 + 검색결과 미리 걸러냄 / OR이 + /AND *
                SELECT *
                FROM tbl_board
                WHERE title LIKE '%30%'
                ORDER BY board_no DESC
                ) v_board)
WHERE rn BETWEEN 1 AND 10

--                 WHERE title LIKE '%길동%' / OR content LIKE '%길동%' 제목 + 이름 검색

--- spl backup!

CREATE SEQUENCE seq_product;
DROP SEQUENCE seq_product;

DROP TABLE tbl_product;
CREATE TABLE tbl_product (
    serial_no VARCHAR2(100) PRIMARY KEY
    , product_name VARCHAR2(100) NOT NULL
    , price NUMBER(8) NOT NULL
    , made_at DATE DEFAULT SYSDATE
);

INSERT INTO tbl_product
    (serial_no, product_name, price)
VALUES (TO_CHAR(SYSDATE, 'YYYYMMDD') || LPAD(seq_product.nextval, 4, '0'), 'USB DISK 16GB', 10000);

commit;
ROLLBACK;

SELECT * FROM tbl_product;
SELECT * FROM tbl_score;
SELECT * FROM tbl_board;

DELETE FROM tbl_board WHERE board_no= 311;

SELECT  * FROM
(SELECT ROWNUM rn, v_board.* FROM (
                SELECT *
                FROM tbl_board
                WHERE title LIKE '%3%'
                ORDER BY board_no DESC
                )
                v_board)
WHERE rn BETWEEN 1 AND 10;