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
}