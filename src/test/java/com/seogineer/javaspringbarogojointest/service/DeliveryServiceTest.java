package com.seogineer.javaspringbarogojointest.service;

import com.seogineer.javaspringbarogojointest.dto.DeliveryCreateRequest;
import com.seogineer.javaspringbarogojointest.dto.DeliveryResponse;
import com.seogineer.javaspringbarogojointest.entity.Delivery;
import com.seogineer.javaspringbarogojointest.enums.DeliveryStatus;
import com.seogineer.javaspringbarogojointest.repository.DeliveryRepository;
import com.seogineer.javaspringbarogojointest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.seogineer.javaspringbarogojointest.enums.ErrorMessage.*;
import static com.seogineer.javaspringbarogojointest.fixture.UserFixtures.사용자1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeliveryService deliveryService;

    private User userDetails;

    @BeforeEach
    void setUp() {
        userDetails = (User) User.withUsername("user1")
                .password("1234")
                .roles("USER")
                .build();
    }

    @Test
    void 존재하지_않는_유저는_배달을_생성할_수_없다() {
        DeliveryCreateRequest request = new DeliveryCreateRequest("서울시 강남구", LocalDateTime.now());

        assertThatThrownBy(() -> deliveryService.createDelivery(request, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());
    }

    @Test
    void 배달이_정상적으로_생성된다() {
        DeliveryCreateRequest request = new DeliveryCreateRequest("서울시 강남구", LocalDateTime.now());
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(사용자1));
        when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DeliveryResponse response = deliveryService.createDelivery(request, userDetails);

        assertThat(response).isNotNull();
        assertThat(response.getDeliveryAddress()).isEqualTo("서울시 강남구");
        assertThat(response.getUsername()).isEqualTo("user1");
        assertThat(response.getDeliveryStatus()).isEqualTo(DeliveryStatus.ORDERED);
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    @Test
    void 기간이_3일_초과할_경우_예외_발생() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(4);

        assertThatThrownBy(() -> deliveryService.getDeliveriesWithinDateRange(startDate, endDate, userDetails))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MAX_PERIOD_EXCEEDED.getMessage());
    }

    @Test
    void 기간_내의_배달_목록이_정상적으로_조회된다() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = startDate.plusDays(2).atTime(LocalTime.MAX);

        Delivery delivery1 = new Delivery();
        delivery1.setUser(사용자1);
        delivery1.setDeliveryAddress("주소1");
        delivery1.setDeliveryDate(startDateTime);
        delivery1.setDeliveryStatus(DeliveryStatus.ORDERED);

        Delivery delivery2 = new Delivery();
        delivery2.setUser(사용자1);
        delivery2.setDeliveryAddress("주소2");
        delivery2.setDeliveryDate(endDateTime);
        delivery2.setDeliveryStatus(DeliveryStatus.ORDERED);

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(사용자1));
        when(deliveryRepository.findByUserAndDeliveryDateBetween(사용자1, startDateTime, endDateTime))
                .thenReturn(List.of(delivery1, delivery2));

        List<DeliveryResponse> responses = deliveryService.getDeliveriesWithinDateRange(startDate, endDate, userDetails);

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getDeliveryAddress()).isEqualTo("주소1");
        assertThat(responses.get(1).getDeliveryAddress()).isEqualTo("주소2");
    }

    @Test
    void 배달이_존재하지_않으면_주소를_수정할_수_없다() {
        when(deliveryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deliveryService.updateDeliveryAddress(1L, "새 주소", userDetails))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(DELIVERY_NOT_FOUND.getMessage());
    }

    @Test
    void 유저가_배달_소유자가_아닐_경우_수정할_수_없다() {
        // given
        Delivery delivery = new Delivery();
        delivery.setUsername("otherUser");
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));

        // when & then
        assertThatThrownBy(() -> deliveryService.updateDeliveryAddress(1L, "새 주소", userDetails))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(USER_INFO_MISMATCH.getMessage());
    }

    @Test
    void 배달이_완료되거나_취소된_경우_수정할_수_없다() {
        Delivery delivery = new Delivery();
        delivery.setUsername("user1");
        delivery.setDeliveryStatus(DeliveryStatus.DELIVERED);
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));

        assertThatThrownBy(() -> deliveryService.updateDeliveryAddress(1L, "새 주소", userDetails))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CANNOT_MODIFY_COMPLETED_ORDER.getMessage());
    }

    @Test
    void 주소가_정상적으로_수정된다() {
        Delivery delivery = new Delivery();
        delivery.setUsername("user1");
        delivery.setDeliveryStatus(DeliveryStatus.ORDERED);
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DeliveryResponse response = deliveryService.updateDeliveryAddress(1L, "새 주소", userDetails);

        assertThat(response.getDeliveryAddress()).isEqualTo("새 주소");
    }
}
