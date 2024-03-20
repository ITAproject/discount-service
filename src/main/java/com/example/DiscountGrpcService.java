package com.example;

import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;

@GrpcService
public class DiscountGrpcService implements DiscountService{

    private final DiscountRepository discountRepository;

    public DiscountGrpcService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Override
    public Uni<DiscountResponse> getDiscount(GetDiscountRequest request) {
        return discountRepository.find("productId", request.getProductId())
                .firstResult()
                .onItem().transform(discount -> {
                    try {
                        DiscountResponse.Builder response = DiscountResponse.newBuilder();
                        response.setId(discount.getId().toHexString());
                        response.setProductId(discount.getProductId());
                        response.setDiscount(discount.getDiscount());
                        return response.build();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Error: " + e.getMessage());
                        return null;
                    }
                });
    }

    @Override
    public Uni<DiscountResponse> setDiscount(SetDiscountRequest request) {
        return discountRepository.persist(new Discount(new ObjectId(), request.getProductId(), request.getDiscount()))
                .onItem().transform(discount -> {
                    try {
                        DiscountResponse.Builder response = DiscountResponse.newBuilder();
                        response.setId(discount.getId().toHexString());
                        response.setProductId(discount.getProductId());
                        response.setDiscount(discount.getDiscount());
                        return response.build();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Error: " + e.getMessage());
                        return null;
                    }
                });
    }

    @Override
    public Uni<ProductIdResponse> deleteDiscount(DeleteDiscountRequest request) {
        discountRepository.delete("productId", request.getProductId());
        ProductIdResponse response = ProductIdResponse.newBuilder()
                .setProductId(request.getProductId())
                .build();

        return Uni.createFrom().item(response);
    }

    @Override
    public Uni<ProductIdResponse> updateDiscount(UpdateDiscountRequest request) {
        discountRepository.update("discount", request.getDiscount()).where("productId", request.getProductId());
        ProductIdResponse response = ProductIdResponse.newBuilder()
                .setProductId(request.getProductId())
                .build();

        return Uni.createFrom().item(response);
    }


}
