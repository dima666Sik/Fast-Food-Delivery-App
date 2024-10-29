package ua.dev.food.fast.service.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("user_permissions")
public class UserPermission {
    @Column("user_id")
    private Long userId;

    @Column("permission_id")
    private Long permissionId;
}
