package com.project.web_prg.board.repository;

import com.project.web_prg.board.domain.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Log4j2
@RequiredArgsConstructor // autowired
public class BoardRepositoryImpl implements BoardRepository{

    private final JdbcTemplate template;

    /*
    @Autowired
    public BoardRepositoryImpl(JdbcTemplate template) {
        this.template = template;
    }

     */

    @Override
    public boolean save(Board board) {
        String sql = "INSERT INTO tbl_board (board_no, writer, title, content) VALUES (seq_tbl_board.nextVal, ?,?,?)";
        log.info("save process with jdbc - {}", board);
        return template.update(sql, board.getWriter(), board.getTitle(), board.getContent()) == 1;
    }

    @Override
    public List<Board> findAll() {
        String sql = "SELECT * FROM tbl_board ORDER BY board_no DESC";
        return template.query(sql,(rs,rn) -> new Board(rs));
    }

    @Override
    public Board findOne(Long boardNo) {
        String sql = "SELECT * FROM tbl_board WHERE board_no=?";
        return template.queryForObject(sql, (rs,rn)-> new Board(rs),boardNo);
    }

    @Override
    public boolean remove(Long boardNo) {
        String sql = "DELETE FROM tbl_board WHERE board_no=?";
        return template.update(sql, boardNo)==1;
    }

    @Override
    public boolean modify(Board board) {
        String sql = "UPDATE tbl_board SET writer=?, title=?, content=? WHERE board_no=?";
        return template.update(sql, board.getWriter(), board.getTitle(), board.getContent(),board.getBoardNo())==1;
    }

    @Override
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) as cnt FROM tbl_board";
        // 단건조회 -> QFO / 결과값이 한줄=QFO or 여러줄 query
        // return template.query(sql,(rs,rn) -> new Board(rs)); board에 묶어주는 역할!
        // return template.queryForObject(sql, Integer.class); Integer로 결과값을 묶나..?

        return template.queryForObject(sql, Integer.class);
    }

    @Override
    public void upViewCount(Long boardNo) {
        String sql = "UPDATE tbl_board SET view_cnt = view_cnt + 1 WHERE board_no=?";
        template.update(sql, boardNo);
    }

}
