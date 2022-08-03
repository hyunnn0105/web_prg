package com.project.web_prg.member.controller;

import com.project.web_prg.member.domain.Member;
import com.project.web_prg.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    // 회원가입 양식 띄우기 요청
    @GetMapping("/sign-up")
    public void signUp(){
        log.info("member controller sign up GET - forwording to sign-up.jsp");
        //Using generated security password: 75938b8c-292d-48aa-878a-8b962b536f22
        // 경로가 동일하면 return없이 void로 설정해도됨
        // return "member/sign-up";
    }

    // 회원가입 처리 요청
    @PostMapping("/sign-up")
    public String signUp(Member member){
        log.info("/Member/sign-up POST - {}", member);
        boolean flag = memberService.signUp(member);
        return flag ? "redirect:/" : "redirect:/member/sign-up";
    }

    @GetMapping("/check")
    @ResponseBody // 비동기
    public ResponseEntity<Boolean> check(String type, String value){
        log.info("/member/check?type={}&value={} GET ASYNC", type, value);

        boolean flag = memberService.checkSignUpValue(type, value);

        return new ResponseEntity<>(flag, HttpStatus.OK);
    }

}
