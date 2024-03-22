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
    private Long id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "order_date_arrived")
    private String orderDateArrived;

    @Column(name = "order_time_arrived")
    private String orderTimeArrived;

    @Column(name = "total_amount")
    private Double totalAmount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_order_id", referencedColumnName = "id")
    private AddressOrder addressOrder;

    @Column(name = "cash_payment")
    private Boolean cashPayment;
//    @OneToOne(mappedBy = "basicOrder", cascade = CascadeType.ALL)
//    private BasicOrderGuest basicOrderGuest;
//
//    @OneToOne(mappedBy = "basicOrder", cascade = CascadeType.ALL)
//    private BasicOrderUser basicOrderUser;

}
