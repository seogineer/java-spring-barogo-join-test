package com.seogineer.javaspringbarogojointest.dto;

import com.seogineer.javaspringbarogojointest.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class UserResponse {
    private String username;
    private String name;
    private List<DeliveryResponse> deliveries = new ArrayList<>();

    public UserResponse(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
        user.getDeliveries().forEach(delivery -> deliveries.add(new DeliveryResponse(delivery)));
    }
}
