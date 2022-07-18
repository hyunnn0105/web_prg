package com.project.web_prg.board.domain;

import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

//TBlBoard - sql cammer case로 바꿔서 이름 정해줌
// wapper -> Long == 0

// 기본 생성자 6개 무조건!! 데이터 넣는 곳에서 넣어주기
@Setter @Getter @ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
public class Board {
    private Long boardNo;
    private String writer;
    private String title;
    private String content;
    private Long viewCnt;
    // java.util
    private Date regDate;

    public Board(ResultSet rs) throws SQLException {
        this.boardNo = rs.getLong("board_no");
        this.writer = rs.getString("writer");
        this.title = rs.getString("title");
        this.content = rs.getString("content");
        this.viewCnt = rs.getLong("view_cnt");
        this.regDate = rs.getDate("reg_date");

    }
}
