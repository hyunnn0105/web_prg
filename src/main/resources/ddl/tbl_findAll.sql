-- tbl_board. * tbl_board에 있는 모든 칼럼

SELECT ROWNUM, tbl_board. *
FROM tbl_board
WHERE ROWNUM BETWEEN 1 AND 20
ORDER BY board_no DESC
;

-- 먼저 역정렬을 하고 뽑아야함
-- 서브쿼리 안쪽에서 정렬되야함

-- 안쪽 ROWNUM(rn)에서 걸러내기
SELECT *
FROM(SELECT ROWNUM rn, v_board. *
    --ROWNUM 배치
    FROM (
    -- (역)정렬
        SELECT
        * FROM tbl_board
        ORDER BY board_no DESC
    ) v_board)
WHERE rn BETWEEN 21 AND 40
;