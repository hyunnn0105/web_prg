package com.project.web_prg.board.mybatis.repository;

import com.project.web_prg.board.common.paging.Page;
import com.project.web_prg.board.mybatis.domain.Board;
import com.project.web_prg.board.mybatis.dto.ValridateMemberDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        board.setAccount("tese1234");

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
    @Test
    @DisplayName("제목으로 검색된 목록을 조회해야 한다.")
    void serachTest(){
//        Search search = new Search(
//                new Page(1,10)
//                ,"hello"
//                ,""
//        );
//        mapper.findAll2(search).forEach(System.out::println);

    }

    @Test
    @DisplayName("특정 게시물에 첨부된 파일 경로들을 조회한다")
    void findFileNamesTest(){
        Long bno = 323L;

        List<String> fileNames = mapper.findFileNames(bno);

        fileNames.forEach(System.out::println);

        assertTrue(fileNames.size()==2);

    }

    @Test
    @DisplayName("게시글번호로 글쓴이의 계정정보 가져오기")
    void findAccountTest(){
        Long boardNo = 331L;
        ValridateMemberDTO memberDTO = mapper.findMemberByBoardNo(boardNo);

        System.out.println("memberDTO = " + memberDTO);
        assertEquals("tese1234", memberDTO.getAccount());
        assertEquals("COMMON", memberDTO.getAuth().toString());
    }
}