package com.project.web_prg.board.common.paging;

import com.project.web_prg.board.common.Page;
import com.project.web_prg.board.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PagemakerTest {
    @Autowired
    BoardRepository repository;


    @Test
    @DisplayName("성공해야한다")
    void pageInfoTest(){

        int totalCount = repository.getTotalCount();
        PageMaker pm = new PageMaker(new Page(17, 10), totalCount);

        System.out.println(pm);

        assertEquals(11, pm.getBeginPage());
        assertEquals(20, pm.getEndPage());


    }

}