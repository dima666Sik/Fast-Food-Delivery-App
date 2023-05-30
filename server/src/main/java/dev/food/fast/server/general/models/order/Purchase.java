package dev.food.fast.server.general.models.order;

import dev.food.fast.server.auth.models.User;
import dev.food.fast.server.general.models.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "purchase")
public class Purchase {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    public Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basic_order_user_id")
    public BasicOrderUser basicOrderUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basic_order_guest_id")
    public BasicOrderGuest basicOrderGuest;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "quantity")
    private Integer quantity;
}
