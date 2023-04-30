package dev.food.fast.server.general.service;

import dev.food.fast.server.general.repository.SliderImageRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SliderServiceTest {

    @Mock
    private SliderImageRepository imageRepository;

    @InjectMocks
    private SliderService sliderService;

    @Test
    void addImageToSlider() {
        List<String> urlImgs = List.of("../../assets/images/slider/first_pizza_slide.png",
                "../../assets/images/slider/s_slider.png",
                "../../assets/images/slider/third_img_for_slider.png");
        for (String urlImg : urlImgs) {
            ResponseEntity<?> response = sliderService.addImageToSlider(urlImg);
            assertEquals(200, response.getStatusCodeValue());
            assertEquals("Image added successfully", response.getBody());
        }
    }
}