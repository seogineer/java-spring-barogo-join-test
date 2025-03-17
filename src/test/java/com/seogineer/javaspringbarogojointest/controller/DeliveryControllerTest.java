package com.seogineer.javaspringbarogojointest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seogineer.javaspringbarogojointest.dto.DeliveryCreateRequest;
import com.seogineer.javaspringbarogojointest.dto.DeliveryResponse;
import com.seogineer.javaspringbarogojointest.entity.Delivery;
import com.seogineer.javaspringbarogojointest.enums.DeliveryStatus;
import com.seogineer.javaspringbarogojointest.service.DeliveryService;
import com.seogineer.javaspringbarogojointest.utils.CustomUserDetailsService;
import com.seogineer.javaspringbarogojointest.utils.JwtAuthenticationFilter;
import com.seogineer.javaspringbarogojointest.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.seogineer.javaspringbarogojointest.fixture.UserFixtures.사용자1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(value = DeliveryController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class))
class DeliveryControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private DeliveryService deliveryService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .alwaysDo(print())
                .build();
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    void 주문_생성() throws Exception {
        DeliveryCreateRequest request = new DeliveryCreateRequest("서울시 강남구 역삼동", LocalDateTime.of(2099, 3, 17, 10, 0, 0));

        Delivery delivery = new Delivery();
        delivery.setId(1L);
        delivery.setDeliveryAddress("서울시 강남구 역삼동");
        delivery.setDeliveryStatus(DeliveryStatus.ORDERED);
        delivery.setDeliveryDate(LocalDateTime.of(2099, 3, 17, 10, 0, 0));
        delivery.setUser(사용자1);
        delivery.setUsername("user1");
        DeliveryResponse response = new DeliveryResponse(delivery);

        when(deliveryService.createDelivery(any(DeliveryCreateRequest.class), any())).thenReturn(response);

        mockMvc.perform(post("/api/deliveries/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.deliveryAddress").value("서울시 강남구 역삼동"))
                .andExpect(jsonPath("$.deliveryStatus").value(DeliveryStatus.ORDERED.toString()))
                .andDo(document("delivery-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("deliveryAddress").description("배송 주소"),
                                fieldWithPath("deliveryDate").description("배송 날짜 및 시간")
                        ),
                        responseFields(
                                fieldWithPath("username").description("사용자 이름"),
                                fieldWithPath("deliveryAddress").description("배송 주소"),
                                fieldWithPath("deliveryDate").description("배송 날짜 및 시간"),
                                fieldWithPath("deliveryStatus").description("배송 상태")
                        )
                ));

        verify(deliveryService, times(1)).createDelivery(any(DeliveryCreateRequest.class), any());
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    void 주문_조회() throws Exception {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 31);

        Delivery delivery1 = new Delivery();
        delivery1.setId(1L);
        delivery1.setDeliveryAddress("서울시 강남구 역삼동");
        delivery1.setDeliveryStatus(DeliveryStatus.DELIVERED);
        delivery1.setDeliveryDate(LocalDateTime.of(2025, 1, 15, 10, 0, 0));
        delivery1.setUser(사용자1);
        delivery1.setUsername("user1");

        Delivery delivery2 = new Delivery();
        delivery2.setId(2L);
        delivery2.setDeliveryAddress("서울시 마포구 상암동");
        delivery2.setDeliveryStatus(DeliveryStatus.ORDERED);
        delivery2.setDeliveryDate(LocalDateTime.of(2025, 1, 20, 14, 0, 0));
        delivery2.setUser(사용자1);
        delivery2.setUsername("user1");

        List<DeliveryResponse> responses = Arrays.asList(
                new DeliveryResponse(delivery1),
                new DeliveryResponse(delivery2)
        );

        when(deliveryService.getDeliveriesWithinDateRange(eq(startDate), eq(endDate), any())).thenReturn(responses);

        mockMvc.perform(get("/api/deliveries/search")
                .param("startDate", "2025-01-01")
                .param("endDate", "2025-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].deliveryAddress").value("서울시 강남구 역삼동"))
                .andExpect(jsonPath("$[1].deliveryAddress").value("서울시 마포구 상암동"))
                .andDo(document("deliveries-search",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("startDate").description("검색 시작 날짜 (yyyy-MM-dd)"),
                                parameterWithName("endDate").description("검색 종료 날짜 (yyyy-MM-dd)")
                        ),
                        responseFields(
                                fieldWithPath("[].username").description("사용자 이름"),
                                fieldWithPath("[].deliveryAddress").description("배송 주소"),
                                fieldWithPath("[].deliveryDate").description("배송 날짜 및 시간"),
                                fieldWithPath("[].deliveryStatus").description("배송 상태")
                        )
                ));

        verify(deliveryService, times(1)).getDeliveriesWithinDateRange(eq(startDate), eq(endDate), any());
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    void 주문_주소_변경() throws Exception {
        Long deliveryId = 1L;
        String newAddress = "서울시 송파구 잠실동";

        Delivery delivery = new Delivery();
        delivery.setId(deliveryId);
        delivery.setDeliveryAddress(newAddress);
        delivery.setDeliveryStatus(DeliveryStatus.ORDERED);
        delivery.setDeliveryDate(LocalDateTime.of(2025, 1, 15, 10, 0, 0));
        delivery.setUser(사용자1);
        delivery.setUsername("user1");
        DeliveryResponse response = new DeliveryResponse(delivery);

        when(deliveryService.updateDeliveryAddress(eq(deliveryId), eq(newAddress), any())).thenReturn(response);

        mockMvc.perform(put("/api/deliveries/{deliveryId}/address", deliveryId)
                .with(csrf())
                .param("newAddress", newAddress))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryAddress").value(newAddress))
                .andDo(document("delivery-update-address",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("deliveryId").description("수정할 배송 ID")
                        ),
                        requestParameters(
                                parameterWithName("newAddress").description("새 배송 주소"),
                                parameterWithName("_csrf").description("CSRF 토큰").optional()
                        ),
                        responseFields(
                                fieldWithPath("username").description("사용자 이름"),
                                fieldWithPath("deliveryAddress").description("수정된 배송 주소"),
                                fieldWithPath("deliveryDate").description("배송 날짜 및 시간"),
                                fieldWithPath("deliveryStatus").description("배송 상태")
                        )
                ));

        verify(deliveryService, times(1)).updateDeliveryAddress(eq(deliveryId), eq(newAddress), any());
    }
}
