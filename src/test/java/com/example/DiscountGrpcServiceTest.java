package com.example;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


@QuarkusTest
public class DiscountGrpcServiceTest {

        @GrpcClient
        DiscountService client;

        @BeforeEach
        void beforeEach() {
            final DiscountRepository discountRepository = new DiscountRepository();
            discountRepository.deleteAll().await().indefinitely();
        }

        @Test
        public void shouldCreateDiscount() {
            SetDiscountRequest discount = SetDiscountRequest.newBuilder().setProductId("1").setDiscount(10).build();
            int discountNum = client.setDiscount(discount).await().indefinitely().getDiscount();
            assertEquals(10, discountNum);
        }

        @Test
        public void shouldGetDiscount() {
            SetDiscountRequest discount = SetDiscountRequest.newBuilder().setProductId("1").setDiscount(10).build();
            client.setDiscount(discount).await().indefinitely();
            int discountNum = client.getDiscount(GetDiscountRequest.newBuilder().setProductId("1").build()).await().indefinitely().getDiscount();
            assertEquals(10, discountNum);
        }
}
