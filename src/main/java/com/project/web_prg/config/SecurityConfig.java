package com.project.web_prg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 시큐리티 설정을 웹에 적용
public class SecurityConfig {

    // 시큐리티 기본 설정을 처리하는 빈
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // 초기에 나오는 디폴트 로그인 화면 안뜨게 하기
        http.csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/member/**")
                .permitAll();
        // csrf().disable() csrf공격 방어 토큰 생성 해제
        // .authorizeHttpRequests() 권한 요청 범위 설정
        // .permitAll(); /member/로 시작하는 요청은 따로 권한 검증 하지말아라
        // .antMatchers("/board/**").hasRole("ADMIN") -> board/**은 어드민만 들어와라!
        return http.build();
    }

}
