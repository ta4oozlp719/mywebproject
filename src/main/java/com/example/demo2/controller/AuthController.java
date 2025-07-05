package com.example.demo2.controller;

import com.example.demo2.domain.User;
import com.example.demo2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (!userService.register(user)) {
            model.addAttribute("error", "이미 존재하는 아이디입니다.");
            model.addAttribute("user", user);  // 폼 데이터 유지
            return "register";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {
        if (userService.login(username, password).isPresent()) {
            session.setAttribute("username", username); // 세션에 저장
            return "redirect:/welcome";
        }
        model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
        return "login";
    }


    @GetMapping("/welcome")
    public String welcomePage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username != null ? username : "사용자");
        return "welcome";
    }


    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 초기화
        return "redirect:/login"; // 로그인 페이지로 이동
    }
}
