package com.project.web_prg.util;

import com.project.web_prg.member.domain.Auth;
import com.project.web_prg.member.domain.Member;

import javax.servlet.http.HttpSession;

public class LoginUtils {

    public static final String LOGIN_FLAG = "loginUser";

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

}
