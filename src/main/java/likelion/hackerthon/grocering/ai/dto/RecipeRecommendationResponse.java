package likelion.hackerthon.grocering.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Schema(description = "AI 레시피 추천 응답 정보")
@Data
public class RecipeRecommendationResponse {
    @Schema(description = "AI가 추천한 레시피 목록 (최대 3개)")
    private List<RecommendedRecipe> recipes;
    
    @Schema(description = "추천된 레시피 정보")
    @Data
    public static class RecommendedRecipe {
        @Schema(description = "요리명", example = "김치찌개")
        private String name;
        
        @Schema(description = "요리 종류", example = "한식")
        private String cuisine;
        
        @Schema(description = "난이도", example = "보통", allowableValues = {"쉬움", "보통", "어려움"})
        private String difficulty;
        
        @Schema(description = "조리시간 (분 단위)", example = "30")
        private Integer cookingTime;
        
        @Schema(description = "요리 썸네일 이미지 URL", example = "https://someimgcdn.com/recipe/korean/kimchi-jjigae.webp")
        private String thumbnail;
        
        @Schema(description = "필요한 재료 목록")
        private List<RecipeIngredient> ingredients;
        
        @Schema(description = "조리 과정 단계별 설명")
        private List<String> instructions;
        
        @Schema(description = "요리에 대한 간단한 설명", example = "매콤하고 따뜻한 한국의 대표적인 찌개 요리입니다.")
        private String description;
    }
    
    @Schema(description = "레시피 재료 정보")
    @Data
    public static class RecipeIngredient {
        @Schema(description = "재료명", example = "김치")
        private String name;
        
        @Schema(description = "필요한 양", example = "300g")
        private String amount;
        
        @Schema(description = "예상 가격", example = "2,000원")
        private String estimatedPrice;
    }
}