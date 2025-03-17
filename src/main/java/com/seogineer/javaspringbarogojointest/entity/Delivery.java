package com.seogineer.javaspringbarogojointest.entity;

import com.seogineer.javaspringbarogojointest.enums.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String username;
    private String deliveryAddress;
    private LocalDateTime deliveryDate; // 배달 날짜

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus; // 배달 상태 (예: 주문, 배송 중, 완료)
}

