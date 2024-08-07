package dev.food.fast.server.general.models.order;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "address_order")
public class AddressOrder {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "house_number")
    private String houseNumber;

    @Column(name = "flat_number")
    private String flatNumber;

    @Column(name = "floor_number")
    private String floorNumber;

//    @OneToOne(mappedBy = "addressOrder")
//    private BasicOrder basicOrder;

}
