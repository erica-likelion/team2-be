package likelion.hackerthon.grocering.recipe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "스크랩된 레시피 정보")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeScrapResponse {
    
    @Schema(description = "스크랩 ID", example = "1")
    private Long id;
    
    @Schema(description = "레시피명", example = "김치찌개")
    private String recipeName;
    
    @Schema(description = "레시피 전체 내용")
    private String recipeContent;
    
    @Schema(description = "요리 종류", example = "한식")
    private String cuisineType;
    
    @Schema(description = "난이도", example = "보통")
    private String difficulty;
    
    @Schema(description = "조리시간 (분 단위)", example = "30")
    private Integer cookingTime;
    
    @Schema(description = "식료품점 ID", example = "1")
    private Long groceryId;
    
    @Schema(description = "식료품점명", example = "○○ 수입식품마트")
    private String groceryName;
    
    @Schema(description = "스크랩 등록일시", example = "2025-08-22T10:30:00")
    private LocalDateTime createdAt;
}