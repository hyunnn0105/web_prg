package com.project.web_prg.board.service;

import com.project.web_prg.board.domain.Board;
import com.project.web_prg.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

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
        return repository.findAll();
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
