package com.project.web_prg.member.controller;

import com.project.web_prg.member.domain.Member;
import com.project.web_prg.member.dto.KakaouserInfoDto;
import com.project.web_prg.member.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

import static com.project.web_prg.member.domain.OAuthValue.KAKAO_APP_KEY;
import static com.project.web_prg.member.domain.OAuthValue.KAKAO_REDIRECT_URI;
import static com.project.web_prg.member.domain.SNSLogin.KAKAO;
import static com.project.web_prg.util.LoginUtils.LOGIN_FLAG;
import static com.project.web_prg.util.LoginUtils.LOGIN_FROM;

@Controller
@Log4j2
@RequiredArgsConstructor
public class KaKaoController {

    private final KakaoService kakaoService;

    @GetMapping("/kakao-test")
    public void kakaoTest(Model model){
        log.info("forward to kakao-test.jsp ");
        model.addAttribute("appKey", KAKAO_APP_KEY);
        model.addAttribute("redirectUri", KAKAO_REDIRECT_URI);
    }
    
    // 카카오 인증서버가 보내준 인가코드를 받아서 처리할 메서드
    @GetMapping(KAKAO_REDIRECT_URI)
    public String kakaoLogin(String code, HttpSession session) throws Exception {
        log.info("{} GET code - {}", KAKAO_REDIRECT_URI, code);
        
        // 인가코드를 통해 액세스토큰 발급받기
        String accessToken = kakaoService.getAccessToken(code);
        
        // header에 accessToken 넣어주기
        // accessToken을 통해 사용자 정보 요청(프로필사진,닉네임 등)
        KakaouserInfoDto userInfo = kakaoService.getKakaoUerInfo(accessToken);
        // 우리서버에서 카카오 서버로 통신해야함!
        if (userInfo != null){
            Member member = new Member();
            member.setAccount(userInfo.getEmail());
            member.setName(userInfo.getNickName());
            member.setEmail(userInfo.getEmail());

            session.setAttribute(LOGIN_FLAG, member);
            session.setAttribute("profile_path", userInfo.getProfileImg());
            session.setAttribute(LOGIN_FROM, KAKAO);
            session.setAttribute("accessToken", accessToken);
            return "redirect:/";
        }
        return "redirect:/member/sign-in";
    }

    // 카카오 로그아웃 -> url은 내가설정하기
    @GetMapping("/kakao/logout")
    public String kakaoLogout(HttpSession session) throws Exception{

        // 카카오 로그아웃 처리
        kakaoService.logOut((String) session.getAttribute("accessToken"));
        // 우리 서비스 로그아웃 -> 우리 사이트 세션 날려주기
        session.invalidate();

        return "redirect:/kakao-test";
    }

}
