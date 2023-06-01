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
@Table(name = "basic_order_user")
public class BasicOrderUser {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "basic_order_id")
    private BasicOrder basicOrder;
}
