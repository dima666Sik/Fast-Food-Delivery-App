package ua.dev.food.fast.service.domain.model;

import org.springframework.data.relational.core.mapping.Column;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("processing_order")
public class ProcessingOrder {
    @Column("order_id")
    private Long orderId;

    @Column("courier_id")
    private Long courierId;
}
