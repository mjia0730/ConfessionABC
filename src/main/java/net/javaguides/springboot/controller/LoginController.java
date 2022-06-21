package net.javaguides.springboot.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;




@Controller
public class LoginController {     
	
    //page for admin to login where admin will be redirect to admin page
    @PostMapping("/login")
    public String login(HttpServletRequest request) {
    	if(request.getParameter("username").equals("admin") && request.getParameter("password").equals("password"))
    		return "redirect:/admin";
    	else
    		return "redirect:/?invalid";
    }

}