package com.epam.zelener.restaurant.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DynamicInsert
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column
    private String fullName;

    @Column
    private String phoneNumber;

    @Column
    private String address;

    @Email
    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default 'GUEST'")
    private Role role;

    @Column(columnDefinition = "boolean default true")
    private Boolean isActive;

    @Column(columnDefinition = "timestamp default now()")
    private LocalDateTime created;

    @Column(columnDefinition = "timestamp default now()")
    private LocalDateTime updated;

    @Column(columnDefinition = "timestamp default now()")
    private LocalDateTime lastVisit;

}