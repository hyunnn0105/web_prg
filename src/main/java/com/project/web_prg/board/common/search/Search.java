package com.project.web_prg.board.common.search;

import com.project.web_prg.board.common.paging.Page;
import lombok.*;

@Setter @Getter @ToString
@NoArgsConstructor @AllArgsConstructor
public class Search extends Page{
    // 페이징이 필수 -> 페이지 컴포지션
    private String type; // 검색 조건
//    keyword를 query로 변경해서도 사용할 수 있다ㅏ
    private String keyword; //검색키워드
}
