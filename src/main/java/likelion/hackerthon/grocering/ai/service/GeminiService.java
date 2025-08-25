package likelion.hackerthon.grocering.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.hackerthon.grocering.ai.dto.RecipeRecommendationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {

    private final WebClient geminiWebClient;
    private final ObjectMapper objectMapper;

    @Value("${google.ai.api-key}")
    private String apiKey;

    @Value("${google.ai.model}")
    private String model;

    public RecipeRecommendationResponse.RecommendedRecipe generateRecipeRecommendation(String userOnboardingData, String storeProducts) {
        String prompt = buildRecipePrompt(userOnboardingData, storeProducts);

        try {
            // Gemini API 요청 바디 구성
            Map<String, Object> requestBody = Map.of(
                "contents", Collections.singletonList(
                    Map.of("parts", Collections.singletonList(
                        Map.of("text", prompt)
                    ))
                )
            );

            // REST API 호출
            String response = geminiWebClient
                .post()
                .uri("/v1/models/{model}:generateContent?key={apiKey}", model, apiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            return parseRecipeFromResponse(response);

        } catch (Exception e) {
            log.error("Gemini AI 호출 중 오류 발생", e);
            throw new RuntimeException("AI 레시피 추천 생성에 실패했습니다.", e);
        }
    }

    private String extractTextFromResponse(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode candidates = root.path("candidates");
            
            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode firstCandidate = candidates.get(0);
                JsonNode content = firstCandidate.path("content");
                JsonNode parts = content.path("parts");
                
                if (parts.isArray() && parts.size() > 0) {
                    return parts.get(0).path("text").asText();
                }
            }
            
            return "레시피 추천을 생성할 수 없습니다.";
            
        } catch (Exception e) {
            log.error("AI 응답 파싱 중 오류 발생", e);
            return "AI 응답을 처리할 수 없습니다.";
        }
    }

    private RecipeRecommendationResponse.RecommendedRecipe parseRecipeFromResponse(String jsonResponse) {
        try {
            // Gemini 응답에서 텍스트 추출
            String aiResponseText = extractTextFromResponse(jsonResponse);
            
            // 마크다운 코드블록 제거 (```json ... ``` 또는 ``` ... ```)
            String cleanedJson = cleanMarkdownCodeBlocks(aiResponseText);
            
            // AI가 반환한 JSON 텍스트를 파싱
            RecipeRecommendationResponse.RecommendedRecipe recipe = objectMapper.readValue(
                cleanedJson, RecipeRecommendationResponse.RecommendedRecipe.class
            );
            
            return recipe;
            
        } catch (Exception e) {
            log.error("AI 응답 파싱 중 오류 발생", e);
            
            // 파싱 실패 시 기본 레시피 반환
            RecipeRecommendationResponse.RecommendedRecipe fallbackRecipe = new RecipeRecommendationResponse.RecommendedRecipe();
            fallbackRecipe.setName("서비스 오류");
            fallbackRecipe.setCuisine("기타");
            fallbackRecipe.setDifficulty("보통");
            fallbackRecipe.setCookingTime(30);
            fallbackRecipe.setThumbnail("https://images.unsplash.com/photo-1546833999-b9f581a1996d");
            fallbackRecipe.setIngredients(Collections.emptyList());
            fallbackRecipe.setInstructions(Collections.singletonList("레시피 추천 서비스에 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
            fallbackRecipe.setDescription("레시피 추천 서비스에 일시적인 오류가 발생했습니다.");
            
            return fallbackRecipe;
        }
    }

    private String cleanMarkdownCodeBlocks(String text) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }
        
        // 마크다운 코드블록 패턴 제거
        String cleaned = text.trim();
        
        // 마크다운 코드 블록 패턴 제거
        if (cleaned.startsWith("```")) {
            // 첫 번째 줄 제거 (```json 또는 ```)
            int firstNewLine = cleaned.indexOf('\n');
            if (firstNewLine > 0) {
                cleaned = cleaned.substring(firstNewLine + 1);
            }
        }
        
        if (cleaned.endsWith("```")) {
            // 마지막 ``` 제거
            int lastBackticks = cleaned.lastIndexOf("```");
            if (lastBackticks > 0) {
                cleaned = cleaned.substring(0, lastBackticks);
            }
        }
        
        return cleaned.trim();
    }

    private String buildRecipePrompt(String userOnboardingData, String storeProducts) {
        return String.format("""
            당신은 외국 식료품점의 재료를 바탕으로 실존하는 외국 요리 레시피를 추천하는 전문가입니다.
            해당 식료품점에서 판매하지 않는 재료는 표시를 해주세요.
            
            사용자 선호도:
            %s

            현재 식료품점 판매 상품:
            %s

            위 정보를 바탕으로 사용자가 가장 좋아할 만한 최상의 레시피 1개를 추천하고, 
            반드시 아래 JSON 형식으로만 응답해주세요. 
            
            중요: 마크다운 코드블록(```)이나 다른 텍스트는 절대 포함하지 말 것. JSON만 반환할 것:

            {
              "name": "요리명",
              "cuisine": "요리 종류 (한식/중식/일식/양식/할랄/러시아/베트남/태국 등)",
              "difficulty": "난이도 (쉬움/보통/어려움 중 하나)",
              "cookingTime": 조리시간_숫자_분단위,
              "thumbnail": "https://example.com/recipe-image.jpg",
              "ingredients": [
                {
                  "name": "재료명",
                  "amount": "필요량",
                  "estimatedPrice": "예상가격"
                }
              ],
              "instructions": [
                "조리과정 1단계",
                "조리과정 2단계",
                "조리과정 3단계"
              ],
              "description": "사용자 선호도를 고려한 추천 이유와 요리 설명을 2-3문장으로"
            }

            요구사항:
            1. 해당 식료품점에서 구매 가능한 재료만 사용
            2. 사용자 선호도를 충분히 반영
            3. thumbnail은 적절한 이미지 URL 생성 (https://images.unsplash.com/photo-* 형태로)
            4. instructions는 상세하고 단계별로 작성
            5. estimatedPrice는 "1,000원" 형태로 표기
            6. 반드시 유효한 JSON 형식으로만 응답
            7. 코드블록(```)이나 추가 설명 절대 금지, 오직 JSON만 반환
            ultrathinking.
            """, userOnboardingData, storeProducts);
    }
}