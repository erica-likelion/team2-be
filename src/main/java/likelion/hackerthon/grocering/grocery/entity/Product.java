package likelion.hackerthon.grocering.grocery.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalTime;

@Entity
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grocery_id")
    private Grocery grocery;
}
