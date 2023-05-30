package dev.food.fast.server.general.models.order;

import dev.food.fast.server.auth.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Time;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "basic_order")
public class BasicOrder {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "order_date")
    private String orderDate;

    @Column(name = "order_time")
    private String orderTime;

    @Column(name = "total_amount")
    private Double totalAmount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_order_id", referencedColumnName = "id")
    private AddressOrder addressOrder;

    @OneToOne(mappedBy = "basicOrder", cascade = CascadeType.ALL)
    private BasicOrderGuest basicOrderGuest;

    @OneToOne(mappedBy = "basicOrder", cascade = CascadeType.ALL)
    private BasicOrderUser basicOrderUser;

}
