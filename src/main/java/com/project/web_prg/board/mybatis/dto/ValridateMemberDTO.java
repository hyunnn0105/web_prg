package com.project.web_prg.board.mybatis.dto;

import com.project.web_prg.member.domain.Auth;
import lombok.*;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class ValridateMemberDTO {
    // 삭제할때 넘겨줄거?
    private String account;
    private Auth auth;
}
