package com.example;

import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;

import java.util.logging.Level;
import java.util.logging.Logger;

@GrpcService
public class DiscountGrpcService implements DiscountService{

    private static final Logger logger = Logger.getLogger(DiscountGrpcService.class.getName());

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
                        if(discount == null) {
                            logger.log(Level.INFO, "No discount found for productId: " + request.getProductId());
                            return null;
                        }
                        DiscountResponse.Builder response = DiscountResponse.newBuilder();
                        response.setId(discount.getId().toHexString());
                        response.setProductId(discount.getProductId());
                        response.setDiscount(discount.getDiscount());
                        logger.log(Level.INFO, "Discount get for productId: " + request.getProductId());
                        return response.build();
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.log(Level.SEVERE, "Error processing getDiscount request", e);
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
                        logger.log(Level.INFO, "Discount set for productId: " + request.getProductId());
                        return response.build();
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.log(Level.SEVERE, "Error processing setDiscount request", e);
                        return null;
                    }
                });
    }

    @Override
    public Uni<ProductIdResponse> deleteDiscount(DeleteDiscountRequest request) {
        return discountRepository.delete("productId", request.getProductId())
                .onItem().transform(deletedCount -> {
                    if (deletedCount > 0) {
                        ProductIdResponse response = ProductIdResponse.newBuilder().setProductId(request.getProductId()).build();
                        logger.log(Level.INFO, "Discount deleted for productId: " + request.getProductId());
                        return response;
                    } else {
                        logger.log(Level.WARNING, "No discount found for productId: " + request.getProductId());
                        return null;
                    }
                });
    }


    @Override
    public Uni<ProductIdResponse> updateDiscount(UpdateDiscountRequest request) {
        return discountRepository.update("discount", request.getDiscount())
                .where("productId", request.getProductId())
                .onItem().transformToUni(updatedCount -> {
                    if (updatedCount > 0) {
                        ProductIdResponse response = ProductIdResponse.newBuilder().setProductId(request.getProductId()).build();
                        logger.log(Level.INFO, "Discount updated for productId: " + request.getProductId());
                        return Uni.createFrom().item(response);
                    } else {
                        logger.log(Level.WARNING, "No discount found for productId: " + request.getProductId());
                        return Uni.createFrom().nullItem();
                    }
                });
    }



}
