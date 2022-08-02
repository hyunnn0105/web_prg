package com.project.web_prg.board.service;

import com.project.web_prg.board.common.search.Search;
import com.project.web_prg.board.mybatis.domain.Board;
import com.project.web_prg.board.mybatis.repository.BoardMapper;
import com.project.web_prg.reply.repository.ReplyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardService {

//    private final BoardRepository repository;
    private final BoardMapper boardMapper;



    private final ReplyMapper replyMapper;
    // 게시물 등록 요청 중간 처리
    @Transactional
    public boolean saveService(Board board){
        log.info("save service start - {}", board);
        // 게시글 내용 DB에 저장
        boolean flag = boardMapper.save(board);

        List<String> fileNames = board.getFileNames();

        if (fileNames != null && fileNames.size() > 0) {
            for (String fileName : fileNames) {
                // 첨부파일 내용 DB에 저장
                boardMapper.addFile(fileName);
            }
        }

        return flag;
    }

    /*
    // 게시글 전체 조회 요청 중간 처리
    public List<Board> findAllService(){
        log.info("findall service start");
        List<Board> boardList = repository.findAll();

        // 목록 중간데이터 처리
        // processConverting(boardList);

        // 글제목 줄임처리
        substringTitle(boardList);
        
        // 시간포멧팅 처리
        convertDateFormat(boardList);

        return boardList;
    }

     */
/*
    public Map<String, Object> findAllService(Page page){
        log.info("findall service start");

        HashMap<String, Object> findDataMap = new HashMap<>();

        List<Board> boardList = repository.findAll(page);

        // 목록 중간데이터 처리
        // processConverting(boardList);

        // 글제목 줄임처리
        substringTitle(boardList);

        // 시간포멧팅 처리
        convertDateFormat(boardList);

        // map에 리스트 + totalCount
        findDataMap.put("bList", boardList);
        findDataMap.put("tc", repository.getTotalCount());

        return findDataMap;
    }

 */

    public Map<String, Object> findAllService(Search search){
        log.info("findall service start");

        HashMap<String, Object> findDataMap = new HashMap<>();

        List<Board> boardList = boardMapper.findAll2(search);
        System.out.println(boardList);

        // 목록 중간데이터 처리
         processConverting(boardList);

        /*
        // 글제목 줄임처리
        substringTitle(boardList);
        // 시간포멧팅 처리
        convertDateFormat(boardList);
        // 새로운 게시글인지 확인
        checkNewArticle(boardList);

         */

        // map에 리스트 + totalCount
        findDataMap.put("bList", boardList);
        findDataMap.put("tc", boardMapper.getTotalCount2(search));


        return findDataMap;
    }




    private void processConverting(List<Board> boardList) {
        for (Board b : boardList) {
            convertDateFormat(b);
            substringTitle(b);
            checkNewArticle(b);
            setReplyCount(b);
        }
    }

    private void setReplyCount(Board b) {
        b.setReplyCount(replyMapper.getReplyCount(b.getBoardNo()));
    }

    private void checkNewArticle(Board b) {
        // 게시글의 작성일자와 현재시간을 대조
        // 게시물의 작성일자 가져오기 - 16억 5초
        long regDateTime = b.getRegDate().getTime();

        // 현재 시간 얻기 (밀리초) - 16억 10초
        long nowTime = System.currentTimeMillis();

        // 현재시간 - 작성시간
        long diff = nowTime - regDateTime;

        // 신규 게시물 제한시간
        long limitTime = 60 * 5 * 1000;

        if (diff < limitTime) {
            b.setNewArticle(true);
        }

    }



    private void convertDateFormat(Board b) {
            Date date = b.getRegDate();
            // hh -> 1~12 / HH -> 1~24 /a - am/pm
            SimpleDateFormat sdf = new SimpleDateFormat("YY-MM-dd a hh:mm");
            b.setPrettierDate(sdf.format(date));
    }

    private void substringTitle(Board b) {
        // 글제목 줄임처리

        // 만약에 글제목이 5글자 이상이라면
        // 5글자만 보여주고 나머지는 ...처리
        String title = b.getTitle();
        if (title.length() > 5) {
            String subStr = title.substring(0, 5);
            b.setShortTitle(subStr + "...");
        } else {
            b.setShortTitle(title);
        }
    }

    @Transactional // 전부 성공해야 한다        응답객체? -> 응답할떄 쿠키 전송
    public Board findOneService(Long boardNo, HttpServletResponse response, HttpServletRequest request){
        log.info("findOne ser start- {}", boardNo);
        Board board = boardMapper.findOne(boardNo);
        
        // 해당 게시물 번호에 해당하는 쿠키가 있는지 확인
        // 1. 쿠키가 없으면 조회수를 상승시켜주고 2.쿠키를 만들어서 클라이언트에 전송
        
        // 쿠키 -> 조회수를 올려주는 것에대해 판단?
        makeViewCount(boardNo, response, request);


        return board;
    }

    private void makeViewCount(Long boardNo, HttpServletResponse response, HttpServletRequest request) {
        // 1. 해당 이름의 쿠키가 있으면 쿠키가 들어오고 없ㅇ,면 null이 들어옴
        Cookie foundCookie = WebUtils.getCookie(request, "b" + boardNo);


        // 2.
        if (foundCookie == null) {
            boardMapper.upViewCount(boardNo);

            Cookie cookie = new Cookie("b" + boardNo, String.valueOf(boardNo));// 쿠키생성
            cookie.setMaxAge(60); // 쿠키 수명 설정(초단위) 60 * 60 이런식으로 설정 가능
            cookie.setPath("/board/content"); // cookie 작동 범위->컨텐트 아래 전부

            // 응답시 쿠키 전달해서 간다
            response.addCookie(cookie); //클라이언트에 쿠키 전송
        }
        
        // 세션 -> 쿠키를 삭제해도 남아있음 but 브라우저 창을 여러개 띄워서 올릴수있음
    }

    // 게시물 삭제 요청 중간 처리 - 저장된 댓글 삭제 후 게시글을 삭제하기
    // 둘 다 실행되도록 @Transactional 처리
    @Transactional
    public boolean removeService(Long boardNo) {
        log.info("remove service start - {}", boardNo);
        // 댓글 먼저 모두 삭제
        replyMapper.removeAll(boardNo);
        boolean delete = boardMapper.remove(boardNo);
        return delete;
    }

    // 게시물 수정 요청 중간 처리
    public boolean modifyService(Board board) {
        log.info("modify service start - {}", board);
//        makeViewCount(board.getBoardNo(),response, request);
        return boardMapper.modify(board);
    }

    // 첨부파일 목록
    public List<String> getFiles(Long bno){
        return boardMapper.findFileNames(bno);
    }


}
