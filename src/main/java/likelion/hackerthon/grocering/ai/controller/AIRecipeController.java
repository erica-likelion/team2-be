package likelion.hackerthon.grocering.ai.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion.hackerthon.grocering.ai.dto.RecipeRecommendationRequest;
import likelion.hackerthon.grocering.ai.dto.RecipeRecommendationResponse;
import likelion.hackerthon.grocering.ai.service.RecipeRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AI 레시피 추천(AI Recipe)", description = "AI 레시피 추천 관련 api 입니다.")
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIRecipeController {
    
    private final RecipeRecommendationService recipeRecommendationService;
    
    @Operation(summary = "AI 레시피 추천", description = "사용자 선호도와 식료품점 재고를 기반으로 AI가 레시피를 추천하는 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "레시피 추천 성공. AI가 생성한 레시피 목록을 반환합니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "세션이 만료되었거나 유효하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류 또는 AI 서비스 오류")
    })
    @PostMapping("/recipes/recommend")
    public ResponseEntity<RecipeRecommendationResponse> recommendRecipes(
            @RequestBody RecipeRecommendationRequest request,
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = true) String userId) {
        
        RecipeRecommendationResponse response = recipeRecommendationService.generateRecommendations(request, Long.valueOf(userId));
        return ResponseEntity.ok(response);
    }
}