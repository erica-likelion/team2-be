package likelion.hackerthon.grocering.user.entity;

import jakarta.persistence.*;
import likelion.hackerthon.grocering.user.dto.UserPreferences;
import lombok.Getter;
import lombok.Setter;

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

    @Setter
    @Column(unique = true)
    private String guestId; // 서버가 꺼져서 세션 유지가 안되는 상황을 방지하기 위해 guestId도 저장

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Recipe> recipes = new ArrayList<>();

    public void setPreferences(UserPreferences preferences) {
        this.hotFoodPreference = preferences.hotFoodPreference();
        this.sweetFoodPreference = preferences.sweetFoodPreference();
        this.saltyFoodPreference = preferences.saltyFoodPreference();
        this.cookingMethodFoodPreference = preferences.cookingMethodFoodPreference();
        this.maxCookingTimePreference = preferences.maxCookingTimePreference();
        this.tryingNewFoodPreference = preferences.tryingNewFoodPreference();
        this.allergicFoods = preferences.allergicFoods();
        this.religionBannedFoods = preferences.religionBannedFoods();
    }
}
