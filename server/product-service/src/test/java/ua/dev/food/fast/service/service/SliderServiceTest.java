package ua.dev.food.fast.service.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ua.dev.food.fast.service.domain.model.SliderImage;
import ua.dev.food.fast.service.exception_handler.exception.ResourceNotFoundException;
import ua.dev.food.fast.service.repository.SliderImageRepository;
import ua.dev.food.fast.service.util.MessageConstants;

import static org.mockito.Mockito.*;

@SpringBootTest
class SliderServiceTest {
    @Mock
    private SliderImageRepository imageRepository;

    @InjectMocks
    private SliderService sliderService;

    @Test
    void getImagesForSliderWhenImagesExistReturnsFluxOfImages() {
        // Mocked data
        SliderImage image1 = SliderImage.builder().id(1L).urlImg("url1").build();
        SliderImage image2 = SliderImage.builder().id(2L).urlImg("url2").build();

        // Mock behavior
        when(imageRepository.findAll()).thenReturn(Flux.just(image1, image2));

        // Test
        StepVerifier.create(sliderService.getImagesForSlider())
            .expectNextMatches(dto -> dto.urlImg().equals("url1"))
            .expectNextMatches(dto -> dto.urlImg().equals("url2"))
            .verifyComplete();

        // Verify interaction
        verify(imageRepository, times(1)).findAll();
    }

    @Test
    void getImagesForSliderWhenNoImagesExistThrowsResourceNotFoundException() {
        // Mock behavior
        when(imageRepository.findAll()).thenReturn(Flux.empty());

        // Test
        StepVerifier.create(sliderService.getImagesForSlider())
            .expectErrorMatches(throwable ->
                throwable instanceof ResourceNotFoundException &&
                    throwable.getMessage().equals(MessageConstants.IMAGES_FOR_SLIDER_WAS_NOT_FOUND)
            )
            .verify();

        // Verify interaction
        verify(imageRepository, times(1)).findAll();
    }

    @Test
    void addImageToSliderSavesImageSuccessfully() {
        // Mocked data
        String urlImg = "test-url";
        SliderImage image = SliderImage.builder().urlImg(urlImg).build();

        // Mock behavior
        when(imageRepository.save(any(SliderImage.class))).thenReturn(Mono.just(image));

        // Test
        StepVerifier.create(sliderService.addImageToSlider(urlImg))
            .verifyComplete();

        // Verify interaction
        verify(imageRepository, times(1)).save(argThat(savedImage -> savedImage.getUrlImg().equals(urlImg)));
    }
}