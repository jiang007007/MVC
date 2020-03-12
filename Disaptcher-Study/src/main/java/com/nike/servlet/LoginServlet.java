package com.nike.servlet;

import com.nike.annotation.Controller;
import com.nike.annotation.ReqestMapping;

@Controller
public class LoginServlet {
    @ReqestMapping
    public void say(){
        System.out.println("hello  client");
    }
}
