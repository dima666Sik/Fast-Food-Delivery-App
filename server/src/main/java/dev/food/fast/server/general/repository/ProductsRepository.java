package dev.food.fast.server.general.repository;

import dev.food.fast.server.general.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Product, Integer> {
}
