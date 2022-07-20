package com.project.web_prg.board.common.paging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

// commamd 객체 실행시 기본생성자 + setter 실행
@ToString @Getter
@AllArgsConstructor
public class Page {
    private int pageNum; // 페이지 번호
    private int amount; // 한 페이지당 배치할 게시물 수

    public Page() {
        this.pageNum = 1;
        this.amount = 10;
    }

    public void setPageNum(int pageNum) {
        // int Max값보다 작게 들어가야함
        if (pageNum <= 0 || pageNum > Integer.MAX_VALUE){
            this.pageNum = 1;
            return;
        }
        this.pageNum = pageNum;
    }

    public void setAmount(int amount) {
        // 양이 너무 작거나 100개 이상 보여달라고 할 때
        if (amount < 10 || amount > 100) {
            this.amount = 10;
            return;
        }
        this.amount = amount;
    }

}
