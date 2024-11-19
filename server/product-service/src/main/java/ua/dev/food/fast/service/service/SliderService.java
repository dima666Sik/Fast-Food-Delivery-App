package ua.dev.food.fast.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.dto.response.SliderImageResponseDto;
import ua.dev.food.fast.service.domain.model.SliderImage;
import ua.dev.food.fast.service.domain.model.mapper.Mapper;
import ua.dev.food.fast.service.exception_handler.exception.ResourceNotFoundException;
import ua.dev.food.fast.service.repository.SliderImageRepository;
import ua.dev.food.fast.service.util.MessageConstants;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SliderService {

    private final SliderImageRepository imageRepository;

    public Flux<SliderImageResponseDto> getImagesForSlider() {
        return imageRepository.findAll()
            .switchIfEmpty(Flux.error(new ResourceNotFoundException(MessageConstants.IMAGES_FOR_SLIDER_WAS_NOT_FOUND)))
            .map(Mapper::sliderImageToSliderImageResponseDto);
    }

    @Transactional
    public Mono<Void> addImageToSlider(String urlImg) {
        SliderImage image = SliderImage.builder().urlImg(urlImg).build();
        return imageRepository.save(image).then();
    }

}




