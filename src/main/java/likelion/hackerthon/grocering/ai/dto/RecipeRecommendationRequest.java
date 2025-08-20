package likelion.hackerthon.grocering.ai.dto;

import lombok.Data;

@Data
public class RecipeRecommendationRequest {
    private Long userId;
    private Long storeId;
    private String additionalRequirements; // 추가 요구사항 (예: "매운 음식 원함", "30분 이내" 등)
}