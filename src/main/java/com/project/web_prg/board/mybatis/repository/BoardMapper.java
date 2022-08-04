package com.project.web_prg.board.mybatis.repository;

import com.project.web_prg.board.common.paging.Page;
import com.project.web_prg.board.common.search.Search;
import com.project.web_prg.board.mybatis.domain.Board;
import com.project.web_prg.board.mybatis.dto.ValridateMemberDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface BoardMapper {
    // 게시글 쓰기 기능
    // sql -> insert ino
     boolean save(Board board);
     
     // 게시글 전체 조회 with search
//    List<Board> findAll();

    List<Board>findAll(Page page);
    List<Board>findAll2(Search search);

    // 게시글 상세 조회 -> pk
    Board findOne(Long boardNo);
    
    boolean remove(Long boardNo);
    // 여러 정보 받기
    boolean modify(Board board);

    // 전체 게시물 수 조회
    int getTotalCount();
    int getTotalCount2(Search search);

    // 조회수 상승 처리
    void upViewCount(Long boardNo);

    // 파일 첨부 기능 처리
    void addFile(String fileName);

    // 게시물에 붙어있는 첨부파일결로명 전부 조회하기
    List<String> findFileNames(Long bno);
    
    // 게시물 번호로 게시글 작성자의 계정명과 권한 가져오기
    ValridateMemberDTO findMemberByBoardNo(Long boardNo);
}
