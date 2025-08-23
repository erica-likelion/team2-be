package likelion.hackerthon.grocering.recipe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "레시피 스크랩 요청 정보")
@Data
public class RecipeScrapRequest {
    
    @Schema(description = "레시피명", example = "김치찌개", required = true)
    @NotBlank(message = "레시피명은 필수입니다")
    @Size(max = 200, message = "레시피명은 200자 이내로 입력해주세요")
    private String recipeName;
    
    @Schema(description = "레시피 전체 내용 (AI 응답)", required = true)
    @NotBlank(message = "레시피 내용은 필수입니다")
    private String recipeContent;
    
    @Schema(description = "요리 종류", example = "한식")
    @Size(max = 50, message = "요리 종류는 50자 이내로 입력해주세요")
    private String cuisineType;
    
    @Schema(description = "난이도", example = "보통", allowableValues = {"쉬움", "보통", "어려움"})
    @Size(max = 20, message = "난이도는 20자 이내로 입력해주세요")
    private String difficulty;
    
    @Schema(description = "조리시간 (분 단위)", example = "30")
    private Integer cookingTime;
    
    @Schema(description = "식료품점 ID", example = "1", required = true)
    @NotNull(message = "식료품점 ID는 필수입니다")
    private Long groceryId;
    
    @Schema(description = "식료품점명", example = "○○ 수입식품마트")
    @Size(max = 100, message = "식료품점명은 100자 이내로 입력해주세요")
    private String groceryName;
}