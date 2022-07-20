

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