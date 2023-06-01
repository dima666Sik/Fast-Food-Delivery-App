package dev.food.fast.server.general.models.product;

import dev.food.fast.server.auth.models.User;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "product_status_likes")
public class ProductStatusLikes {

    @Id
    @GeneratedValue
    public Integer id;

    @Column
    public Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    public Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
}
