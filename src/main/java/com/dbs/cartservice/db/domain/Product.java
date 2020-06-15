package com.dbs.cartservice.db.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity @Getter @Setter @Builder
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
}
