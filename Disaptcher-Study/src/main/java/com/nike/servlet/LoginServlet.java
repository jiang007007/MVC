package com.nike.servlet;

import com.nike.annotation.Controller;
import com.nike.annotation.ReqestMapping;

@Controller
@ReqestMapping("/hello")
public class LoginServlet {
    @ReqestMapping("/word")
    public void say(){
        System.out.println("hello  client");
    }
}
