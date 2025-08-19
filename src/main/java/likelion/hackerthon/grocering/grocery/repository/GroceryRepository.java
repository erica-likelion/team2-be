package likelion.hackerthon.grocering.grocery.repository;

import likelion.hackerthon.grocering.grocery.dto.SearchedGrocery;
import likelion.hackerthon.grocering.grocery.entity.Grocery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroceryRepository extends JpaRepository<Grocery, Long> {
    @Query("SELECT NEW likelion.hackerthon.grocering.grocery.dto.SearchedGrocery(g.id, g.name, g.shotAddress, g.country, g.openTime, g.closeTime) " +
            "FROM Grocery g JOIN g.products p WHERE p.name = :productName")
    List<SearchedGrocery> findGroceriesByProductName(@Param("productName") String productName);
}
