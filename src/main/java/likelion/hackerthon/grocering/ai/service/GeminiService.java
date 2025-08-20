package likelion.hackerthon.grocering.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public String generateRecipeRecommendation(String userOnboardingData, String storeProducts) {
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

            return extractTextFromResponse(response);

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

    private String buildRecipePrompt(String userOnboardingData, String storeProducts) {
        return String.format("""
            당신은 외국 식료품점의 재료를 활용한 요리 레시피를 추천하는 전문가입니다.

            사용자 정보:
            %s

            현재 식료품점 판매 상품:
            %s

            위 정보를 바탕으로 다음 조건을 만족하는 레시피 3개를 추천해주세요:
            1. 해당 식료품점에서 구매 가능한 재료만 사용
            2. 사용자의 취향과 요구사항 반영
            3. 조리법을 단계별로 자세히 설명
            4. 예상 조리 시간과 난이도 표시
            5. 각 레시피마다 필요한 재료와 대략적인 가격 정보 포함

            JSON 형태로 응답해주세요:
            {
                "recipes": [
                    {
                        "name": "요리명",
                        "cuisine": "요리 종류 (예: 이탈리안, 일본식 등)",
                        "difficulty": "난이도 (쉬움/보통/어려움)",
                        "cookingTime": "조리시간 (분)",
                        "ingredients": [
                            {
                                "name": "재료명",
                                "amount": "필요량",
                                "estimatedPrice": "예상 가격"
                            }
                        ],
                        "instructions": ["조리과정 1단계", "조리과정 2단계", ...],
                        "description": "요리에 대한 간단한 설명"
                    }
                ]
            }
            """, userOnboardingData, storeProducts);
    }
}