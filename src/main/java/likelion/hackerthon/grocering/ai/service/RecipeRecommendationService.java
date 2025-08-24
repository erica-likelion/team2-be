package likelion.hackerthon.grocering.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.hackerthon.grocering.ai.dto.RecipeRecommendationRequest;
import likelion.hackerthon.grocering.ai.dto.RecipeRecommendationResponse;
import likelion.hackerthon.grocering.grocery.entity.Product;
import likelion.hackerthon.grocering.grocery.repository.ProductRepository;
import likelion.hackerthon.grocering.user.entity.User;
import likelion.hackerthon.grocering.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeRecommendationService {

    private final GeminiService geminiService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public RecipeRecommendationResponse generateRecommendations(RecipeRecommendationRequest request, Long userId) {
        try {
            // 1. 사용자 온보딩 데이터 가져오기
            String userOnboardingData = getUserOnboardingData(userId);

            // 2. 식료품점 상품 데이터 가져오기
            String storeProducts = getStoreProductsData(request.getStoreId());

            // 3. 추가 요구사항 반영
            String enhancedUserData = enhanceUserDataWithRequirements(userOnboardingData, request.getAdditionalRequirements());

            // 4. Gemini AI 호출 - 구조화된 레시피 객체 반환
            RecipeRecommendationResponse.RecommendedRecipe recipe = geminiService.generateRecipeRecommendation(enhancedUserData, storeProducts);

            // 5. 구조화된 응답 반환
            RecipeRecommendationResponse response = new RecipeRecommendationResponse();
            response.setRecipes(List.of(recipe));
            return response;

        } catch (Exception e) {
            log.error("레시피 추천 생성 중 오류 발생", e);
            
            // 오류 발생 시 기본 응답 반환
            RecipeRecommendationResponse fallbackResponse = new RecipeRecommendationResponse();
            RecipeRecommendationResponse.RecommendedRecipe fallbackRecipe = new RecipeRecommendationResponse.RecommendedRecipe();
            fallbackRecipe.setName("서비스 오류");
            fallbackRecipe.setCuisine("기타");
            fallbackRecipe.setDifficulty("보통");
            fallbackRecipe.setCookingTime(30);
            fallbackRecipe.setThumbnail("https://images.unsplash.com/photo-1546833999-b9f581a1996d");
            fallbackRecipe.setIngredients(List.of());
            fallbackRecipe.setInstructions(List.of("레시피 추천 서비스에 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
            fallbackRecipe.setDescription("레시피 추천 서비스에 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            fallbackResponse.setRecipes(List.of(fallbackRecipe));
            return fallbackResponse;
        }
    }

    private String getUserOnboardingData(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // UserPreferences DTO에서 preference 필드를 직접 사용
        return user.getPreference();
    }

    private String getStoreProductsData(Long storeId) {
        List<Product> products = productRepository.findByGroceryId(storeId);
        if (products.isEmpty()) {
            return "판매 상품 없음";
        }

        return "판매 상품: " + products.stream()
                .map(Product::getName)
                .collect(Collectors.joining(", "));
    }

    private String enhanceUserDataWithRequirements(String userOnboardingData, String additionalRequirements) {
        if (additionalRequirements != null && !additionalRequirements.trim().isEmpty()) {
            return userOnboardingData + ", 추가 요구사항: " + additionalRequirements;
        }
        return userOnboardingData;
    }

}
