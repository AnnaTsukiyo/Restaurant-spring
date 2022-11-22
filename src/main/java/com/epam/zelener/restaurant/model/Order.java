package com.epam.zelener.restaurant.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DynamicInsert
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "single_order_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User userId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_order_id")
    private List<Dish> dishList;
    @Column
    private Double totalPrice;

    @Column
    private String methodOfReceiving;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(30) default 'NEW'")
    private OrderStatus status;

    @Column(columnDefinition = "timestamp default now()")
    private LocalDateTime created;

    @Column(columnDefinition = "timestamp default now()")
    private LocalDateTime updated;

}
