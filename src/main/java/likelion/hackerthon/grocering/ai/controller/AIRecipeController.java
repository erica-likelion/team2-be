package likelion.hackerthon.grocering.ai.controller;

import likelion.hackerthon.grocering.ai.dto.RecipeRecommendationRequest;
import likelion.hackerthon.grocering.ai.dto.RecipeRecommendationResponse;
import likelion.hackerthon.grocering.ai.service.RecipeRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIRecipeController {
    
    private final RecipeRecommendationService recipeRecommendationService;
    
    @PostMapping("/recipes/recommend")
    public ResponseEntity<RecipeRecommendationResponse> recommendRecipes(
            @RequestBody RecipeRecommendationRequest request) {
        
        RecipeRecommendationResponse response = recipeRecommendationService.generateRecommendations(request);
        return ResponseEntity.ok(response);
    }
}