package likelion.hackerthon.grocering.grocery.service;

import likelion.hackerthon.grocering.grocery.dto.GroceryProductsResponse;
import likelion.hackerthon.grocering.grocery.dto.ProductResponse;
import likelion.hackerthon.grocering.grocery.dto.SearchedGrocery;
import likelion.hackerthon.grocering.grocery.entity.Grocery;
import likelion.hackerthon.grocering.grocery.entity.Product;
import likelion.hackerthon.grocering.grocery.repository.GroceryRepository;
import likelion.hackerthon.grocering.grocery.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final GroceryRepository groceryRepository;

    public List<SearchedGrocery> getGroceryListByProductName(String productName) {
        return groceryRepository.findGroceriesByProductName(productName);
    }

    public GroceryProductsResponse getProductsByGroceryId(Long groceryId) {
        Grocery grocery = groceryRepository.findById(groceryId)
                .orElseThrow(() -> new IllegalArgumentException("식료품점을 찾을 수 없습니다. ID: " + groceryId));

        List<Product> products = productRepository.findByGroceryId(groceryId);
        
        List<ProductResponse> productResponses = products.stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getImageUrl()
                ))
                .collect(Collectors.toList());

        return new GroceryProductsResponse(
                grocery.getId(),
                grocery.getName(),
                grocery.getShotAddress(),
                grocery.getCountry(),
                grocery.getOpenTime(),
                grocery.getCloseTime(),
                productResponses
        );
    }
}
