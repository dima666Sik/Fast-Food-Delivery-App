package ua.dev.food.fast.service.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table("statuses")
public class Status {
    @Id
    private Long id;
    private State state;
}
