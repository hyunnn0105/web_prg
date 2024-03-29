package com.project.web_prg.board.repository;

import com.project.web_prg.board.common.paging.Page;
import com.project.web_prg.board.mybatis.domain.Board;

import java.util.List;

public interface BoardRepository {
    // 게시글 쓰기 기능
    // sql -> insert ino
     boolean save(Board board);
     
     // 게시글 전체 조회
    List<Board> findAll();

    List<Board>findAll(Page page);
    
    // 게시글 상세 조회 -> pk
    Board findOne(Long boardNo);
    
    boolean remove(Long boardNo);
    // 여러 정보 받기
    boolean modify(Board board);

    // 전체 게시물 수 조회
    int getTotalCount();

    // 조회수 상승 처리
    void upViewCount(Long boardNo);
}
