package com.example;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


@QuarkusTest
public class DiscountGrpcServiceTest {

        @GrpcClient
        DiscountServiceGrpc client;

        @BeforeEach
        void beforeEach() {
            final DiscountRepository discountRepository = new DiscountRepository();
            discountRepository.deleteAll().await().indefinitely();
        }

        @Test
        public void shouldCreateDiscount() {
            Discount discount = Discount.newBuilder().setProductId("1").setDiscount(10).build();
            int discountNum = client.create(discount).await().indefinitely().getDiscount();
            assertEquals(10, discountNum);
        }
}
