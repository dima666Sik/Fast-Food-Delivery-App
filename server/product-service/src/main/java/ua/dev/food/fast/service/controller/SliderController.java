package ua.dev.food.fast.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.dto.response.SliderImageResponseDto;
import ua.dev.food.fast.service.service.SliderService;

import java.util.List;

@RestController
@RequestMapping("/api/v2/product/slider")
@RequiredArgsConstructor
public class SliderController {

    private final SliderService service;

    @GetMapping("/images")
    public Mono<ResponseEntity<Flux<SliderImageResponseDto>>> getImagesToSlider() {
        return Mono.just(ResponseEntity.ok().body(service.getImagesForSlider()));
    }

    @PostMapping("/add-slider-images")
    public Mono<ResponseEntity<Void>> addImagesToSlider() {
        return Flux.fromIterable(List.of("1_slide.png", "2_slide.png", "3_slide.png"))
            .flatMap(service::addImageToSlider)
            .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
    }
}
