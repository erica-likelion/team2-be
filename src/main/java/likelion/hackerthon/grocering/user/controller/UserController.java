package likelion.hackerthon.grocering.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import likelion.hackerthon.grocering.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/guest/session")
    public String issueGuestSession(HttpServletRequest request) {
        return userService.saveUser(request);
    }
}
