package com.project.web_prg.board.controller;

import com.project.web_prg.board.domain.Board;
import com.project.web_prg.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    // 게시글 목록 요청
    @GetMapping("/list")
    public String list(){
        log.info("controller requ /board/list GET");
        List<Board> boardList = boardService.findAllService();
        log.info("return data -{}", boardList);
        return "";
    }

    @GetMapping("/content/{boardNo}")
    public String content(@PathVariable Long boardNo){
        log.info("controller request /board/list GET");
        Board board = boardService.findOneService(boardNo);
        log.info("return data - {}", board);
        return "";
    }

    // 게시물 쓰기 화면 요청
    @GetMapping("/write")
    public String write(){
        log.info("controller req /board/write GET");
        return "";
    }
}
