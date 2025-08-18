package likelion.hackerthon.grocering.grocery.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Grocery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double lat;

    private Double lng; // 위도 경도는 double로 일단 저장하겠습니다

    private String name;

    private String shotAddress; // 안산 상록구 사동, 시흥시 배곧동 이런 주소

    private String country;

    private LocalTime openTime;

    private LocalTime closeTime;

    @OneToMany(mappedBy = "grocery" , cascade = CascadeType.REMOVE)
    private List<Product> products = new ArrayList<>();
}
