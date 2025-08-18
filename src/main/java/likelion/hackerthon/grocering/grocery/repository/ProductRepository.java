package likelion.hackerthon.grocering.grocery.repository;

import likelion.hackerthon.grocering.grocery.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
