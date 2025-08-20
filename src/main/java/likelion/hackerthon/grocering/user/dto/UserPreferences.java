package likelion.hackerthon.grocering.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 선호도 정보")
public record UserPreferences(@Schema(description = "매운 음식 선호도") String hotFoodPreference,
                              @Schema(description = "단 음식 선호도") String sweetFoodPreference,
                              @Schema(description = "짠 음식 선호도") String saltyFoodPreference,
                              @Schema(description = "조리 방법 선호도") String cookingMethodFoodPreference,
                              @Schema(description = "최대 조리 시간 선호도") String maxCookingTimePreference,
                              @Schema(description = "새로운 음식 시도 선호도") String tryingNewFoodPreference,
                              @Schema(description = "알러지 음식", example = "복숭아, 땅콩") String allergicFoods,
                              @Schema(description = "종교적으로 금지된 음식", example = "돼지고기, 소고기") String religionBannedFoods) {
}
