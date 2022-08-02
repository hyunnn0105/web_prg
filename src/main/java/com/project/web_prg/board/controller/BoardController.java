package com.project.web_prg.board.controller;

import com.project.web_prg.board.common.paging.Page;
import com.project.web_prg.board.common.paging.PageMaker;
import com.project.web_prg.board.common.search.Search;
import com.project.web_prg.board.mybatis.domain.Board;
import com.project.web_prg.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    // 게시글 목록 요청
    @GetMapping("/list")
    // jsp로 날릴때 @ModelAttribute
    public String list(@ModelAttribute("s") Search search, Model model){
        log.info("controller requ /board/list GET : {}", search);

        Map<String, Object> boardMap = boardService.findAllService(search);
        log.debug("return data -{}", boardMap);

        // 페이지 정보 생성
        PageMaker pm = new PageMaker(new Page(search.getPageNum(), search.getAmount())
                , (Integer) boardMap.get("tc"));

        log.info("totalcount - {}", pm);
        // http://localhost:8185/board/list?type=tc&keyword=%ED%95%98%ED%95%98
        //totalcount - PageMaker(beginPage=1, endPage=10, prev=false, next=true, page=Page(pageNum=1, amount=10), totalCount=306)
        // endpage 값이랑 total이랑,,,

        model.addAttribute("bList", boardMap.get("bList"));
        model.addAttribute("pm", pm);
        return "board/board-list";
    }

    // controller 수정
    @GetMapping("/content/{boardNo}")
    public String content(@PathVariable Long boardNo, Model model,
                          HttpServletResponse response, HttpServletRequest request
                            , @ModelAttribute("p") Page page){
        log.info("controller request /board/list GET");
        Board board = boardService.findOneService(boardNo, response,request);
        log.info("return data - {}", board);
        model.addAttribute("b", board);
        return "board/board-detail";
    }

    // 게시물 쓰기 화면 요청
    @GetMapping("/write")
    public String write(){
        log.info("controller request /board/write GET");
        return "board/board-write";
    }

    // 게시물 등록 화면 요청
    @PostMapping("/write")
//    public String write(@RequestBody Board board){ <- test용
    // model -> fowarding / 
    public String write(Board board, @RequestParam("files") List<MultipartFile> fileList, RedirectAttributes ra){
        log.info("controller request /board/write POST! - {}", board);

        /*
        if (fileList != null) {
            List<String> fileNames = new ArrayList<>();
            for (MultipartFile f : fileList) {
                log.info("attachment file-name: {}", f.getOriginalFilename());
                fileNames.add(f.getOriginalFilename());
            }
            board.setFileNames(fileNames);
        }

         */

        // db 넣기
        boolean flag = boardService.saveService(board);
        // 게시글 등록에 성공하면 클라이언트에 성공메세지 전송 
        //BUT req에 담은거라 msg 값이 리다이렉트 되는 순간 값이 사라진다
        // RedirectAttributes / addFlashAttribute 사용해서 (글쓰기를 통한 리다이렉트 목록 요청시 success) 값을 전달한다
        if (flag) ra.addFlashAttribute("msg","reg-success");
        return flag ? "redirect:/board/list" : "redirect:/";
    }

    // 게시물 삭제 요청
    @GetMapping("/delete")
    public String delete(Long boardNo){
        log.info("controller request /board/delete GET - {}", boardNo);
        return boardService.removeService(boardNo) ? "redirect:/board/list" : "redirect:/";
    }

    // 수정화면 요청 GET --> overloding 규칙 위반 (안에 들어가는 값이 달라야한다)
    // findOne 있어서 req/rep 생성해서 넘김
    @GetMapping("/modify")
    public String modify(Long boardNo, Model model, HttpServletRequest request, HttpServletResponse response){
        log.info("controller request /board/modify GET! - bno {}", boardNo);
        Board board = boardService.findOneService(boardNo, response ,request);
        model.addAttribute("board", board);
        return "board/board-modify";
    }


    // 수정 처리 요청 POST-> ㅠㅏ라미터 방식으로 값 넘기기 X
    @PostMapping("/modify")
    public String modify(Board board){
        log.info("controller request /board/modify POST - {}", board);
        boolean flag = boardService.modifyService(board);
        return flag ? "redirect:/board/content/" + board.getBoardNo() : "redirect:/";
    }

}
