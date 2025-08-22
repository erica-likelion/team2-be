package likelion.hackerthon.grocering.recipe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import likelion.hackerthon.grocering.recipe.dto.RecipeScrapListResponse;
import likelion.hackerthon.grocering.recipe.dto.RecipeScrapRequest;
import likelion.hackerthon.grocering.recipe.dto.RecipeScrapResponse;
import likelion.hackerthon.grocering.recipe.service.RecipeScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "레시피 스크랩(Recipe Scrap)", description = "레시피 스크랩 및 즐겨찾기 관련 API 입니다.")
@RestController
@RequestMapping("/api/recipe/scraps")
@RequiredArgsConstructor
public class RecipeScrapController {
    
    private final RecipeScrapService recipeScrapService;
    
    @Operation(summary = "레시피 스크랩", description = "AI가 추천한 레시피를 스크랩(즐겨찾기)에 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "스크랩 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 이미 스크랩한 레시피"),
            @ApiResponse(responseCode = "401", description = "세션이 만료되었거나 유효하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<RecipeScrapResponse> scrapRecipe(
            @Valid @RequestBody RecipeScrapRequest request,
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = true) String userId) {
        
        RecipeScrapResponse response = recipeScrapService.scrapRecipe(Long.valueOf(userId), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "내 스크랩 레시피 목록 조회 (페이징)", description = "사용자가 스크랩한 레시피 목록을 페이징으로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "세션이 만료되었거나 유효하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<RecipeScrapListResponse> getMyScrapList(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지당 항목 수", example = "10") 
            @RequestParam(defaultValue = "10") int size,
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = true) String userId) {
        
        RecipeScrapListResponse response = recipeScrapService.getUserScrapList(Long.valueOf(userId), page, size);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "내 스크랩 레시피 전체 목록 조회", description = "사용자가 스크랩한 모든 레시피를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "세션이 만료되었거나 유효하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/all")
    public ResponseEntity<List<RecipeScrapResponse>> getAllMyScrap(
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = true) String userId) {
        
        List<RecipeScrapResponse> response = recipeScrapService.getAllUserScraps(Long.valueOf(userId));
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "스크랩 레시피 상세 조회", description = "특정 스크랩 레시피의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "세션이 만료되었거나 유효하지 않음"),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없음"),
            @ApiResponse(responseCode = "404", description = "스크랩 레시피를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{scrapId}")
    public ResponseEntity<RecipeScrapResponse> getScrapDetail(
            @Parameter(description = "스크랩 ID", example = "1") 
            @PathVariable Long scrapId,
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = true) String userId) {
        
        RecipeScrapResponse response = recipeScrapService.getScrapDetail(Long.valueOf(userId), scrapId);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "스크랩 레시피 삭제", description = "스크랩한 레시피를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "세션이 만료되었거나 유효하지 않음"),
            @ApiResponse(responseCode = "403", description = "삭제 권한이 없음"),
            @ApiResponse(responseCode = "404", description = "스크랩 레시피를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{scrapId}")
    public ResponseEntity<Void> deleteScrap(
            @Parameter(description = "스크랩 ID", example = "1") 
            @PathVariable Long scrapId,
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = true) String userId) {
        
        recipeScrapService.deleteScrap(Long.valueOf(userId), scrapId);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "식료품점별 스크랩 레시피 조회", description = "특정 식료품점 기반으로 스크랩한 레시피들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "세션이 만료되었거나 유효하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/grocery/{groceryId}")
    public ResponseEntity<List<RecipeScrapResponse>> getScrapsByGrocery(
            @Parameter(description = "식료품점 ID", example = "1") 
            @PathVariable Long groceryId,
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = true) String userId) {
        
        List<RecipeScrapResponse> response = recipeScrapService.getScrapsByGrocery(Long.valueOf(userId), groceryId);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "요리 유형별 스크랩 레시피 조회", description = "특정 요리 유형(한식, 중식 등)으로 스크랩한 레시피들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "세션이 만료되었거나 유효하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/cuisine/{cuisineType}")
    public ResponseEntity<List<RecipeScrapResponse>> getScrapsByCuisineType(
            @Parameter(description = "요리 유형", example = "한식") 
            @PathVariable String cuisineType,
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = true) String userId) {
        
        List<RecipeScrapResponse> response = recipeScrapService.getScrapsByCuisineType(Long.valueOf(userId), cuisineType);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "내 스크랩 개수 조회", description = "사용자가 스크랩한 레시피의 총 개수를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "세션이 만료되었거나 유효하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/count")
    public ResponseEntity<Long> getMyScrapCount(
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = true) String userId) {
        
        long count = recipeScrapService.getUserScrapCount(Long.valueOf(userId));
        return ResponseEntity.ok(count);
    }
    
    @Operation(summary = "레시피 스크랩 여부 확인", description = "특정 레시피가 이미 스크랩되어 있는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "확인 완료"),
            @ApiResponse(responseCode = "401", description = "세션이 만료되었거나 유효하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkScrapStatus(
            @Parameter(description = "레시피명", example = "김치찌개") 
            @RequestParam String recipeName,
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = true) String userId) {
        
        boolean isScrapped = recipeScrapService.isAlreadyScrapped(Long.valueOf(userId), recipeName);
        return ResponseEntity.ok(isScrapped);
    }
}