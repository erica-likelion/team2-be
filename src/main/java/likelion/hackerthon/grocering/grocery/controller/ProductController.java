package likelion.hackerthon.grocering.grocery.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion.hackerthon.grocering.grocery.dto.SearchedGrocery;
import likelion.hackerthon.grocering.grocery.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
