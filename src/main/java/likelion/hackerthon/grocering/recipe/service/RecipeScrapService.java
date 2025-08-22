package likelion.hackerthon.grocering.recipe.service;

import likelion.hackerthon.grocering.recipe.dto.RecipeScrapListResponse;
import likelion.hackerthon.grocering.recipe.dto.RecipeScrapRequest;
import likelion.hackerthon.grocering.recipe.dto.RecipeScrapResponse;
import likelion.hackerthon.grocering.recipe.entity.UserRecipeScrap;
import likelion.hackerthon.grocering.recipe.repository.UserRecipeScrapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeScrapService {
    
    private final UserRecipeScrapRepository recipeScrapRepository;
    
    /**
     * 레시피 스크랩 저장
     */
    @Transactional
    public RecipeScrapResponse scrapRecipe(Long userId, RecipeScrapRequest request) {
        // 중복 스크랩 체크
        if (recipeScrapRepository.existsByUserIdAndRecipeName(userId, request.getRecipeName())) {
            throw new RuntimeException("이미 스크랩한 레시피입니다: " + request.getRecipeName());
        }
        
        UserRecipeScrap scrap = UserRecipeScrap.builder()
                .userId(userId)
                .recipeName(request.getRecipeName())
                .recipeContent(request.getRecipeContent())
                .cuisineType(request.getCuisineType())
                .difficulty(request.getDifficulty())
                .cookingTime(request.getCookingTime())
                .groceryId(request.getGroceryId())
                .groceryName(request.getGroceryName())
                .build();
        
        UserRecipeScrap savedScrap = recipeScrapRepository.save(scrap);
        log.info("레시피 스크랩 저장 완료 - 사용자: {}, 레시피: {}", userId, request.getRecipeName());
        
        return convertToResponse(savedScrap);
    }
    
    /**
     * 사용자별 스크랩 레시피 목록 조회 (페이징)
     */
    public RecipeScrapListResponse getUserScrapList(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserRecipeScrap> scrapPage = recipeScrapRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        
        List<RecipeScrapResponse> recipes = scrapPage.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        return RecipeScrapListResponse.builder()
                .totalCount(scrapPage.getTotalElements())
                .currentPage(scrapPage.getNumber())
                .totalPages(scrapPage.getTotalPages())
                .pageSize(scrapPage.getSize())
                .recipes(recipes)
                .build();
    }
    
    /**
     * 사용자별 스크랩 레시피 전체 목록 조회
     */
    public List<RecipeScrapResponse> getAllUserScraps(Long userId) {
        List<UserRecipeScrap> scraps = recipeScrapRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return scraps.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 특정 스크랩 레시피 상세 조회
     */
    public RecipeScrapResponse getScrapDetail(Long userId, Long scrapId) {
        UserRecipeScrap scrap = recipeScrapRepository.findById(scrapId)
                .orElseThrow(() -> new RuntimeException("스크랩 레시피를 찾을 수 없습니다: " + scrapId));
        
        // 본인 스크랩인지 확인
        if (!scrap.getUserId().equals(userId)) {
            throw new RuntimeException("접근 권한이 없습니다.");
        }
        
        return convertToResponse(scrap);
    }
    
    /**
     * 스크랩 레시피 삭제
     */
    @Transactional
    public void deleteScrap(Long userId, Long scrapId) {
        UserRecipeScrap scrap = recipeScrapRepository.findById(scrapId)
                .orElseThrow(() -> new RuntimeException("스크랩 레시피를 찾을 수 없습니다: " + scrapId));
        
        // 본인 스크랩인지 확인
        if (!scrap.getUserId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        
        recipeScrapRepository.delete(scrap);
        log.info("레시피 스크랩 삭제 완료 - 사용자: {}, 스크랩 ID: {}", userId, scrapId);
    }
    
    /**
     * 특정 식료품점 기반 스크랩 레시피 조회
     */
    public List<RecipeScrapResponse> getScrapsByGrocery(Long userId, Long groceryId) {
        List<UserRecipeScrap> scraps = recipeScrapRepository.findByUserIdAndGroceryIdOrderByCreatedAtDesc(userId, groceryId);
        return scraps.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 요리 유형별 스크랩 레시피 조회
     */
    public List<RecipeScrapResponse> getScrapsByCuisineType(Long userId, String cuisineType) {
        List<UserRecipeScrap> scraps = recipeScrapRepository.findByUserIdAndCuisineType(userId, cuisineType);
        return scraps.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 사용자 스크랩 개수 조회
     */
    public long getUserScrapCount(Long userId) {
        return recipeScrapRepository.countByUserId(userId);
    }
    
    /**
     * 스크랩 중복 체크
     */
    public boolean isAlreadyScrapped(Long userId, String recipeName) {
        return recipeScrapRepository.existsByUserIdAndRecipeName(userId, recipeName);
    }
    
    /**
     * Entity를 Response DTO로 변환
     */
    private RecipeScrapResponse convertToResponse(UserRecipeScrap scrap) {
        return RecipeScrapResponse.builder()
                .id(scrap.getId())
                .recipeName(scrap.getRecipeName())
                .recipeContent(scrap.getRecipeContent())
                .cuisineType(scrap.getCuisineType())
                .difficulty(scrap.getDifficulty())
                .cookingTime(scrap.getCookingTime())
                .groceryId(scrap.getGroceryId())
                .groceryName(scrap.getGroceryName())
                .createdAt(scrap.getCreatedAt())
                .build();
    }
}