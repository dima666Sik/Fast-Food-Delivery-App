package dev.food.fast.server.auth.models;

import jakarta.persistence.*;
import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue
    public Long id;

    @Column(unique = true)
    public String refreshToken;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    @Column
    public boolean revoked;

    @Column
    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
}

