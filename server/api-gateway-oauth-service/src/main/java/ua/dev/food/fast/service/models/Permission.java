package ua.dev.food.fast.service.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("permissions")
public class Permission {
    @Id
    private Long id;
    private Role role;
}