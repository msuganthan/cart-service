package com.dbs.cartservice.dto;

import com.dbs.cartservice.db.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter @Setter @Builder
public class ProductDetails implements Serializable {
    private String name;
    private String description;
    private BigDecimal price;

    public Product toProduct() {
        return Product.builder()
                      .name(this.name)
                      .description(this.description)
                      .price(this.price)
                      .build();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProductDetails{");
        sb.append("name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", price=").append(price);
        sb.append('}');
        return sb.toString();
    }
}
