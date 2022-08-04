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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.project.web_prg.util.LoginUtils.*;

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
    public void signIn(@ModelAttribute("message") String message, HttpServletRequest request){

        log.info("member controller sign-in GET - forwording to sign-up.jsp");
        // 요청 정보 헤더 안에는 Referer라는 키가 있는데
        // 여기 안에는 이 페이지를 진입할 때 어디에서 왔는지 URL 정보가 들어있음

        String referer = request.getHeader("Referer");
        log.info("referer -{}", referer);
        log.info("message - {}", message);

        request.getSession().setAttribute("redirectURI", referer);
    
    }

    // 로그인 요청 처리 - 아이디 / 비번 / 자동로그인(O/X) 요청 3가지
    // BUT 체크 안하면 2개만 나감
    // ** 중요 사용하는 데이터에 따라 도메인을 따로 생성해서 사용한다!
    @PostMapping("/sign-in")
    public String signIn(LoginDTO inputData
            , RedirectAttributes ra
            , HttpSession session
            , HttpServletResponse response             // 세션정보 객체
    ){
        log.info("/member/sign-in POST - {}", inputData);
        // 여기에서 보여지는 로그는 js에서 따로 처리해야함
//        log.info("session timeout - {}", session.getMaxInactiveInterval());
        
        // 로그인 서비스 호출
        LoginFlag flag = memberService.logIn(inputData, session, response);

        if (flag == LoginFlag.SUCCESS) {
            log.info("login success!");
            String redirectURI = (String) session.getAttribute("redirectURI");
            log.info("redirectURI - {}", redirectURI);
            return "redirect:" + redirectURI; // 로그인 요청이 들어온 곳으로 돌아가기
        }
        // Msg -> message인가?
        ra.addFlashAttribute("loginMsg", flag);
        // sign-in.jsp
        return "redirect:/member/sign-in";
    }
    
    @GetMapping("/sign-out")
    public String signOut(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        if (isLogin(session)){
            // 만약 자동로그인 상태라면 해제한다.
            if (hasAutoLoginCookie(request)){
                memberService.autoLogOut(getCurrnetUtil(session), request, response);

            }
            // 1. 세션에서 정보를 삭제한다
            session.removeAttribute(LOGIN_FLAG);
            // 2. 세션을 무효화 한다
            session.invalidate();
            return "redirect:/";
            // 자동로그인이면 자동로드인 쿠키 삭제해주기!! +[22.08.04]
            //
        }
        return "redirect:/member/sign-in";
    }

}
