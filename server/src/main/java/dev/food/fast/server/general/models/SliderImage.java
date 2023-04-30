package dev.food.fast.server.general.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "slider_images")
public class SliderImage {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "url_img")
    private String urlImg;
}
