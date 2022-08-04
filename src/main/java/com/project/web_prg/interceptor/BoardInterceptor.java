package com.project.web_prg.interceptor;

import com.project.web_prg.board.mybatis.dto.ValridateMemberDTO;
import com.project.web_prg.util.LoginUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

// 인터셉터 : 컨트롤러에 요청이 들어가기 전, 후에 공통처리 할 일 들을 정의해놓는 클래스
@Configuration
@Log4j2
public class BoardInterceptor implements HandlerInterceptor {
    // defult면 오버라이딩 강요X

    // LOGIN X시 하는 행동들이 튕김
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
//        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/views/member/sign-in.jsp");
        if (session.getAttribute("loginUser") == null) {
            log.info("this request deny!! 집에 가");
//            dispatcher.forward(request, response);

            response.sendRedirect("/member/sign-in?message=no-login");
            return false;
        }
        return true;
    }


    // modify랑 delete에서도 삭제하게하기 -> config 설정
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        // PostHandle이 작동해야하는 URI 목록
        List<String> uriList = Arrays.asList("/board/modify", "/board/delete");

        String requestUri = request.getRequestURI();
        log.info("requestUri - {}", requestUri);
        // 현재 요청 메서드 정보 확인!! 이것도 확인 할 수 있다
        String method = request.getMethod();
        log.info("metode 현재 요청 메서드 정보 확인!! - {}", method);




        if (uriList.contains(requestUri) && method.equalsIgnoreCase("GET")) {
            log.info("board interceptor postHandle() ----- ! ");
            HttpSession session = request.getSession();

            // controller의 메서드를 처리 한 수 모델에 담긴 데이터의 맵
            Map<String, Object> modelMap = modelAndView.getModel();
            ValridateMemberDTO dto = (ValridateMemberDTO) modelMap.get("validate");
            log.info("dto - {}", dto);

            log.info("modelMap - {}", modelMap);
            log.info("modelMap.size - {}", modelMap.size());


            // 요청을 보낸 아이디랑 게시글의 계정명이랑 일치해야한다
            log.info("login account - {}", LoginUtils.getCurrnetUtil(session));
            if (isAdmin(session)) return;

            // 수정하려는 게시글의 계정명 정보와 세션에 저장된 계정명 정보가 일치하지 않으면 돌려보내라~
            if (!isMine(session, dto.getAccount())){
                response.sendRedirect("/board/list");
            }
        }

    }

    private boolean isAdmin(HttpSession session) {
        return LoginUtils.getCurrnetMemberAuth(session).equals("ADMIN");
    }

    // 게시글 정보 == 세션정보 -> 나다.
    private static boolean isMine(HttpSession session, String account) {
        return account.equals(LoginUtils.getCurrnetUtil(session));
    }


}
