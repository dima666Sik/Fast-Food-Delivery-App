package ua.dev.food.fast.service.util;

import ua.dev.food.fast.service.domain.model.product.Product;

import java.math.BigDecimal;
import java.util.List;

public class DefaultProductsData {
    private DefaultProductsData() {
    }

    public static List<Product> getAllDefaultProducts() {
        Product product1 = Product.builder()
            .title("Chicken Burger")
            .price(new BigDecimal("24.0"))
            .likes(120L)
            .image01("product_01.jpg")
            .image02("product_01_1.jpg")
            .image03("product_01_3.jpg")
            .category("Burger")
            .description("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.")
            .build();

        Product product2 = Product.builder()
            .title("Pizza With Mushroom")
            .price(new BigDecimal("110.0"))
            .likes(20L)
            .image01("product_4_2.jpg")
            .image02("product_4_1.jpg")
            .image03("product_4_3.png")
            .category("Pizza")
            .description("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.")
            .build();

        Product product3 = Product.builder()
            .title("Vegetarian Pizza")
            .price(new BigDecimal("115.0"))
            .likes(130L)
            .image01("product_2_1.jpg")
            .image02("product_2_2.jpg")
            .image03("product_2_3.jpg")
            .category("Pizza")
            .description("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.")
            .build();

        Product product4 = Product.builder()
            .title("Double Cheese Margherita")
            .price(new BigDecimal("110.0"))
            .likes(90L)
            .image01("product_3_1.jpg")
            .image02("product_3_2.jpg")
            .image03("product_3_3.jpg")
            .category("Pizza")
            .description("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.")
            .build();

        Product product5 = Product.builder()
            .title("Mexican Green Wave")
            .price(new BigDecimal("110.0"))
            .likes(125L)
            .image01("product_4_1.jpg")
            .image02("product_4_2.jpg")
            .image03("product_4_3.jpg")
            .category("Pizza")
            .description("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.")
            .build();

        // Continue for the remaining products in the same pattern...

        return List.of(
            product1, product2, product3, product4, product5,
            Product.builder()
                .title("Cheese Burger")
                .price(new BigDecimal("24.0"))
                .likes(170L)
                .image01("product_04.jpg")
                .image02("product_08.jpg")
                .image03("product_09.jpg")
                .category("Burger")
                .description("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.")
                .build(),
            Product.builder()
                .title("Royal Cheese Burger")
                .price(new BigDecimal("24.0"))
                .likes(110L)
                .image01("product_01.jpg")
                .image02("product_01_1.jpg")
                .image03("product_01_3.jpg")
                .category("Burger")
                .description("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.")
                .build()
        );
    }

}
