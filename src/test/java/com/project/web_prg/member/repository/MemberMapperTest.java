package com.project.web_prg.member.repository;

import com.project.web_prg.member.domain.Auth;
import com.project.web_prg.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MemberMapperTest {

    @Autowired
    MemberMapper mapper;

    @Autowired // 빈등록 -> only 싱글톤
    BCryptPasswordEncoder encoder;

    @Test
    @DisplayName("회원가입이 성공해야 함")
    void registerTest(){
        Member m = new Member();
        m.setAccount("apple123");
        // 이렇게 넣지말고 인코딩 해야함? -> 암호화 알고리즘
        m.setPassword("12345");
        m.setName("사과왕");
        m.setEmail("apple@gmail.com");
        m.setAuth(Auth.ADMIN);

        boolean flag = mapper.resgister(m);

        assertTrue(flag);

    }
    
    @Test
    @DisplayName("비밀번호가 암호화 인코딩이 되어야한다")
    void encodingpasswordTest(){
        // before encoding 
        String rawPassword = "ddd5555";
        
        // 인코딩을 위한 객체 생성
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 인코딩 후 비빌번호
        String encodePassword = encoder.encode(rawPassword);

        System.out.println("rawPassword = " + rawPassword);
        // 디비에 저장해야 하는 password
        System.out.println("encodePassword = " + encodePassword);
    }

    @Test
    @DisplayName("회원가입이 성공해야 하고 비밀번호 인코딩이 성공해야 한다")
    void registerTest2(){
        Member m = new Member();
        m.setAccount("peach");
        // 이렇게 넣지말고 인코딩 해야함? -> 암호화 알고리즘
        m.setPassword(new BCryptPasswordEncoder().encode("1234"));
        m.setName("복숭아");
        m.setEmail("peach@gmail.com");
        m.setAuth(Auth.ADMIN);

        boolean flag = mapper.resgister(m);

        assertTrue(flag);

    }
    
    

    @Test
    @DisplayName("유저 한명만 조회하기")
    void findUserTest(){
        String account = "peach";

        Member member = mapper.findUser(account);
        System.out.println("member = " + member);
        assertEquals("복숭아",member.getName());
    }
    
    // 실패테스트도 돌려보기

    @Test
    @DisplayName("유저 한명만 조회 실패하기")
    void findUserTest2(){
        String account = "peach123";

        Member member = mapper.findUser(account);
        System.out.println("member = " + member);
        assertNull(member);
    }
    
    @Test
    @DisplayName("아이디를 중복확인 할 수 있다")
    void checkAccountTest(){
        Map<String, Object> checkMap = new HashMap<>();
        checkMap.put("type", "account");
        checkMap.put("value", "peach");
        int flag = mapper.isDuplicate(checkMap);

        assertEquals(1, flag);
    }

    @Test
    @DisplayName("이메일을 중복확인 할 수 있다")
    void checkEmailTest(){
        Map<String, Object> checkMap = new HashMap<>();
        checkMap.put("type", "email");
        checkMap.put("value", "peach@gmail.com");
        int flag = mapper.isDuplicate(checkMap);

        assertEquals(1, flag);
    }

    @Test
    @DisplayName("로그인을 검증해야한다")
    void signInTest(){

        // 로그인 시도 계정, 패스워드
        String inputId = "tese1234";
        String inputPw = "!12345678";

        // 로그인 시도한 계정명으로 회원정보를 조회
        Member foundMember = mapper.findUser(inputId);
        // 2. 회원가입 여부를 먼저 확인
        if (foundMember != null) {
            // 3. 패스워드를 대조한다
            // 실제 회원의 비밀번호를 가져온다.
            String dbPw = foundMember.getPassword();
            // 암호화 된 비번이랑 클라이언트네서 입력받은 비번이랑 비교하기
            // 4. 암호화된 패스워드를 디코딩하여 비교 matches(인코딩,db)
            if (encoder.matches(inputPw, dbPw)){
                System.out.println("로그인 성공");
            } else {
                System.out.println("비밀번호가 틀렸습니다");
            }
        } else {
            System.out.println("존재하지 않는 아이디 입니다.");
        }
    }
    
    
}