package ua.dev.food.fast.service.domain.model.mapper;

import ua.dev.feign.clients.ProductResponseDto;
import ua.dev.food.fast.service.domain.dto.response.SliderImageResponseDto;
import ua.dev.food.fast.service.domain.model.SliderImage;
import ua.dev.food.fast.service.domain.model.product.Product;

public final class Mapper {
    private Mapper() {
    }

    public static SliderImageResponseDto sliderImageToSliderImageResponseDto(SliderImage sliderImage) {
        return SliderImageResponseDto.builder().id(sliderImage.getId()).urlImg(sliderImage.getUrlImg()).build();
    }

    public static ProductResponseDto productToProductResponseDto(Product product) {
        return ProductResponseDto.builder().id(product.getId()).title(product.getTitle())
            .description(product.getDescription()).price(product.getPrice()).category(product.getCategory())
            .likes(product.getLikes()).image01(product.getImage01()).image02(product.getImage02())
            .image03(product.getImage03()).build();
    }
}
