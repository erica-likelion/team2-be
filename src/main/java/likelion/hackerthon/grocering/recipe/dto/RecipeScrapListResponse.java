package likelion.hackerthon.grocering.recipe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "스크랩된 레시피 목록 응답")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeScrapListResponse {
    
    @Schema(description = "총 스크랩 개수", example = "15")
    private long totalCount;
    
    @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
    private int currentPage;
    
    @Schema(description = "총 페이지 수", example = "3")
    private int totalPages;
    
    @Schema(description = "페이지당 항목 수", example = "10")
    private int pageSize;
    
    @Schema(description = "스크랩된 레시피 목록")
    private List<RecipeScrapResponse> recipes;
}