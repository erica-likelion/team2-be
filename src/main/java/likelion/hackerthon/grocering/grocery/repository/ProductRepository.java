package likelion.hackerthon.grocering.grocery.repository;

import likelion.hackerthon.grocering.grocery.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByGroceryId(Long groceryId);
}
