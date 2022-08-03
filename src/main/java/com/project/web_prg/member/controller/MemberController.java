package com.project.web_prg.member.controller;

import com.project.web_prg.member.domain.Member;
import com.project.web_prg.member.dto.LoginDTO;
import com.project.web_prg.member.service.LoginFlag;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    // 회원가입 양식 띄우기 요청
    @GetMapping("/sign-up")
    public void signUp(Member member ,RedirectAttributes ra){
        log.info("member controller sign up GET - forwording to sign-up.jsp");
        //Using generated security password: 75938b8c-292d-48aa-878a-8b962b536f22
        // 경로가 동일하면 return없이 void로 설정해도됨
        // return "member/sign-up";
        ra.addFlashAttribute("msg", "reg-success");
    }

    // 회원가입 처리 요청
    @PostMapping("/sign-up")
    public String signUp(Member member){
        log.info("/Member/sign-up POST - {}", member);
        boolean flag = memberService.signUp(member);
        // 어디로 이동시킬 것인가? 이메일 인증(DB 컬럼에도 있어야함) or 바로 로그인 폼으로 돌리기?
        return flag ? "redirect:/member/sign-in" : "redirect:/member/sign-up";
    }

    @GetMapping("/check")
    @ResponseBody // 비동기
    public ResponseEntity<Boolean> check(String type, String value){
        log.info("/member/check?type={}&value={} GET ASYNC", type, value);

        boolean flag = memberService.checkSignUpValue(type, value);

        return new ResponseEntity<>(flag, HttpStatus.OK);
    }

    // 로그인 화면을 열어주는 요청처리
    @GetMapping("/sign-in")
    public void signIn(){
        log.info("member controller sign-in GET - forwording to sign-up.jsp");
    }

    // 로그인 요청 처리 - 아이디 / 비번 / 자동로그인(O/X) 요청 3가지
    // BUT 체크 안하면 2개만 나감
    // ** 중요 사용하는 데이터에 따라 도메인을 따로 생성해서 사용한다!
    @PostMapping("/sign-in")
    public String signIn(LoginDTO inputData
            , RedirectAttributes ra
            , HttpSession session // 세션정보 객체
    ){
        log.info("/member/sign-in POST - {}", inputData);
        // 여기에서 보여지는 로그는 js에서 따로 처리해야함
//        log.info("session timeout - {}", session.getMaxInactiveInterval());
        
        // 로그인 서비스 호출
        LoginFlag flag = memberService.logIn(inputData, session);

        if (flag == LoginFlag.SUCCESS) {
            log.info("login success!");
            return "redirect:/"; // home
        }
        // Msg -> message인가?
        ra.addFlashAttribute("loginMsg", flag);
        // sign-in.jsp
        return "redirect:/member/sign-in";
    }

}
