package likelion.hackerthon.grocering.grocery.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion.hackerthon.grocering.grocery.dto.GroceryProductsResponse;
import likelion.hackerthon.grocering.grocery.dto.SearchedGrocery;
import likelion.hackerthon.grocering.grocery.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "상품(Product)", description = "상품 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "상품 이름으로 식료품 조회", description = "상품 이름으로 식료품을 조회하는 api 입니다.")
    @Parameter(name = "productName", description = "상품 이름")
    @GetMapping("/grocery-list")
    public List<SearchedGrocery> findGroceryByProductName(@RequestParam String productName) {
        return productService.getGroceryListByProductName(productName);
    }

    @Operation(summary = "식료품점별 상품 목록 조회", description = "특정 식료품점의 모든 상품을 조회하는 API입니다. AI 추천을 위한 재료 목록을 제공합니다.")
    @Parameter(name = "groceryId", description = "식료품점 ID")
    @GetMapping("/api/grocery/{groceryId}/products")
    public GroceryProductsResponse getProductsByGroceryId(@PathVariable Long groceryId) {
        return productService.getProductsByGroceryId(groceryId);
    }
}
