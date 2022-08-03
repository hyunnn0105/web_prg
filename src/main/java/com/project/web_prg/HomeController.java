package com.project.web_prg;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@Log4j2
public class HomeController {
    @GetMapping("/")
    public String home(Model model, HttpSession session){
        log.info("welcome page open");

        // session을 뒤져서 로그인 요청 찾기
        Object loginUser = session.getAttribute("loginUser");
        /*
        if (loginUser == null) {
            model.addAttribute("login", false);
        } else {
            model.addAttribute("login", true);
        }

         */
        return "index";
    }

    // 일반 컨트롤러에서 json 보내기



    @GetMapping("/haha")
    @ResponseBody // 리턴데이터가 뷰포워딩이 아닌 제이슨으로 전달됨
    public Map<String, String> haha(){
        Map<String, String> map = new HashMap<>();
        map.put("a", "aaa");
        map.put("b", "bb");
        map.put("c", "ccc");
        return map;
    }
}
