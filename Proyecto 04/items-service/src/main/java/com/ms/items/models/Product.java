package com.ms.items.models;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
public class Product {
    private Long id;
    private String name;
    private Double price;
    private LocalDate createAt;

}
