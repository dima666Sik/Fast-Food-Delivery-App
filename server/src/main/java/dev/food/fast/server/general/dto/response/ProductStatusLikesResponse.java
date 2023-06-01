package dev.food.fast.server.general.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatusLikesResponse {
    private Integer id;
    private Boolean status;
    private Integer likes;
}
