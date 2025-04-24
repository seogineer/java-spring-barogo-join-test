package com.seogineer.javaspringbarogojointest.entity;

import com.seogineer.javaspringbarogojointest.enums.DeliveryStatus;
import com.seogineer.javaspringbarogojointest.fixture.UserFixtures;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryTest {

    @Test
    void 생성() {
        User 사용자1 = UserFixtures.사용자1;
        LocalDateTime expectedDate = LocalDateTime.of(2025, 3, 15, 10, 30, 0);

        Delivery delivery = new Delivery();
        delivery.setId(1L);
        delivery.setDeliveryAddress("주소1");
        delivery.setDeliveryStatus(DeliveryStatus.ORDERED);
        delivery.setDeliveryDate(expectedDate);
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
