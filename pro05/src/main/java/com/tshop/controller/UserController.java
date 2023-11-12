package com.tshop.controller;

import com.tshop.entity.User;
import com.tshop.service.UserService;
import com.tshop.util.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/member")
public class UserController {
    @Autowired
    private UserService userService;


    //회원 가입
    @GetMapping("/join")
    public String joinForm(Model model, User user){
        model.addAttribute("user", user);
        return "member/joinForm";
    }

    @PostMapping("joinPro")
    public String joinPro(User user, Integer roleId, Model model){
        userService.save(user, 3); // 추후 회원가입 시 고르는 것으로 고려 ( 관리자가 권한 부여 하는 방법으로 하는 것도 나쁘지 않을 듯)
        model.addAttribute("user", user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(Model model, LoginForm loginForm){
        model.addAttribute("loginForm", loginForm);
        return "member/login";
    }


    @PostMapping("/auth")
    public String loginPro(User user, HttpSession session, Model model){
        User result = userService.loginPro(user);
        if(result!=null){
            session.setAttribute("userSid", result);
            model.addAttribute("user", user);
        } else {
            session.invalidate();
        }
        return "redirect:/";
    }

    //로그아웃
    @GetMapping("/logout")
    public String logout(Model model, HttpSession session){
        session.invalidate();
        model.addAttribute("msg", "로그아웃 되었습니다.");
        return "redirect:/";
    }

}
