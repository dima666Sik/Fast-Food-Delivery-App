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
@Table(name = "address_order")
public class AddressOrder {
    @Id
    private Long id;
    private String city;
    private String street;
    private String houseNumber;
    private String flatNumber;
    private String floorNumber;

}
