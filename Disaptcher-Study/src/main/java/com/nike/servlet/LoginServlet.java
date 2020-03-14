package com.nike.servlet;

import com.nike.annotation.Controller;
import com.nike.annotation.ReqestMapping;
import com.nike.po.UrpUser;

@Controller
@ReqestMapping("/hello")
public class LoginServlet {
    @ReqestMapping("/word")
    public void say(UrpUser urpUser){
        System.out.println(urpUser);
    }
}
