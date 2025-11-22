package com.msvc.order.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroPedido;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderid")
    private List<OrderLineItems>orderLineItems = new ArrayList<>();
}
