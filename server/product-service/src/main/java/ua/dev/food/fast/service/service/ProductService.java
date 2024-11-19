package ua.dev.food.fast.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.feign.clients.ProductResponseDto;
import ua.dev.food.fast.service.domain.dto.request.ProductRequestDto;
import ua.dev.food.fast.service.domain.model.mapper.Mapper;
import ua.dev.food.fast.service.domain.model.product.Product;
import ua.dev.food.fast.service.exception_handler.exception.ResourceNotFoundException;
import ua.dev.food.fast.service.exception_handler.exception.ValidationException;
import ua.dev.food.fast.service.repository.ProductRepository;
import ua.dev.food.fast.service.util.MessageConstants;

import java.io.File;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductService {

    @Value("${back-end.urls.url-all-images}")
    private String partURLtoImages;
    private final ProductRepository productRepository;

    public Mono<ProductResponseDto> getProductById(Long id) {
        return productRepository.findById(id)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException(MessageConstants.PRODUCT_NOT_FOUND)))
            .map(Mapper::productToProductResponseDto);
    }

    public Mono<Void> addProduct(Flux<FilePart> files, ProductRequestDto request) {
        String imageDirectory = System.getProperty("user.dir") + partURLtoImages;
        makeDirectoryIfNotExist(imageDirectory);

        // Ensure no more than 3 files are uploaded reactively
        return files
            .count() // Counts the number of files reactively
            .flatMap(fileCount -> {
                if (fileCount > 3) {
                    return Mono.error(new ValidationException(MessageConstants.YOU_CAN_T_UPLOAD_MORE_THAN_3_IMAGES));
                }
                // Process the files after validation
                return files
                    .flatMap(filePart -> filePart.transferTo(Paths.get(imageDirectory, filePart.filename())))
                    .then(files // Collect filenames after all files are uploaded
                        .take(3) // Limit to the first 3 files
                        .map(FilePart::filename) // Get the filenames
                        .collectList() // Collect filenames into a list
                        .flatMap(filenames -> {
                            var product = Product.builder()
                                .title(request.title())
                                .price(request.price())
                                .likes(0L)
                                .image01(filenames.get(0)) // Set first filename
                                .image02(filenames.get(1)) // Set second filename
                                .image03(filenames.get(2)) // Set third filename
                                .category(request.category())
                                .description(request.description())
                                .build();
                            return productRepository.save(product).then(); // Save product
                        }));
            })
            .onErrorResume(e -> {
                // Log the error
                log.error("Error occurred while processing the product upload: {}", e.getMessage(), e);
                return Mono.error(e); // Propagate the error
            });
    }


    private void makeDirectoryIfNotExist(String imageDirectory) {
        File directory = new File(imageDirectory);
        if (directory.exists()) {
            return;
        }
        if (directory.mkdir()) {
            log.warn("Creating directory");
        }
    }

    public Mono<Void> addDefaultProduct(Product request) {
        var product = Product.builder()
            .title(request.getTitle())
            .price(request.getPrice())
            .likes(request.getLikes())
            .image01(request.getImage01())
            .image02(request.getImage02())
            .image03(request.getImage03())
            .category(request.getCategory())
            .description(request.getDescription())
            .build();
        return productRepository.save(product).then();
    }


}


