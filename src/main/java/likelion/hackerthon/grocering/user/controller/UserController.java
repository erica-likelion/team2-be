package likelion.hackerthon.grocering.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "유저(User)", description = "유저 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "게스트 세션 발급", description = "게스트 세션을 발급하는 api 입니다. 프론트 처음 접속 시 요청하시면 됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "세션 발급 성공. 게스트 id를 반환합니다. 프론트에서 저장하고 있다가 혹시 서버가 꺼지거나 해서 401 에러가 뜨면 세션 갱신 API에 사용됩니다.")
    })
    @PostMapping("/guest/session")
    public String issueGuestSession(HttpServletRequest request) {
        return userService.saveUser(request);
    }

    @Operation(summary = "유저 선호도 저장", description = "유저의 선호도를 저장하는 api 입니다.")
    @PostMapping("/user/preferences")
    public ResponseEntity<String> saveUserPreferences(@RequestBody UserPreferences userPreferences,
                                                      @Parameter(hidden = true) @SessionAttribute(name = "userId", required = true) String userId) {
        userService.savePreferences(userPreferences, Long.valueOf(userId));
        return ResponseEntity.ok("save user preferences");
    }

    @Operation(summary = "세션 갱신", description = "세션을 갱신하는 api 입니다. 처음 게스트 세션 발급할 때 받은 게스트 id로 요청하시면 됩니다.")
    @PostMapping("/renew/session")
    public String renewSession(@RequestBody GuestIdData guestIdData, HttpServletRequest request) {
        userService.renewSession(request, guestIdData.guestId());
        return "renew session";
    }
}
