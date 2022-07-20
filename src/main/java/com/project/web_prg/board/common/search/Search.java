package com.project.web_prg.board.common.search;

import com.project.web_prg.board.common.paging.Page;
import lombok.*;

@Setter @Getter @ToString
@NoArgsConstructor @AllArgsConstructor
public class Search {
    // 페이징이 필수 -> 페이지 컴포지션
    private Page page;
    private String type; // 검색 조건
    private String keyword; //검색키워드
}
