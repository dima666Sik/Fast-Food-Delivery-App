package ua.dev.food.fast.service.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest implements Serializable {
    private String username;
    private String from;
    private String message;
}
