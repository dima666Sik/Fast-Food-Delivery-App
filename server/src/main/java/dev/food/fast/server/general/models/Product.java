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
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private Double price;

    @Column(name = "likes")
    private Integer likes;

    @Column(name = "image01")
    private String image01;

    @Column(name = "image02")
    private String image02;

    @Column(name = "image03")
    private String image03;

    @Column(name = "category")
    private String category;

    @Lob
    @Column(name="description", length=100000)
    private String description;

}
