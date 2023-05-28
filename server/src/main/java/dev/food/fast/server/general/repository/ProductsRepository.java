package dev.food.fast.server.general.repository;

import dev.food.fast.server.general.models.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findById(Integer id);
}
