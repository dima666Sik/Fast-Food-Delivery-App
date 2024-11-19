package ua.dev.food.fast.service.repository;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ua.dev.food.fast.service.domain.model.SliderImage;

public interface SliderImageRepository extends ReactiveCrudRepository<SliderImage, Long> {

}
