package com.project.web_prg.reply.repository;

import com.project.web_prg.reply.domain.Reply;
import com.project.web_prg.board.common.paging.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReplyMapper {
    //댓글 입력
    boolean save(Reply reply);

    //댓글 수정
    boolean modify(Reply reply);

    //댓글 삭제
    boolean remove(Long replyNo);

    // 댓글 전체 삭제 -> 게시글 번호 받아서 전체삭제
    boolean removeAll(Long boardNo);

    //댓글 개별 조회
    Reply findOne(Long replyNo);

    //댓글 목록 조회 #{page.필드명}-> 하나일 때 사용간으
    List<Reply> findAll(@Param("boardNo") Long boardNo
            , @Param("page") Page page);

    // 댓글 수 조회
    int getReplyCount(Long boardNo);

}
