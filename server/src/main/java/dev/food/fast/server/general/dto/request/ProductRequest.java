package dev.food.fast.server.general.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String title;
    private String category;
    private String description;
    private MultipartFile image01;
    private MultipartFile image02;
    private MultipartFile image03;
    private Double price;
}
