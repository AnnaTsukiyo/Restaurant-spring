package com.epam.zelener.restaurant.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DynamicInsert
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dish_id", unique = true)
    private Long id;

    @Column
    private int price;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Categories category;

    @Column(columnDefinition = "boolean default true")
    private Boolean isActive;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "dish_id")
    private List<Ingredient> ingredientList;
}
