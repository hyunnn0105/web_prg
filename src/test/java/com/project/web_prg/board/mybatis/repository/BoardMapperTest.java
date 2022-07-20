package com.project.web_prg.board.mybatis.repository;

import com.project.web_prg.board.common.paging.Page;
import com.project.web_prg.board.mybatis.domain.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardMapperTest {

    @Autowired
    BoardMapper mapper;

    @Test
    @DisplayName("insert mapper t")
    void insertTest(){
        Board board = new Board();
        board.setTitle("mapperT");
        board.setWriter("mama");
        board.setContent("sdfjasfweiwohwefhawefwefw");

        mapper.save(board);
    }
    
    @Test
    @DisplayName("값이 삭제되어야한다")
    void deleteTest(){
        boolean result = mapper.remove(314L);
        assertTrue(result);
    }

    @Test
    @DisplayName("모든 값을 조회할 수 있다")
    void findAllT(){
        Page page = new Page(1,20);
        mapper.findAll(page).forEach(System.out::println);
    }

    /*
    @Test
    @DisplayName("하나의 값의 정보를 업데이트 할 수 있다")
    void findOneTest(){
        mapper.modify();
    }

     */

}