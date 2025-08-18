package likelion.hackerthon.grocering.grocery.service;

import likelion.hackerthon.grocering.grocery.dto.SearchedGrocery;
import likelion.hackerthon.grocering.grocery.repository.GroceryRepository;
import likelion.hackerthon.grocering.grocery.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final GroceryRepository groceryRepository;

    public List<SearchedGrocery> getGroceryListByProductName(String productName) {
        return groceryRepository.findGroceriesByProductName(productName);
    }
}
