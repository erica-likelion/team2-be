package likelion.hackerthon.grocering.user.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hotFoodPreference;

    private String sweetFoodPreference;

    private String saltyFoodPreference;

    private String cookingMethodFoodPreference;

    private String maxCookingTimePreference;

    private String tryingNewFoodPreference;

    private String allergicFoods; // 얘하고 밑에 종교 금지 음식은 프론트에게 리스트 말고 복숭아,바나나,딸기 이런 식으로 문자열로 받을 생각입니다.

    private String religionBannedFoods;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Recipe> recipes = new ArrayList<>();
}
