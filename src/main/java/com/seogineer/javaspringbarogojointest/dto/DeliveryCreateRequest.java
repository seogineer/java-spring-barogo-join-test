package com.seogineer.javaspringbarogojointest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCreateRequest {

    @NotBlank(message = "배달 주소는 필수 입력 값입니다.")
    @Size(min = 5, max = 100, message = "배달 주소는 5자 이상 100자 이하로 입력해야 합니다.")
    private String deliveryAddress;

    @NotNull(message = "배달 날짜는 필수 입력 값입니다.")
    @FutureOrPresent(message = "배달 날짜는 현재 또는 미래의 날짜여야 합니다.")
    private LocalDateTime deliveryDate;
}

