package com.epam.zelener.restaurant.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DynamicInsert
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id", unique = true)
    private int id;

    @Column(unique = true)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(columnDefinition = "boolean default true")
    private Boolean isActive;

    @Column(nullable = false)
    private LocalDate prodDate;

    @Column(nullable = false)
    private LocalDate expDate;

    @Column(columnDefinition = "timestamp default now()")
    private LocalDateTime created;

    @Column(columnDefinition = "timestamp default now()")
    private LocalDateTime updated;

}
