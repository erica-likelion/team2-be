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

    public RecipeRecommendationResponse generateRecommendations(RecipeRecommendationRequest request) {
        try {
            // 1. 사용자 온보딩 데이터 가져오기
            String userOnboardingData = getUserOnboardingData(request.getUserId());

            // 2. 식료품점 상품 데이터 가져오기
            String storeProducts = getStoreProductsData(request.getStoreId());

            // 3. 추가 요구사항 반영
            String enhancedUserData = enhanceUserDataWithRequirements(userOnboardingData, request.getAdditionalRequirements());

            // 4. Gemini AI 호출
            String aiResponse = geminiService.generateRecipeRecommendation(enhancedUserData, storeProducts);

            // 5. JSON 응답 파싱
            return parseAIResponse(aiResponse);

        } catch (Exception e) {
            log.error("레시피 추천 생성 중 오류 발생", e);
            throw new RuntimeException("레시피 추천 생성에 실패했습니다.", e);
        }
    }

    private String getUserOnboardingData(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        StringBuilder sb = new StringBuilder();
        sb.append("매운맛 선호: ").append(user.getHotFoodPreference()).append(", ");
        sb.append("단맛 선호: ").append(user.getSweetFoodPreference()).append(", ");
        sb.append("짠맛 선호: ").append(user.getSaltyFoodPreference()).append(", ");
        sb.append("선호 조리 방식: ").append(user.getCookingMethodFoodPreference()).append(", ");
        sb.append("최대 조리 시간: ").append(user.getMaxCookingTimePreference()).append(", ");
        sb.append("새로운 음식 시도: ").append(user.getTryingNewFoodPreference()).append(", ");
        sb.append("알레르기 정보: ").append(user.getAllergicFoods()).append(", ");
        sb.append("종교적 금기 음식: ").append(user.getReligionBannedFoods());

        return sb.toString();
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

    private RecipeRecommendationResponse parseAIResponse(String aiResponse) {
        try {
            return objectMapper.readValue(aiResponse, RecipeRecommendationResponse.class);
        } catch (Exception e) {
            log.error("AI 응답 파싱 실패: {}", aiResponse, e);
            throw new RuntimeException("AI 응답을 처리할 수 없습니다.", e);
        }
    }
}
