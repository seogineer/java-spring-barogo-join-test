package com.seogineer.javaspringbarogojointest.controller;

import com.seogineer.javaspringbarogojointest.dto.DeliveryCreateRequest;
import com.seogineer.javaspringbarogojointest.dto.DeliveryResponse;
import com.seogineer.javaspringbarogojointest.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/create")
    public ResponseEntity<DeliveryResponse> createDelivery(
            @RequestBody @Valid DeliveryCreateRequest createRequest,
            @AuthenticationPrincipal User user) {

        DeliveryResponse newDelivery = deliveryService.createDelivery(createRequest, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDelivery);
    }

    @GetMapping("/search")
    public ResponseEntity<List<DeliveryResponse>> getDeliveriesWithinDateRange(
            @RequestParam @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @AuthenticationPrincipal User user) {

        List<DeliveryResponse> deliveries = deliveryService.getDeliveriesWithinDateRange(startDate, endDate, user.getUsername());
        return ResponseEntity.ok(deliveries);
    }

    @PutMapping("/{deliveryId}/address")
    public ResponseEntity<DeliveryResponse> updateDeliveryAddress(
            @PathVariable Long deliveryId,
            @RequestParam String newAddress,
            @AuthenticationPrincipal User user) {

        DeliveryResponse updatedDelivery = deliveryService.updateDeliveryAddress(deliveryId, newAddress, user);
        return ResponseEntity.ok(updatedDelivery);
    }
}

