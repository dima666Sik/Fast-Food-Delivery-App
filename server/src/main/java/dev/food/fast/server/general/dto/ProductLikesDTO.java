package dev.food.fast.server.general.dto;

import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductLikesDTO {
    private Integer id;
    private Boolean status;
    private Integer likes;
}
