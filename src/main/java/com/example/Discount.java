package com.example;

import org.bson.types.ObjectId;

public class Discount {
    private ObjectId id;
    private String productId;
    private int discount;

    public Discount() {
    }

    public Discount(ObjectId id, String productId, int discount) {
        this.id = id;
        this.productId = productId;
        this.discount = discount;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
