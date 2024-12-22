package ua.dev.food.fast.service.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "basic_order_guest")
public class BasicOrderGuest {
    @Id
    private Long id;
    private String name;
    private String contactEmail;
    private Long basicOrderId;

}
