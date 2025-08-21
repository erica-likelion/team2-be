package likelion.hackerthon.grocering.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "AI 레시피 추천 요청 정보")
@Data
public class RecipeRecommendationRequest {
    @Schema(description = "사용자 ID (세션에서 자동 주입되므로 요청 바디에서 제거됨)", hidden = true)
    private Long userId;
    
    @Schema(description = "식료품점 ID", example = "1", required = true)
    private Long storeId;
    
    @Schema(description = "추가 요구사항 (선택사항)", example = "매운 음식을 원해요, 30분 이내로 만들 수 있는 요리")
    private String additionalRequirements;
}