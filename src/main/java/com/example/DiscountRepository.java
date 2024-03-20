package com.example;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DiscountRepository implements ReactivePanacheMongoRepository<Discount> {
}
