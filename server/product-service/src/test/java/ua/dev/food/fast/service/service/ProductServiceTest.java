package ua.dev.food.fast.service.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ua.dev.food.fast.service.domain.dto.request.ProductRequestDto;
import ua.dev.food.fast.service.domain.model.product.Product;
import ua.dev.food.fast.service.exception_handler.exception.ResourceNotFoundException;
import ua.dev.food.fast.service.exception_handler.exception.ValidationException;
import ua.dev.food.fast.service.repository.ProductRepository;
import ua.dev.food.fast.service.util.MessageConstants;

import java.math.BigDecimal;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testGetProductByIdSuccess() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setTitle("Pizza");

        when(productRepository.findById(productId)).thenReturn(Mono.just(product));

        StepVerifier.create(productService.getProductById(productId))
            .expectNextMatches(dto -> dto.title().equals("Pizza"))
            .verifyComplete();

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testGetProductByIdNotFound() {
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Mono.empty());

        StepVerifier.create(productService.getProductById(productId))
            .expectErrorMatches(throwable -> throwable instanceof ResourceNotFoundException &&
                throwable.getMessage().equals(MessageConstants.PRODUCT_NOT_FOUND))
            .verify();

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testAddProductSuccess() {
        ProductRequestDto requestDto = new ProductRequestDto("Pizza", "Food", "Delicious pizza", BigDecimal.valueOf(10.99));
        FilePart file1 = mock(FilePart.class);
        FilePart file2 = mock(FilePart.class);
        FilePart file3 = mock(FilePart.class);

        when(file1.filename()).thenReturn("image1.png");
        when(file2.filename()).thenReturn("image2.png");
        when(file3.filename()).thenReturn("image3.png");

        when(file1.transferTo(any(Path.class))).thenReturn(Mono.empty());
        when(file2.transferTo(any(Path.class))).thenReturn(Mono.empty());
        when(file3.transferTo(any(Path.class))).thenReturn(Mono.empty());

        when(productRepository.save(any(Product.class))).thenReturn(Mono.empty());

        StepVerifier.create(productService.addProduct(Flux.just(file1, file2, file3), requestDto))
            .verifyComplete();

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testAddProductExceedsFileLimit() {
        ProductRequestDto requestDto = new ProductRequestDto("Pizza", "Food", "Delicious pizza", BigDecimal.valueOf(10.99));
        FilePart file1 = mock(FilePart.class);
        FilePart file2 = mock(FilePart.class);
        FilePart file3 = mock(FilePart.class);
        FilePart file4 = mock(FilePart.class);

        when(file1.filename()).thenReturn("image1.png");
        when(file2.filename()).thenReturn("image2.png");
        when(file3.filename()).thenReturn("image3.png");
        when(file4.filename()).thenReturn("image4.png");

        StepVerifier.create(productService.addProduct(Flux.just(file1, file2, file3, file4), requestDto))
            .expectErrorMatches(throwable -> throwable instanceof ValidationException &&
                throwable.getMessage().equals(MessageConstants.YOU_CAN_T_UPLOAD_MORE_THAN_3_IMAGES))
            .verify();

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testAddProductFileTransferFailure() {
        ProductRequestDto requestDto = new ProductRequestDto("Pizza", "Food", "Delicious pizza", BigDecimal.valueOf(10.99));
        FilePart file1 = mock(FilePart.class);

        when(file1.filename()).thenReturn("image1.png");
        when(file1.transferTo(any(Path.class))).thenReturn(Mono.error(new RuntimeException("File transfer failed")));

        StepVerifier.create(productService.addProduct(Flux.just(file1), requestDto))
            .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                throwable.getMessage().equals("File transfer failed"))
            .verify();

        verify(productRepository, never()).save(any(Product.class));
    }

}
