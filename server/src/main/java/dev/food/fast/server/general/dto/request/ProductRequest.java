package dev.food.fast.server.general.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String title;
    private Double price;
    private Integer likes;
    private String image01;
    private String image02;
    private String image03;
    private String category;
    private String description;
}
