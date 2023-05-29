package dev.food.fast.server.general.models.order;

import dev.food.fast.server.auth.models.User;
import jakarta.persistence.*;
import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "basic_order_guest")
public class BasicOrderGuest {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "contact_email")
    private String contactEmail;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "basic_order_id")
    private BasicOrder basicOrder;

}
