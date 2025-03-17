package com.seogineer.javaspringbarogojointest.dto;

import com.seogineer.javaspringbarogojointest.entity.Delivery;
import com.seogineer.javaspringbarogojointest.enums.DeliveryStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DeliveryResponse {
    private String username;
    private String deliveryAddress;
    private LocalDateTime deliveryDate;
    private DeliveryStatus deliveryStatus;

    public DeliveryResponse(Delivery delivery) {
        this.username = delivery.getUsername();
        this.deliveryAddress = delivery.getDeliveryAddress();
        this.deliveryDate = delivery.getDeliveryDate();
        this.deliveryStatus = delivery.getDeliveryStatus();
    }
}
