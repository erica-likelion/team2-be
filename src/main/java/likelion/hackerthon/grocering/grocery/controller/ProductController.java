package likelion.hackerthon.grocering.grocery.controller;

import likelion.hackerthon.grocering.grocery.dto.SearchedGrocery;
import likelion.hackerthon.grocering.grocery.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/grocery-list")
    public List<SearchedGrocery> findGroceryByProductName(@RequestParam String productName) {
        return productService.getGroceryListByProductName(productName);
    }
}
