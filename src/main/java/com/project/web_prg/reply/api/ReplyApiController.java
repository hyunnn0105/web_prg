package com.project.web_prg.reply.api;

import com.project.web_prg.board.common.paging.Page;
import com.project.web_prg.reply.domain.Reply;
import com.project.web_prg.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1/replies")
public class ReplyApiController {

    private final ReplyService replyService;

    /*
        - 댓글 목록 요청  : /api/v1/replies - GET
        - 댓글 개별 조회 요청 : /api/v1/replies/72 - GET
        - 댓글 쓰기요청 : api/v1/replies - POST
        - 댓글 수정요청 : api/v1/replies/72 - PUT
        - 댓글 삭제요청 : api/v1/replies/72 - DELETE
     */

    // 댓글 목록 요청 - 글번호는 쿼리/파라미터 json X
    @GetMapping("")
    public Map<String, Object> list(Long boardNo, Page page){
        log.info("/api/v1/replies GET bno-{}, page-{}", boardNo, page);

        Map<String, Object> replies = replyService.getList(boardNo, page);

        return replies;
    }
    
    // 댓글 등록 요청
    // http://localhost:8185/api/v1/replies?boardNo=301
    @PostMapping("")
    // form으로 받을 수 없어서 json으로 받기
    public String create(@RequestBody Reply reply){
        log.info("/api/v1/replies POST - {}", reply);
        boolean flag = replyService.write(reply);
        return flag ? "success" : "fail";
    }

    // 댓글 수정 요청
    @PutMapping("/{rno}")
    // 경로 문자열? @PathVariable / 수정값은 리퀘스트 바디
    public String modify(@PathVariable Long rno , @RequestBody Reply reply){
        // 경로 문자열 넣어주기
        reply.setReplyNo(rno);
        log.info("/api/v1/replies POST - {}", reply);
        boolean flag = replyService.modify(reply);
        return flag ? "modi-success" : "modi-fail";
    }

    // 댓글 삭제 요청
    @DeleteMapping("/{rno}")
    public String delete(@PathVariable Long rno){
        log.info("/api/v1/replies DELETE - {}", rno);
        boolean flag = replyService.remove(rno);
        return flag ? "del-success" : "del-fail";
    }

    /*
            {
          "replyNo": 1002,
          "replyText": "YARC로 댓글 수정하기!1 히히",
          "replyWriter": "메메메",
          "boardNo": 301
        }

        {
          "replyNo": 1001,
          "boardNo": 301
        }

     */

}
