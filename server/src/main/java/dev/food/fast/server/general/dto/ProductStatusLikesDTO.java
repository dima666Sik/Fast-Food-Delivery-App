package dev.food.fast.server.general.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatusLikesDTO {
    private Integer id;
    private Boolean status;
    private Integer likes;
}
