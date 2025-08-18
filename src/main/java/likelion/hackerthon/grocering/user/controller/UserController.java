package likelion.hackerthon.grocering.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import likelion.hackerthon.grocering.user.dto.GuestIdData;
import likelion.hackerthon.grocering.user.dto.UserPreferences;
import likelion.hackerthon.grocering.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/guest/session")
    public String issueGuestSession(HttpServletRequest request) {
        return userService.saveUser(request);
    }

    @PostMapping("/user/preferences")
    public ResponseEntity<String> saveUserPreferences(@RequestBody UserPreferences userPreferences,
                                                      @SessionAttribute(name = "userId", required = true) String userId) {
        userService.savePreferences(userPreferences, Long.valueOf(userId));
        return ResponseEntity.ok("save user preferences");
    }

    @PostMapping("/renew/session")
    public String renewSession(@RequestBody GuestIdData guestIdData, HttpServletRequest request) {
        userService.renewSession(request, guestIdData.guestId());
        return "renew session";
    }
}
