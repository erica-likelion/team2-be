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

    private String preference;

    @Setter
    @Column(unique = true)
    private String guestId; // 서버가 꺼져서 세션 유지가 안되는 상황을 방지하기 위해 guestId도 저장

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Recipe> recipes = new ArrayList<>();

    public void setPreferences(UserPreferences preferences) {
        this.preference = preferences.preference();
    }
}
