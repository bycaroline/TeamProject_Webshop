package com.teamproject.garngalore.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    private String id;

    private String userID;

    private List<Product> productList;

    private double totalPrice;

    private double withShipping;

    private int numberOfProducts;

    public void setProducts(List<Product> productList) {
        this.productList = productList;
    }
}
