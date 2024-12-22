package ua.dev.food.fast.service.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "basic_order_user")
public class BasicOrderUser {
    @Id
    private Long id;
    private Long userId;
    private Long basicOrderId;
}
