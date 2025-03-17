package com.seogineer.javaspringbarogojointest.service;

import com.seogineer.javaspringbarogojointest.dto.DeliveryCreateRequest;
import com.seogineer.javaspringbarogojointest.dto.DeliveryResponse;
import com.seogineer.javaspringbarogojointest.entity.Delivery;
import com.seogineer.javaspringbarogojointest.enums.DeliveryStatus;
import com.seogineer.javaspringbarogojointest.repository.DeliveryRepository;
import com.seogineer.javaspringbarogojointest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.seogineer.javaspringbarogojointest.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;

    @Transactional
    public DeliveryResponse createDelivery(DeliveryCreateRequest request, User user) {
        if (user == null) {
            throw new IllegalArgumentException(USER_NOT_FOUND.getMessage());
        }

        Delivery delivery = new Delivery();
        delivery.setUser(userRepository.findByUsername(user.getUsername()).get());
        delivery.setUsername(user.getUsername());
        delivery.setDeliveryAddress(request.getDeliveryAddress());
        delivery.setDeliveryDate(request.getDeliveryDate());
        delivery.setDeliveryStatus(DeliveryStatus.ORDERED);
        deliveryRepository.save(delivery);

        return new DeliveryResponse(delivery);
    }

    @Transactional(readOnly = true)
    public List<DeliveryResponse> getDeliveriesWithinDateRange(LocalDate startDate, LocalDate endDate, User user) {
        if (ChronoUnit.DAYS.between(startDate, endDate) > 3) {
            throw new IllegalArgumentException(MAX_PERIOD_EXCEEDED.getMessage());
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        com.seogineer.javaspringbarogojointest.entity.User entityUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND.getMessage()));

        List<Delivery> deliveries = deliveryRepository.findByUserAndDeliveryDateBetween(entityUser, startDateTime, endDateTime);

        return deliveries.stream().map(DeliveryResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public DeliveryResponse updateDeliveryAddress(Long deliveryId, String newAddress, User user) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException(DELIVERY_NOT_FOUND.getMessage()));

        if (!delivery.getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException(USER_INFO_MISMATCH.getMessage());
        }

        if (delivery.getDeliveryStatus() == DeliveryStatus.DELIVERED || delivery.getDeliveryStatus() == DeliveryStatus.CANCELLED) {
            throw new IllegalArgumentException(CANNOT_MODIFY_COMPLETED_ORDER.getMessage());
        }

        delivery.setDeliveryAddress(newAddress);

        return new DeliveryResponse(deliveryRepository.save(delivery));
    }
}
