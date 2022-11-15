package com.epam.zelener.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DynamicInsert
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", unique = true)
    private long orderId;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "dish_id")
    private Long dishId;
    @Column
    private Double totalPrice;
    @Column(columnDefinition = "timestamp default now()")
    private LocalDateTime created;
    @Column
    private String methodOfReceiving;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default 'NEW'")
    private OrderStatus status;

}
