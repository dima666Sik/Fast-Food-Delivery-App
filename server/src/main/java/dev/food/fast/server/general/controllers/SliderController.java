package dev.food.fast.server.general.controllers;

import dev.food.fast.server.general.service.SliderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/slider")
@RequiredArgsConstructor
public class SliderController {

    private final SliderService service;

    @GetMapping("/images")
    public ResponseEntity<?> getImagesToSlider(
    ) {
        return service.getImagesForSlider();
    }

    @PostMapping("/add-slider-images")
    public ResponseEntity<?> addImagesToSlider() {
        List<String> urlImgs = List.of("first_pizza_slide.png",
                "second_img_for_slider.png",
                "third_img_for_slider.png");

        for (String urlImg : urlImgs) {
            ResponseEntity<?> response = service.addImageToSlider(urlImg);
        }
        return ResponseEntity.ok("Images added successfully");
    }
}
