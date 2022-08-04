package com.project.web_prg.member.domain;

import lombok.*;

import java.util.Date;

// @DATA 사용하면 나중에 할때 불편하다
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private String account;
    private String password;
    private String name;
    private String email;
    private Auth auth;
    // 공통컬럼을 상속으로 처리?
    private Date regDate;
    private String sessionId;
    private Date limitTime;

}
