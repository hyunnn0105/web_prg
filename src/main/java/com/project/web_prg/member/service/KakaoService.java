package com.project.web_prg.member.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project.web_prg.member.domain.OAuthValue;
import com.project.web_prg.member.dto.KakaouserInfoDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Log4j2
public class KakaoService implements OAuthService, OAuthValue {
    // OAuthValue : appKey, redirectUri

    // 카카오로그인시 사용자정보에 접근할 수 있는 엑세스토큰을 발급
    @Override
    public String getAccessToken(String authCode) throws Exception {
        // 1. 엑세스 토큰을 발급 요청할 URI
        String reqUri = "https://kauth.kakao.com/oauth/token";

        // 2. server to server 요청
        // 2-1. 문자타입의 URL을 객체로 포장
        URL url = new URL(reqUri);

        // 2-2. 해당요청을 연결하고 그 연결정보를 담을 conntection 객체 생성
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 2-3. 요청정보를 설정
        conn.setRequestMethod("POST"); // 요청 방식 설정

        // 요청 헤더 설정
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        conn.setDoOutput(true); // 응답 결과를 받겠다!

        sendAccessTokenRequest(authCode, conn);

        // 3. 응답데이터 받기
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))){

            // 3-1. 응답데이터를 입력스트림으로부터 읽기
            String responseData = br.readLine();
            log.info("responseData - {}", responseData);
            
            // 3-2. 응답받은 json을 gson 라이브러리를 사용하여 자바 객체로 파싱
            JsonParser parser = new JsonParser();
            // JsonElement는 자바로 변환된 JSON
            JsonElement element = parser.parse(responseData);
            
            // 3-3. json property key를 사용해서 필요한 데이터 추출
            JsonObject object = element.getAsJsonObject();
            String accessToken = object.get("access_token").getAsString();
            String tokenType = object.get("token_type").getAsString();
            log.info("tokenType -{}", tokenType);
            log.info("access_token - {}", accessToken);

//            getKakaoUerInfo(accessToken);

            return accessToken;

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    private void sendAccessTokenRequest(String authCode, HttpURLConnection conn) throws IOException {


        // 2-4. 요청 파라미터 추가 using stream
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()))) {
            StringBuilder queryParm = new StringBuilder();
            queryParm.append("grant_type=authorization_code")
                    .append("&client_id=" + KAKAO_APP_KEY)
                    .append("&redirect_uri=http://localhost:8185" + KAKAO_REDIRECT_URI)
                    .append("&code=" + authCode);

//            log.info("queryParm - {}", queryParm.toString());
            // 출력스트림을 이용해서 파라미터 전송
            bw.write(queryParm.toString());
            // 버퍼 비워주기
            bw.flush();

            // 응답상태코드확인
            int responseCode = conn.getResponseCode();
            log.info("response code - {}", responseCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public KakaouserInfoDto getKakaoUerInfo(String accessToken) throws Exception {
        //Request: 액세스 토큰 사용
        String reqUri = "https://kapi.kakao.com/v2/user/me";
        // 2. server to server 요청
        // 2-1. 문자타입의 URL을 객체로 포장
        URL url = new URL(reqUri);

        // 2-2. 해당요청을 연결하고 그 연결정보를 담을 conntection 객체 생성
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 2-3. 요청정보를 설정
        conn.setRequestMethod("POST"); // 요청 방식 설정

        // 요청 헤더 설정
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setDoOutput(true); // 응답 결과를 받겠다!

        int responseCode = conn.getResponseCode();
        log.info("userInfo res-code - {}", responseCode);


        //  응답 데이터 받기
        try (BufferedReader br
                     = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {

            String responseData = br.readLine();
            log.info("responseData - {}", responseData);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(responseData);

            JsonObject object = element.getAsJsonObject();
            // 가진 값들이 object
            JsonObject kakaoAccount = object.get("kakao_account").getAsJsonObject();
            JsonObject profile = kakaoAccount.get("profile").getAsJsonObject();

            String nickName = profile.get("nickname").getAsString();
            log.info("nickName - {}", nickName);
            String profileImage = profile.get("profile_image_url").getAsString();
            log.info("profileImage - {}", profileImage);
            String email = kakaoAccount.get("email").getAsString();
            log.info("email - {}", email);
//            String gender = kakaoAccount.get("gender").getAsString();
//            log.info("gender - {}", gender);

            KakaouserInfoDto dto = new KakaouserInfoDto(nickName, profileImage, email);

            log.info("카카오 유저 정보: {}", dto);

            return dto;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void logOut(String accessToken) throws Exception {

        //Request: 액세스 토큰 사용
        String reqUri = "https://kapi.kakao.com/v1/user/logout";
        // 2. server to server 요청
        URL url = new URL(reqUri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 2-3. 요청정보를 설정
        conn.setRequestMethod("POST"); // 요청 방식 설정

        // 요청 헤더 설정
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setDoOutput(true); // 응답 결과를 받겠다!

        int responseCode = conn.getResponseCode();
        log.info("/kakaoservice/logout POST!  - {}", responseCode);

        //  응답 데이터 받기
        try (BufferedReader br
                     = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {

            String responseData = br.readLine();
            log.info("responseData - {}", responseData);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
