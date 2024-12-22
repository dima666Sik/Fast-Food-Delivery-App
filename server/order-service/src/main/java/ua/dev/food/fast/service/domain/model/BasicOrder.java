package ua.dev.food.fast.service.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "basic_order")
@ToString
public class BasicOrder {
    @Id
    private Long id;
    private String phone;
    private String orderDateArrived;
    private String orderTimeArrived;
    private BigDecimal totalAmount;
    private Long addressOrderId;
    private Boolean cashPayment;
    private LocalDateTime orderTimestamp;
}
