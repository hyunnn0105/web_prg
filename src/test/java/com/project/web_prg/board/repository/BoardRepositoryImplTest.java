package com.project.web_prg.board.repository;

import com.project.web_prg.board.common.paging.Page;
import com.project.web_prg.board.mybatis.domain.Board;
import com.project.web_prg.board.service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardRepositoryImplTest {

    @Autowired // bean 등록X
//            @Qualifier("boardRepositoryImpl") -> Bean 등록 여러개 할때?
    BoardRepository repository;

    @Autowired
    BoardService service;

    /*
    @Test
    @DisplayName("게시물 전체조회")
    void findAllTest(){
        List<Board> boardList = service.findAllService();
        boardList.forEach(System.out::println);

        assertEquals(300,boardList.size());
        assertEquals("제목300", boardList.get(0).getTitle());
    }

     */

    @Test
    @DisplayName("300개의 게시물을 삽입해야 한다")
    // junit5부터
    void bulkInsert(){
        Board board;
        for (int i = 1; i <= 300 ; i++) {
            board= new Board();
            board.setTitle("제목" + i);
            board.setWriter("길동이"+i);
            board.setContent("hi" + i);
            repository.save(board);
        }
    }

    @Test
    @DisplayName("전체게시글을 조회하고 반환된 사이즈는 300이여야한다")
    void findAll(){
        List<Board> boardList = repository.findAll();
        boardList.forEach(b -> System.out.println(b));

        assertEquals(300,boardList.size());
    }

    @Test
    @DisplayName("게시글의 제목이 제목300이여야한다")
    void findOneTest(){
        Board board = repository.findOne(300L);
        assertEquals("제목300", board.getTitle());
    }

    @Test
    @DisplayName("특정 게시물을 삭제하고 해당 글이 조회되지 않아야 한다.")
    @Transactional // 실제 삭제xXX
    void deleteTest(){

        boolean remove = repository.remove(300L);

        assertTrue(remove);
        assertThrows(DataAccessException.class, ()-> repository.findOne(300L));
//        Board board = repository.findOne(300L);


    }

    @Test
    @DisplayName("특정 게시물을 수정하고 해당 글을 조회했을때 수정된 제목이 일치해야 한다")
    @Transactional // 실제 삭제xXX
    void modifyTest(){

        Board newboard = new Board();
        newboard.setBoardNo(300L);
        newboard.setTitle("수정된 제목");
        newboard.setWriter("수정된 작성자");
        newboard.setContent("hihihihiih");

        boolean modify = repository.modify(newboard);
        Board board = repository.findOne(newboard.getBoardNo());

        assertTrue(modify);
        assertEquals("수정된 제목", board.getTitle());
        assertEquals("수정된 작성자", board.getWriter());



    }


    @Test
    @DisplayName("전체 게시글 수를 조회해야한다")
    void countT(){
        int totalCount = repository.getTotalCount();
        assertTrue(totalCount==300);
    }

    @Test
    @DisplayName("원하는 페이지 수와 게시물의 양에 따라 게시물 목록을 조회해야한다.")
    void pagingTest(){
        Page page = new Page(1, 20);
        repository.findAll(page).forEach(System.out::println);
    }

}