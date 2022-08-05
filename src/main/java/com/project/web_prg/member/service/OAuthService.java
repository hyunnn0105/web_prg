package com.project.web_prg.member.service;

// SNS로그인 통신처리 담당
public interface OAuthService {

    /**
     * 
     * @param authCode - 인증서버가 발급한 인가토드
     * @return - 엑서스 코드
     * @throws Exception - 통신에러
     */
    // 엑세스토큰 발급 메서드
    String getAccessToken(String authCode) throws Exception;


}
