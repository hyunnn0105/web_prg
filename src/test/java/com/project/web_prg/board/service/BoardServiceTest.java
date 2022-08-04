package com.project.web_prg.board.service;

import com.project.web_prg.board.mybatis.domain.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    BoardService service;

//    @Test
//    @DisplayName("board write")
//    void writeServiceTest(){
//        Board b = new Board();
//        b.setAccount("tese1234");
//        b.setTitle("servicetest");
////        b.setWriter();
//        b.setContent("sdfawefawfwe");
//
//
//        service.saveService(b);
//        System.out.println(b);
//    }
    @Test
    @DisplayName("파일첨부가 잘 수행되어야한다")
    void addFileTest(){
        Board b = new Board();
        b.setWriter("filet");
        b.setTitle("filett");
        b.setContent("file add Test");
        b.setFileNames(Arrays.asList("dog.jsp", "cat.jsp"));

        service.saveService(b);
    }

}