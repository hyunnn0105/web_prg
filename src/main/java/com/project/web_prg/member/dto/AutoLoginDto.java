package com.project.web_prg.member.dto;

import lombok.*;

import java.util.Date;

@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor

public class AutoLoginDto {

    private String account;
    private String sessionId;
    private Date limitTime;
}
