package likelion.hackerthon.grocering.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 선호도 정보")
public record UserPreferences(@Schema(description = "프론트에서 포맷팅하고 넘기는 유저 음식 선호도") String preference) {
}
