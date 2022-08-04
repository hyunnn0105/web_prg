package com.project.web_prg.member.repository;

import com.project.web_prg.member.domain.Member;
import com.project.web_prg.member.dto.AutoLoginDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface MemberMapper {
    // 회원 가입 기능
    boolean resgister(Member member);

    // 중복체크 기능
    // 체크타입 : 계정 or email
    // check 값 : 중복검사대상 값
    // 필드가 작으면 MAP으로 넣을 수 있음
    int isDuplicate(Map<String, Object> checkMap);

    // 회원정보 조회 기능
    Member findUser(String account);

    // 자동로그인 쿠키정보 저장
    void saveAutoLoginCookie(AutoLoginDto dto);

    // 쿠키값(세션아디이)을 가지고 있는 회원정보 조회
    Member findByOneSessionId(String sessionId);
}
