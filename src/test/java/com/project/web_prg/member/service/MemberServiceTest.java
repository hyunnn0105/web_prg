package com.project.web_prg.member.service;

import com.project.web_prg.member.domain.Auth;
import com.project.web_prg.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("평문 비밀번호로 회원가입하면 암호화되어 저장한다")
    void signUpTest(){
        Member m = new Member();
        m.setAccount("banana");
        m.setPassword("banana1234");
        m.setName("바나나나");
        m.setEmail("banana@naver.com");
        m.setAuth(Auth.ADMIN);

        memberService.signUp(m);
    }

    @Test
    @DisplayName("중복된 아이디를 전달하면 트루가 나와야함")
    void checkAccountServiceTest(){
        String account = "banana";
        boolean flag = memberService.checkSignUpValue("account", account);
        assertTrue(flag);
    }
}