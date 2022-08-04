package com.project.web_prg.config;

import com.project.web_prg.interceptor.AfterLoginInterceptor;
import com.project.web_prg.interceptor.AutoLoginInterceptor;
import com.project.web_prg.interceptor.BoardInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 다양한 인터셉터들을 관리하는 설정 클래스
@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final BoardInterceptor boardInterceptor;
    private final AfterLoginInterceptor afterLoginInterceptor;
    // 자동로그인
    private final AutoLoginInterceptor autoLoginInterceptor;




    // 인터셉터 설정 추가 메서드
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 게시판 인터셉터 설정
        registry.addInterceptor(boardInterceptor)
                .addPathPatterns("/board/*")
                .excludePathPatterns("/board/list", "/board/content");

        // 애프터 로그인 인터셉터 설정
        registry.addInterceptor(afterLoginInterceptor)
                .addPathPatterns("/member/sign-in", "/member/sign-up");

        // 자동로그인 인터셉트 설정
        registry.addInterceptor(autoLoginInterceptor)
                .addPathPatterns("/**"); // "/**" 별 두개는 모든 경로 -> 모든 경로에서 쿠키검사함
    }



}
