package com.project.web_prg.member.service;

import com.project.web_prg.member.domain.Member;
import com.project.web_prg.member.dto.AutoLoginDto;
import com.project.web_prg.member.dto.LoginDTO;
import com.project.web_prg.member.repository.MemberMapper;
import com.project.web_prg.util.LoginUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.project.web_prg.member.service.LoginFlag.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;

    // encoder 주입
    private final BCryptPasswordEncoder encoder;
    
    // 회원가입 중간 처리
    public boolean signUp(Member member) {
        // 비밀번호 인코딩 클라이언트 -> 컨트롤러 -> (평문) -> 서비스 -> 암호화
        member.setPassword(encoder.encode(member.getPassword()));
        return memberMapper.resgister(member);
    }

    // 중복확인 중간처리

    /**
     * 계정과 이메일의 중복을 확인하는 메서드
     * @param type - 확인할 정보 (account or email)
     * @param value - 확인할 값
     * @return 중복이라면 true, 중복이 아니라면 false
     */
    public boolean checkSignUpValue(String type, String value) {
        Map<String, Object> checkMap = new HashMap<>();
        checkMap.put("type", type);
        checkMap.put("value", value);

        return memberMapper.isDuplicate(checkMap) == 1;
    }

    // 회원정보조회 중간 처리
    public Member getMember(String account){
        return memberMapper.findUser(account);
    }

    // 로그인 처리
    public LoginFlag logIn(LoginDTO inputData, HttpSession session, HttpServletResponse response){
        // 회원가입 여부 확인
        Member foundMember = memberMapper.findUser(inputData.getAccount());
        if (foundMember != null){
            if (encoder.matches(inputData.getPassword(), foundMember.getPassword())){
                // 로그인 성공 -> 상수
                // 세션에 사용자 정보기록 저장
                session.setAttribute("loginUser", foundMember);
                // 세션 타임아웃 설정(default 30분) -> 1시간
                session.setMaxInactiveInterval( 60 * 60 );

                // 자동로그인 처리
                if (inputData.isAutoLogin()) {
                    log.info("check autologin user --- ! ");
                    KeepLogin(foundMember.getAccount(), session, response);
                }

                return SUCCESS;
            } else {
                // 비번 오류
                return NO_PW;
            }
        } else {
            // 아이디 없음
            return NO_ACC;
        }
    }

    // 자동로그인
    private void KeepLogin(String account ,HttpSession session, HttpServletResponse response) {
        // 1. 자동로그인 쿠키 생성 -> 쿠키의 값으로 현재 세션의 아이디를 저장
        String sessionId = session.getId();
        Cookie autoLoginCookie = new Cookie(LoginUtils.LOGIN_COOKIE, sessionId);

        // 2. 쿠키설정 (수명, 사용 경로)
        int limitTime = 60*60*24*90; // 90일에 대한 second
        autoLoginCookie.setMaxAge(limitTime);
        autoLoginCookie.setPath("/"); // 전체경로로 지정 "/*"이게 여기서 사용 못함
        
        // 3. 로컬에 쿠키 전송 + 응답할떄 사용해서 response 사용함
        response.addCookie(autoLoginCookie);
        
        // 4. 디비에 쿠키값과 수명 저장 + tbl_member에 쿠키 추가?
        AutoLoginDto dto = new AutoLoginDto();
        dto.setSessionId(sessionId);
        // 자동로그인유지시간(초)를 날짜로 변환 -- DB DATE로 들어가서 초를 날짜로 변환하기
        Long nowTime = System.currentTimeMillis();
        Date limitDate = new Date(nowTime + ((long) limitTime * 1000)); // 날짜 저장 (밀리초로 변환!)
        log.info("limitDate - {}", limitDate);
        dto.setLimitTime(limitDate);

        dto.setAccount(account);
        memberMapper.saveAutoLoginCookie(dto);
    }

    // 자동로그인 해제
    public void autoLogOut(String account,HttpServletRequest request, HttpServletResponse response) {
        // 1. 자동로그인 쿠키를 불러온 뒤 수명을 0초로 세팅해서 클라이언트에게 돌려보낸다
        Cookie c = LoginUtils.getAutoLoginCookie(request);
        if (c != null) {
            c.setMaxAge(0);
            response.addCookie(c);

            AutoLoginDto dto = new AutoLoginDto();
            dto.setSessionId("none");
            dto.setLimitTime(new Date());
            dto.setAccount(account);

            // 2. 디비 처리 -> 업데이트 해서
            memberMapper.saveAutoLoginCookie(dto);
        }


    }
}
