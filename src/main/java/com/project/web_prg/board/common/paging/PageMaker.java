package com.project.web_prg.board.common.paging;

import com.project.web_prg.board.common.Page;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 페이지 렌더링 정보 생성
/*
@Setter @Getter @ToString
public class Pagemaker {

    // 한번에 그려낼 페이지 수(버튼의 수)
    private static final int PAGE_COUNT = 10;

    // 랜더링시 페이지 시작값 / 페이지 끝값
    private int beginPage, endPage;
    
    // 이전/다음 버튼 활성화 여부
    private boolean prev, next;

    private Page page; // 현재 위치한 페이지 정보

    public Pagemaker(Page page){
        this.page = page;
        makePageInfo();
    }

    // 페이지 정보 생성 알고리즘
    private void makePageInfo(){
        
        // 1. endpage 계산
        // 올림처리 (현재위치한페이지번호 / 한화면에 배치할 페이지수) * 한 화면에 배치할 페이지수
        // 전체 결과를 다운캐스팅 -> 형식?을 맞춰줌
        this.endPage = (int) (Math.ceil(page.getPageNum() / (double)PAGE_COUNT) * PAGE_COUNT);



    }

 */
// 페이지 렌더링 정보 생성
@Setter
@Getter
@ToString
public class PageMaker {

    // 한번에 그려낼 페이지 수
    private static final int PAGE_COUNT = 10;

    // 렌더링시 페이지 시작값, 페이지 끝값
    private int beginPage, endPage;

    // 이전, 다음 버튼 활성화 여부
    private boolean prev, next;

    private Page page; // 현재 위치한 페이지 정보

    private int totalCount;

    public PageMaker(Page page, int totalCount) {
        this.page = page;
        this.totalCount = totalCount;
        makePageInfo();
    }

    // 페이지 정보 생성 알고리즘
    private void makePageInfo() {

        //1. endPage 계산
        // 올림처리 (현재 위치한 페이지번호 / 한 화면에 배치할 페이지수 ) *  한 화면에 배치할 페이지 수
        this.endPage = (int)(Math.ceil(page.getPageNum() / (double)PAGE_COUNT) * PAGE_COUNT);

        // 2. beginPage 계산
        this.beginPage = endPage - PAGE_COUNT + 1;
        
        /*
        총 게시물 수가 284개고 한 화면당 10개의 게시물을 배치하고 있다면 페이지의 구간은?
            1~10페이지 구간 : 게시물 100개
            11~20페이지 구간 : 게시물 100개
            21~30페이지 구간 : 게시물 100개 ??? -> 게시글이 모자라다
            21~24페이지 구간 : 게시물 37개
        - 마지막 페이지 구간에는 *보정*이 필요하다

        - 마지막 페이지 구간 끝 페이지 보정 공식 : 올림처리(총 게시물수 / 한페이지당 배치할 게시물 수(amount))
            
            
         */
        int realEnd = (int)Math.ceil((double) totalCount/page.getAmount());
        
        // 그러면 끝 페이지 보정은 언제 일어나는가?
        // 마지막 페이지 구간에서 일어날 수도 있고 아닐수도 있다
        if (realEnd < endPage){
            this.endPage = realEnd;
        }

        // 이전버튼 활성화 여부
        this.prev = beginPage > 1;

        // 다음 버튼 활성화 여부
        this.next = endPage < realEnd;
    }



    
}
