package ua.dev.food.fast.service.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("status_order")
public class StatusOrder {
    @Id
    private Long id;

    @Column("order_id")
    private Long orderId;

    @Column("status_id")
    private Long statusId;
}
