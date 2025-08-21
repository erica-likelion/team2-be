package likelion.hackerthon.grocering.ai.dto;

import lombok.Data;
import java.util.List;

@Data
public class RecipeRecommendationResponse {
    private List<RecommendedRecipe> recipes;
    
    @Data
    public static class RecommendedRecipe {
        private String name;
        private String cuisine;
        private String difficulty;
        private Integer cookingTime; // 분 단위
        private List<RecipeIngredient> ingredients;
        private List<String> instructions;
        private String description;
    }
    
    @Data
    public static class RecipeIngredient {
        private String name;
        private String amount;
        private String estimatedPrice;
    }
}