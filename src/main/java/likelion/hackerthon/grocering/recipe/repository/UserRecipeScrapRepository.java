package likelion.hackerthon.grocering.recipe.repository;

import likelion.hackerthon.grocering.recipe.entity.UserRecipeScrap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRecipeScrapRepository extends JpaRepository<UserRecipeScrap, Long> {
    
    /**
     * 사용자별 스크랩한 레시피 목록 조회 (페이징)
     */
    Page<UserRecipeScrap> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    /**
     * 사용자별 스크랩한 레시피 목록 조회 (전체)
     */
    List<UserRecipeScrap> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * 특정 사용자의 특정 레시피 스크랩 존재 여부 확인
     */
    boolean existsByUserIdAndRecipeName(Long userId, String recipeName);
    
    /**
     * 특정 사용자의 특정 레시피 스크랩 조회
     */
    Optional<UserRecipeScrap> findByUserIdAndRecipeName(Long userId, String recipeName);
    
    /**
     * 사용자별 스크랩 개수 조회
     */
    long countByUserId(Long userId);
    
    /**
     * 특정 식료품점 기반 스크랩 레시피 조회
     */
    List<UserRecipeScrap> findByUserIdAndGroceryIdOrderByCreatedAtDesc(Long userId, Long groceryId);
    
    /**
     * 요리 유형별 스크랩 레시피 조회
     */
    @Query("SELECT u FROM UserRecipeScrap u WHERE u.userId = :userId AND u.cuisineType = :cuisineType ORDER BY u.createdAt DESC")
    List<UserRecipeScrap> findByUserIdAndCuisineType(@Param("userId") Long userId, @Param("cuisineType") String cuisineType);
}