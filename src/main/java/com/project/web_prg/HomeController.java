package com.project.web_prg;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@Log4j2
public class HomeController {
    @GetMapping("/")
    public String home(){
        log.info("welcome page open");
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
