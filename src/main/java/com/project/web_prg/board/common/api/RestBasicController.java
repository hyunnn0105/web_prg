package com.project.web_prg.board.common.api;

import com.project.web_prg.board.mybatis.domain.Board;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// jsp 뷰포워딩을 하지않고 클라이언트에게 JSON 데이터를 전송함
@RestController
@Log4j2
public class RestBasicController {

    @GetMapping("/api/hello")
    public String hello(){
        return "hello!!";
    }

    // what is json format
    @GetMapping("/api/board")
    public Board board(){
        Board board = new Board();
        board.setBoardNo(10L);
        board.setTitle("메로오ㅗㅇㅇ");
        board.setWriter("박영희");
        return board;
    }

    @GetMapping("/api/arr")
    public String[] arr(){
        String[] foods = {"짜장면", "탕수육", "볶음밥"};
        return foods;
    }


    // 야크 http://localhost:8185/api/join 확인해보기
    // post 요청처리
    // if 리스트 형태의 데이터가 필요하다면
    // 클라이언트가 리스트를 던져주면 자바로 바꿔야함
    // 스프링 라이브러리에 jackson
    // 기본은 리퀘스트파람
    // @RequestBody 요청 바디에 있는 걸 가져와서 처리하기
    /*
        야크 페이로드에 제이슨 형식으로 작성하기
        [
          "홍길동", "서울시", "30"
        ]

     */
    @PostMapping("api/join")
    // 클라이언트가 준 데이터를 받음 (json)
    public String join(@RequestBody List<String> info){
        log.info("/api/join POST - {}", info);
        // 클라이언트가 준 데이터를 넘김 -> 리턴 타입 맞춰주면 알아서 (json)으로 나감
        return "PUT-OK";
    }

    //put 요청처리
    @PutMapping("api/join")
    // 키값을 필드명이랑 일치시키기
    // 키값에 "" 쌍따옴표 붙여주기
    public String joinput(@RequestBody Board board){
        log.info("/api/join PUT -{}", board);
        return "PUT-OK";
    }

    //delete 요청처리
    @DeleteMapping("api/join")
    public String joindelete(){
        log.info("/api/join delete POST");
        return "DELETE OK";
    }

    // 레스트컨트롤러에서 리다이렉션
    /*
    @GetMapping("hoho")
    public ModelAndView hoho(){
        ModelAndView mv = mv.newModel
        return "index";
    }

     */

}
