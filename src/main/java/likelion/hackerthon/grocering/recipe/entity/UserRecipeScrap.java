package likelion.hackerthon.grocering.recipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_recipe_scraps")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRecipeScrap {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "recipe_name", nullable = false, length = 200)
    private String recipeName;
    
    @Column(name = "recipe_content", columnDefinition = "TEXT")
    private String recipeContent;
    
    @Column(name = "cuisine_type", length = 50)
    private String cuisineType;
    
    @Column(name = "difficulty", length = 20)
    private String difficulty;
    
    @Column(name = "cooking_time")
    private Integer cookingTime;
    
    @Column(name = "grocery_id")
    private Long groceryId;
    
    @Column(name = "grocery_name", length = 100)
    private String groceryName;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}