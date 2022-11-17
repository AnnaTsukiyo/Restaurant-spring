package com.epam.zelener.restaurant.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DynamicInsert
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manager_id")
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int age;
    @Column(nullable = false)
    private String userPhoneNumber;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default 'MANAGER'")
    private Role role;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default 'ACTIVE'")
    private Status status;
    @Column(nullable = false)
    private String position;
    @Column(nullable = false)
    private double salary;

    public Manager(String name, int age, String userPhoneNumber, Role role, String position, double salary) {
        this.name = name;
        this.age = age;
        this.userPhoneNumber = userPhoneNumber;
        this.role = role;
        this.position = position;
        this.salary = salary;
    }
}
