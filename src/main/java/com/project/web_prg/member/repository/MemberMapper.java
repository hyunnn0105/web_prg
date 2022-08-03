package com.project.web_prg.member.repository;

import com.project.web_prg.member.domain.Member;
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
    int inDuplicate(Map<String, Object> checkMap);

    // 회원정보 조회 기능
    Member findUser(String account);

}
