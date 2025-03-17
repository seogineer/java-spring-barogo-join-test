package com.seogineer.javaspringbarogojointest.entity;

import com.seogineer.javaspringbarogojointest.enums.DeliveryStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.seogineer.javaspringbarogojointest.fixture.UserFixtures.사용자1;
import static org.assertj.core.api.Assertions.assertThat;

class DeliveryTest {

    @Test
    void 생성() {
        Delivery delivery = new Delivery();
        delivery.setId(1L);
        delivery.setDeliveryAddress("주소1");
        delivery.setDeliveryStatus(DeliveryStatus.ORDERED);
        delivery.setDeliveryDate(LocalDateTime.of(2025, 3, 15, 10, 30, 0));
        delivery.setUser(사용자1);
        delivery.setUsername(사용자1.getUsername());

        assertThat(delivery.getId()).isEqualTo(1L);
        assertThat(delivery.getDeliveryAddress()).isEqualTo("주소1");
        assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.ORDERED);
        assertThat(delivery.getDeliveryDate()).isEqualTo(LocalDateTime.of(2025, 3, 15, 10, 30, 0));
        assertThat(delivery.getUser()).isEqualTo(사용자1);
        assertThat(delivery.getUsername()).isEqualTo(사용자1.getUsername());
    }
}
