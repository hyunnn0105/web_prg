package com.project.web_prg.interceptor;

import com.project.web_prg.member.domain.Member;
import com.project.web_prg.member.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.project.web_prg.util.LoginUtils.LOGIN_FLAG;
import static com.project.web_prg.util.LoginUtils.getAutoLoginCookie;

// mapper 주입
@Configuration
@RequiredArgsConstructor
public class AutoLoginInterceptor implements HandlerInterceptor {

    private final MemberMapper memberMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1. 자동로그인 쿠키 탐색
        Cookie c = getAutoLoginCookie(request);

        // 2. 자동로그인 쿠키가 발견된 결우 쿠키값으 읽어서 세션아이디 확인
        if (c != null){
            String sessionId = c.getValue();
            // 3. 쿠키에 저장된 세션아이디와 같은 값을 가진 회원정보 조회  - DB
            Member member = memberMapper.findByOneSessionId(sessionId);
            
            // 4. 세션에 해댱회원정보를 저장
            if (member != null){
                // 로그인 여부를 저장?
                request.getSession().setAttribute(LOGIN_FLAG, member);

            }
        }
        // 자동로그인이라 강제할거 없음
        return true;

    }
}
