package com.epam.zelener.restaurant.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
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

    @Column(columnDefinition = "varchar(50) default 'UKRAINE'")
    private String address;

    @Email
    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String confirmedPassword;

    @Column
    private String dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default 'GUEST'")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default 'ACTIVE'")
    private Status status;

    @Column(columnDefinition = "timestamp default now()")
    private LocalDateTime created;

    @Column(columnDefinition = "timestamp default now()")
    private LocalDateTime updated;

    @Column(columnDefinition = "timestamp default now()")
    private LocalDateTime lastVisit;

}
