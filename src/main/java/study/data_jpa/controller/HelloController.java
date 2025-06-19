package study.data_jpa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //Json /XML 등의 데이터 전달 (페이지X)
public class HelloController {

    @GetMapping("/hello")
    public  String hello(){
        return "hello";
    }
}


