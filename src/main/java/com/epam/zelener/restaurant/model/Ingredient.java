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
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id", unique = true)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ingredient_id")
    private List<Food> foodList;

    @Column(columnDefinition = "boolean default true")
    private boolean isActive;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int units;

}
