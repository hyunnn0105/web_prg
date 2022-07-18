package com.project.web_prg.board.service;

import com.project.web_prg.board.domain.Board;
import com.project.web_prg.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository repository;
    // 게시물 등록 요청 중간 처리
    public boolean saveService(Board board){
        log.info("save service start - {}", board);
        return repository.save(board);
    }

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

    /*
    private void processConverting(List<Board> boardList) {
        for (Board b : boardList) {
            convertDateFormat(b);
            substringTitle(b);
        }
    }

     */


    private void convertDateFormat(List<Board> boardList) {
        for (Board b : boardList) {
            Date date = b.getRegDate();
            // hh -> 1~12 / HH -> 1~24 /a - am/pm
            SimpleDateFormat sdf = new SimpleDateFormat("YY-MM-dd a hh:mm");
            b.setPrettierDate(sdf.format(date));
        }
    }

    private void substringTitle(List<Board> boardList) {
        // 글제목 줄임처리
        for (Board b : boardList) {
            // 만약에 글 제목이 5글자 이상이라면 7글자만 보여주고 나머지는 ... 처리하기
            String title = b.getTitle();
            if (title.length() > 5){
                // 0~6번까지 자르기
                String substr = title.substring(0, 4);
                // 제목 다시 세팅하기
                b.setShortTitle(substr + "...");
            } else {
                // 안넘으면 제목=shortTitle
                b.setShortTitle(title);
            }
        }
    }

    public Board findOneService(Long boardNo){
        log.info("findOne ser start- {}", boardNo);
        return repository.findOne(boardNo);
    }

    // 게시물 삭제 요청 중간 처리
    public boolean removeService(Long boardNo) {
        log.info("remove service start - {}", boardNo);
        return repository.remove(boardNo);
    }

    // 게시물 수정 요청 중간 처리
    public boolean modifyService(Board board) {
        log.info("modify service start - {}", board);
        return repository.modify(board);
    }


}
