package com.project.web_prg.util;

import com.project.web_prg.member.domain.Auth;
import com.project.web_prg.member.domain.Member;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginUtils {

    public static final String LOGIN_FLAG = "loginUser";
    public static final String LOGIN_COOKIE = "autoLoginCookie";

    // 로그인 했는지 알려주기~
    public static boolean isLogin(HttpSession session){
        return session.getAttribute(LOGIN_FLAG) != null;
    }

    //
    public static String getCurrnetUtil(HttpSession session){
        Member member = (Member) session.getAttribute(LOGIN_FLAG);
        return member.getAccount();
    }
    
    // 로그인한 사용자 권한 가져오기
    public static Auth getCurrnetMemberAuth(HttpSession session){
        Member member = (Member) session.getAttribute(LOGIN_FLAG);
        return member.getAuth();
    }
    // 자동로그인 쿠키 가져오기
    public static Cookie getAutoLoginCookie(HttpServletRequest request){
        return WebUtils.getCookie(request, LOGIN_COOKIE);
    }
    // 자동로그인 쿠키가 있는지 확인
    public static boolean hasAutoLoginCookie(HttpServletRequest request){
        return WebUtils.getCookie(request, LOGIN_COOKIE) != null;
    }

}
