package com.epam.zelener.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String title;

    @Column(nullable = false)
    private String description;
    @Column
    private LocalDate prodDate;
    @Column
    private LocalDate expDate;

    @Column
    private LocalDateTime created;

    @Column
    private LocalDateTime updated;


}
