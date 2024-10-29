package ua.dev.food.fast.service.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("access_tokens")  // R2DBC table mapping
@ToString
public class AccessToken {

    @Id
    private Long id;

    @Column("token")
    private String token;

    @Column("token_type")
    private TokenType tokenType = TokenType.BEARER;

    @Column("is_revoked")
    private boolean isRevoked;

    @Column("is_expired")
    private boolean isExpired;

    @Column("user_id")  // References User entity by ID
    private Long userId;
}
