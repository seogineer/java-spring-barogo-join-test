package com.seogineer.javaspringbarogojointest.repository;

import com.seogineer.javaspringbarogojointest.entity.Delivery;
import com.seogineer.javaspringbarogojointest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByUserAndDeliveryDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate);
    Optional<Delivery> findById(Long deliveryId);
}
