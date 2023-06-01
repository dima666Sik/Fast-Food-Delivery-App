package dev.food.fast.server.general.controllers;

import dev.food.fast.server.general.models.product.Product;
import dev.food.fast.server.general.dto.request.ProductRequest;
import dev.food.fast.server.general.service.ProductReviewService;
import dev.food.fast.server.general.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/foods")
@RequiredArgsConstructor
public class ProductsController {
    private final ProductsService service;
    private final ProductReviewService productReviewService;

    @PostMapping("/add-product")
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest request) {
        return service.addProduct(request);
    }

    private List<Product> getAllDefaultProducts(){
        Product product1 = new Product();
        product1.setTitle("Chicken Burger");
        product1.setPrice(24.0);
        product1.setLikes(120);
        product1.setImage01("product_01.jpg");
        product1.setImage02("product_01.1.jpg");
        product1.setImage03("product_01.3.jpg");
        product1.setCategory("Burger");
        product1.setDescription("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque. ");

        Product product2 = new Product();
        product2.setTitle("Pizza With Mushroom");
        product2.setPrice(110.0);
        product2.setLikes(20);
        product2.setImage01("product_4.2.jpg");
        product2.setImage02("product_4.1.jpg");
        product2.setImage03("product_4.3.png");
        product2.setCategory("Pizza");
        product2.setDescription("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.");

        Product product3 = new Product();
        product3.setTitle("Vegetarian Pizza");
        product3.setPrice(115.0);
        product3.setLikes(130);
        product3.setImage01("product_2.1.jpg");
        product3.setImage02("product_2.2.jpg");
        product3.setImage03("product_2.3.jpg");
        product3.setCategory("Pizza");
        product3.setDescription("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.");

        Product product4 = new Product();
        product4.setTitle("Double Cheese Margherita");
        product4.setPrice(110.0);
        product4.setLikes(90);
        product4.setImage01("product_3.1.jpg");
        product4.setImage02("product_3.2.jpg");
        product4.setImage03("product_3.3.jpg");
        product4.setCategory("Pizza");
        product4.setDescription("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.");


        Product product5 = new Product();
        product5.setTitle("Maxican Green Wave");
        product5.setPrice(110.0);
        product5.setLikes(125);
        product5.setImage01("product_4.1.jpg");
        product5.setImage02("product_4.2.jpg");
        product5.setImage03("product_4.3.jpg");
        product5.setCategory("Pizza");
        product5.setDescription("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.");

        Product product6 = new Product();
        product6.setTitle("Cheese Burger");
        product6.setPrice(24.0);
        product6.setLikes(170);
        product6.setImage01("product_04.jpg");
        product6.setImage02("product_08.jpg");
        product6.setImage03("product_09.jpg");
        product6.setCategory("Burger");
        product6.setDescription("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.");

        Product product7 = new Product();
        product7.setTitle("Royal Cheese Burger");
        product7.setPrice(24.0);
        product7.setLikes(110);
        product7.setImage01("product_01.jpg");
        product7.setImage02("product_01.1.jpg");
        product7.setImage03("product_01.3.jpg");
        product7.setCategory("Burger");
        product7.setDescription("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.");

        Product product8 = new Product();
        product8.setTitle("Seafood Pizza");
        product8.setPrice(115.0);
        product8.setLikes(70);
        product8.setImage01("product_2.2.jpg");
        product8.setImage02("product_2.3.jpg");
        product8.setImage03("product_2.1.jpg");
        product8.setCategory("Pizza");
        product8.setDescription("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.");

        Product product9 = new Product();
        product9.setTitle("Thin Cheese Pizza");
        product9.setPrice(110.0);
        product9.setLikes(220);
        product9.setImage01("product_3.2.jpg");
        product9.setImage02("product_3.1.jpg");
        product9.setImage03("product_3.3.jpg");
        product9.setCategory("Pizza");
        product9.setDescription("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.");

        Product product10 = new Product();
        product10.setTitle("Classic Hamburger");
        product10.setPrice(24.0);
        product10.setLikes(130);
        product10.setImage01("product_08.jpg");
        product10.setImage02("product_08.jpg");
        product10.setImage03("product_08.jpg");
        product10.setCategory("Burger");
        product10.setDescription("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.");

        Product product11 = new Product();
        product11.setTitle("Maki Roll");
        product11.setPrice(35.0);
        product11.setLikes(76);
        product11.setImage01("sushi_1.png");
        product11.setImage02("sushi_1.png");
        product11.setImage03("sushi_1.png");
        product11.setCategory("Sushi");
        product11.setDescription("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.");

        Product product12 = new Product();
        product12.setTitle("Philadelphia Roll");
        product12.setPrice(35.0);
        product12.setLikes(80);
        product12.setImage01("sushi_2.png");
        product12.setImage02("sushi_2.png");
        product12.setImage03("sushi_2.png");
        product12.setCategory("Sushi");
        product12.setDescription("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.");

        Product product13 = new Product();
        product13.setTitle("Sashimi Roll");
        product13.setPrice(35.0);
        product13.setLikes(100);
        product13.setImage01("sushi_3.png");
        product13.setImage02("sushi_3.png");
        product13.setImage03("sushi_3.png");
        product13.setCategory("Sushi");
        product13.setDescription("Lorem ipsum dolor sit amet consectetur adipisicing elit. Soluta ad et est, fugiat repudiandae neque illo delectus commodi magnam explicabo autem voluptates eaque velit vero facere mollitia. Placeat rem, molestiae error obcaecati enim doloribus impedit aliquam, maiores qui minus neque.");

        return List.of(product1,
                product2,
                product3,
                product4,
                product5,
                product6,
                product7,
                product8,
                product9,
                product10,
                product11,
                product12,
                product13);
    }

    @PostMapping("/add-all-default-products")
    public ResponseEntity<?> addAllDefaultProducts() {
        List<Product> products = getAllDefaultProducts();

        for (Product product : products) {
            ResponseEntity<?> response = service.addProduct(product);
        }

        return ResponseEntity.ok("Products added successfully");
    }

    @GetMapping("/get-all-products")
    public ResponseEntity<?> getAllProducts(
    ) {
        return service.getAllProducts();
    }

    @GetMapping("/get-all-reviews-to-product")
    public ResponseEntity<?> getAllProductReview(@RequestParam("product_id") Integer productId
    ) {
        return productReviewService.getAllProductReview(productId);
    }
}
