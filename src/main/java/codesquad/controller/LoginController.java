package codesquad.controller;

import codesquad.UnAuthenticationException;
import codesquad.domain.User;
import codesquad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping("/login/form")
    public String loginForm() {
        return "user/form";
    }

    @PostMapping("/login")
    public String login(String userId, String password) {
        try {
            User user = userService.login(userId, password);
            return "redirect:/";
        } catch (Exception e) {
            return "user/login_failed";
        }
    }
}
