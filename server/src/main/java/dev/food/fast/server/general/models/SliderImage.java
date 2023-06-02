package dev.food.fast.server.general.models;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "slider_images")
public class SliderImage {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "url_img")
    private String urlImg;
}
