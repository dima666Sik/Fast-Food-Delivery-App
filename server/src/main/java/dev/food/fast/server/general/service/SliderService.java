package dev.food.fast.server.general.service;

import dev.food.fast.server.general.models.SliderImage;
import dev.food.fast.server.general.repository.SliderImageRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SliderService {

    private final SliderImageRepository imageRepository;

    public ResponseEntity<?> getImagesForSlider() {
        List<SliderImage> images = imageRepository.findAll();
        return ResponseEntity.ok(images);
    }

    public ResponseEntity<?> addImageToSlider(String urlImg) {
        SliderImage image = SliderImage.builder().urlImg(urlImg).build();
        imageRepository.save(image);
        return ResponseEntity.ok("Image added successfully");
    }

}




