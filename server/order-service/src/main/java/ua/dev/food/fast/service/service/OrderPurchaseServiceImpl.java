package ua.dev.food.fast.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.feign.clients.OrderInfoRequestDto;
import ua.dev.feign.clients.ProductFeignClient;
import ua.dev.feign.clients.ProductPurchaseDto;
import ua.dev.food.fast.service.domain.dto.request.OrderPurchaseRequest;
import ua.dev.food.fast.service.domain.dto.request.PurchaseItemRequest;
import ua.dev.food.fast.service.domain.dto.response.OrderPurchaseResponse;
import ua.dev.food.fast.service.domain.dto.response.PurchaseItemResponse;
import ua.dev.food.fast.service.domain.model.*;
import ua.dev.food.fast.service.exception_handler.exception.OrderProcessingException;
import ua.dev.food.fast.service.repository.*;
import ua.dev.jwt.service.JwtDecodeService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderPurchaseServiceImpl implements OrderPurchaseService {
    private final AddressOrderRepository addressOrderRepository;
    private final BasicOrderRepository basicOrderRepository;
    private final BasicOrderUserRepository basicOrderUserRepository;
    private final BasicOrderGuestRepository basicOrderGuestRepository;
    private final PurchaseRepository purchaseRepository;
    private final JwtDecodeService jwtDecodeService;
    private final ProductFeignClient productFeignClient;
    private final AmqpTemplate amqpTemplate;
    private final StatusOrderRepository statusOrderRepository;
    private final StatusRepository statusRepository;
    private final ProcessingOrderRepository processingOrderRepository;

    @Override
    public Mono<Void> addOrderWithPurchaseUser(String authHeader, OrderPurchaseRequest orderPurchaseRequest) {
        Long userId = jwtDecodeService.extractUserDataFromBearerHeader(authHeader);
        return Mono.defer(() -> {
            AddressOrder addressOrder = createAddressOrder(orderPurchaseRequest);

            return saveAddressOrderIfDelivery(orderPurchaseRequest, addressOrder)
                .then(Mono.defer(() -> {
                    BasicOrder basicOrder = createBasicOrder(orderPurchaseRequest, addressOrder);
                    return basicOrderRepository.save(basicOrder);
                }))
                .flatMap(basicOrder -> {
                    BasicOrderUser basicOrderUser = BasicOrderUser.builder()
                        .userId(userId)
                        .basicOrderId(basicOrder.getId())
                        .build();
                    return basicOrderUserRepository.save(basicOrderUser)
                        .thenMany(Flux.fromIterable(orderPurchaseRequest.getPurchaseItems())
                            .flatMap(purchaseItemRequest -> addPurchaseItem(purchaseItemRequest, basicOrderUser))
                            .collectList())
                        .then(saveInitialOrderStatus(basicOrder.getId(), State.PENDING))
                        .then();
                })
                .doOnError(e -> log.error("Error occurred while adding order for user: {}", e.getMessage(), e))
                .then();
        });
    }

    private Mono<Void> saveInitialOrderStatus(Long orderId, State state) {
        return statusRepository.findStatusByState(state.name())
            .flatMap(pendingStatus ->
                statusOrderRepository.findByOrderId(orderId)
                    .doOnSuccess(v -> System.out.println("72 " + v))
                    .flatMap(existingStatusOrder -> {
                        System.out.println("Existing StatusOrder: " + existingStatusOrder);

                        existingStatusOrder.setStatusId(pendingStatus.getId());
                        return statusOrderRepository.save(existingStatusOrder);
                    })
                    .switchIfEmpty(
                        Mono.defer(() -> {
                            StatusOrder newStatusOrder = StatusOrder.builder()
                                .orderId(orderId)
                                .statusId(pendingStatus.getId())
                                .build();
                            return statusOrderRepository.save(newStatusOrder);
                        })
                    )
            )
            .then();
    }

    @Override
    public Flux<ProductPurchaseDto> getGuestPurchases(Long orderGuestId) {
        return purchaseRepository.findByBasicOrderGuestId(orderGuestId)
            .flatMap(purchase -> productFeignClient.getProductById(purchase.getProductId())
                .switchIfEmpty(Mono.error(new OrderProcessingException("Product not found...")))
                .map(product -> ProductPurchaseDto.builder()
                    .id(product.id())
                    .title(product.title())
                    .price(product.price())
                    .likes(product.likes())
                    .image01(product.image01())
                    .image02(product.image02())
                    .image03(product.image03())
                    .category(product.category())
                    .description(product.description())
                    .totalPrice(purchase.getTotalPrice())
                    .quantity(purchase.getQuantity())
                    .build())
            );
    }

    @Override
    public Flux<OrderPurchaseResponse> getAllOrder() {
        return basicOrderRepository.findAll()
            .flatMap(basicOrder -> {
                Mono<BasicOrderUser> userMono = basicOrderUserRepository.findByBasicOrderId(basicOrder.getId());
                Mono<BasicOrderGuest> guestMono = basicOrderGuestRepository.findByBasicOrderId(basicOrder.getId());
                return Mono.firstWithValue(userMono, guestMono)
                    .doOnNext(v -> System.out.println(v))
                    .switchIfEmpty(Mono.error(new OrderProcessingException("No user or guest associated with the order ID: " + basicOrder.getId())))
                    .flatMap(value -> {
                        if (value instanceof BasicOrderUser user) {
                            return createOrderPurchaseResponse(basicOrder, user);
                        } else if (value instanceof BasicOrderGuest guest) {
                            return createOrderPurchaseResponse(basicOrder, guest);
                        } else {
                            return Mono.error(new OrderProcessingException("Unexpected result type."));
                        }
                    })
                    .doOnNext(v -> System.out.println(v))
                    .switchIfEmpty(Mono.error(new OrderProcessingException("No user or guest associated with the order.")))
                    .doOnError(e -> log.error("Error: ", e));

            });
    }

    @Override
    public Mono<Void> orderAcceptance(String authHeader, List<Long> orderIds, String status) {
        Long courierId = jwtDecodeService.extractUserDataFromBearerHeader(authHeader);

        System.out.println(orderIds);

        return Flux.fromIterable(orderIds)
            .flatMap(orderId ->
                saveInitialOrderStatus(orderId, State.IN_TRANSIT)
                    .then(processingOrderRepository.save(
                        ProcessingOrder.builder()
                            .courierId(courierId)
                            .orderId(orderId)
                            .build()))
            )
            .then();
    }


    private Mono<OrderPurchaseResponse> createOrderPurchaseResponse(BasicOrder basicOrder, BasicOrderUser user) {
        return createCommonOrderResponse(basicOrder)
            .doOnSuccess(System.out::println)
            .flatMap(orderResponse -> {
                return purchaseRepository.findByBasicOrderUserId(user.getBasicOrderId())
                    .flatMap(purchase -> productFeignClient.getProductById(purchase.getProductId())
                        .doOnSuccess(System.out::println)
                        .map(product -> new PurchaseItemResponse(
                            product.id(),
                            product.image01(),
                            product.title(),
                            product.price(),
                            purchase.getQuantity(),
                            purchase.getTotalPrice()
                        )))
                    .collectList()
                    .doOnNext(orderResponse::setPurchaseItems)
                    .thenReturn(orderResponse);
            })
            .flatMap(orderResponse -> addStatusInOrderPurchaseResponse(orderResponse, user.getBasicOrderId()));

    }

    private Mono<OrderPurchaseResponse> createOrderPurchaseResponse(BasicOrder basicOrder, BasicOrderGuest guest) {
        return createCommonOrderResponse(basicOrder)
            .flatMap(orderResponse -> {
                orderResponse.setCity(guest.getName());
                orderResponse.setPhone(guest.getContactEmail());

                return purchaseRepository.findByBasicOrderGuestId(guest.getBasicOrderId())
                    .flatMap(purchase -> productFeignClient.getProductById(purchase.getProductId())
                        .map(product -> new PurchaseItemResponse(
                            product.id(),
                            product.image01(),
                            product.title(),
                            product.price(),
                            purchase.getQuantity(),
                            purchase.getTotalPrice()
                        )))
                    .collectList()
                    .doOnNext(orderResponse::setPurchaseItems)
                    .thenReturn(orderResponse);
            })
            .flatMap(orderResponse -> addStatusInOrderPurchaseResponse(orderResponse, guest.getBasicOrderId())
            );
    }

    Mono<OrderPurchaseResponse> addStatusInOrderPurchaseResponse(OrderPurchaseResponse orderPurchaseResponse, Long basicOrderId) {
        return statusOrderRepository.findByOrderId(basicOrderId)
            .switchIfEmpty(Mono.error(new RuntimeException("StatusOrder not found for orderId: " + basicOrderId)))
            .flatMap(statusOrder -> statusRepository.findById(statusOrder.getStatusId())
                .switchIfEmpty(Mono.error(new RuntimeException("Status not found for statusId: " + statusOrder.getStatusId())))
            )
            .doOnNext(status -> {
                orderPurchaseResponse.setStatusOrder(status.getState().name());
                System.out.println("Status set: " + status.getState().name());
            })
            .thenReturn(orderPurchaseResponse)
            .doOnError(error -> System.err.println("Error in populateStatusOrder: " + error.getMessage()));
    }

    private Mono<OrderPurchaseResponse> createCommonOrderResponse(BasicOrder basicOrder) {
        OrderPurchaseResponse response = createOrderPurchaseResponse(basicOrder);

        if (basicOrder.getAddressOrderId() != null) {
            return addressOrderRepository.findById(basicOrder.getAddressOrderId())
                .doOnNext(address -> {
                    response.setCity(address.getCity());
                    response.setStreet(address.getStreet());
                    response.setHouseNumber(address.getHouseNumber());
                    response.setFlatNumber(address.getFlatNumber());
                    response.setFloorNumber(address.getFloorNumber());
                })
                .doOnSuccess(System.out::println)
                .thenReturn(response);
        } else {
            return Mono.just(response);
        }
    }


    public Mono<Void> addOrderWithPurchaseGuest(OrderPurchaseRequest orderPurchaseRequest) {
        AddressOrder addressOrder = createAddressOrder(orderPurchaseRequest);

        return saveAddressOrderIfDelivery(orderPurchaseRequest, addressOrder)
            .then(Mono.defer(() -> {
                BasicOrder basicOrder = createBasicOrder(orderPurchaseRequest, addressOrder);
                return basicOrderRepository.save(basicOrder);
            }))
            .flatMap(basicOrder -> {
                BasicOrderGuest basicOrderGuest = BasicOrderGuest.builder()
                    .name(orderPurchaseRequest.getName())
                    .contactEmail(orderPurchaseRequest.getEmail())
                    .basicOrderId(basicOrder.getId())
                    .build();

                return basicOrderGuestRepository.save(basicOrderGuest)
                    .thenMany(Flux.fromIterable(orderPurchaseRequest.getPurchaseItems())
                        .flatMap(purchaseItemRequest -> addPurchaseItem(purchaseItemRequest, basicOrderGuest))
                    )
                    .collectList() // Collect all purchases before proceeding
                    .doOnNext(purchaseList -> {

                        OrderInfoRequestDto orderInfoRequestDto = OrderInfoRequestDto.builder()
                            .email(orderPurchaseRequest.getEmail())
                            .firstName(orderPurchaseRequest.getName())
                            .orderGuestId(basicOrderGuest.getBasicOrderId())
                            .phone(orderPurchaseRequest.getPhone())
                            .dateArrived(orderPurchaseRequest.getDateArrived())
                            .timeArrived(orderPurchaseRequest.getTimeArrived())
                            .totalAmount(orderPurchaseRequest.getTotalAmount())
                            .delivery(orderPurchaseRequest.getDelivery())
                            .build();

                        amqpTemplate.convertAndSend("email-exchange", "email-routing-key", orderInfoRequestDto);
                    })
                    .then(saveInitialOrderStatus(basicOrder.getId(), State.PENDING));
            })
            .doOnError(e -> log.error("Error occurred while adding order for guest: {}", e.getMessage(), e))
            .then();
    }


    @Override
    public Flux<OrderPurchaseResponse> getOrdersWithPurchaseUser(String authHeader) {
        Long userId = jwtDecodeService.extractUserDataFromBearerHeader(authHeader);
        return Flux.defer(() -> basicOrderUserRepository.findBasicOrderUserByUserId(userId)
            .flatMap(this::createOrderPurchaseResponse)
            .flatMap(orderResponse -> addStatusInOrderPurchaseResponse(orderResponse, orderResponse.getId()))
            .onErrorResume(e -> {
                log.error("Error occurred while retrieving orders: {}", e.getMessage(), e);
                return Flux.empty();
            }));
    }

    @Override
    public Flux<OrderPurchaseResponse> getOrdersCourierProcesses(String authHeader) {
        Long courierId = jwtDecodeService.extractUserDataFromBearerHeader(authHeader);

        return processingOrderRepository.findAllByCourierId(courierId)
            .flatMap(processingOrder -> {
                Long orderId = processingOrder.getOrderId();

                // Ищем, связан ли заказ с пользователем или гостем
                Mono<BasicOrderUser> userMono = basicOrderUserRepository.findByBasicOrderId(orderId);
                Mono<BasicOrderGuest> guestMono = basicOrderGuestRepository.findByBasicOrderId(orderId);

                return Mono.firstWithValue(userMono, guestMono)
                    .switchIfEmpty(Mono.error(new OrderProcessingException("No user or guest associated with the order ID: " + orderId)))
                    .flatMap(value -> {
                        if (value instanceof BasicOrderUser user) {
                            return createOrderPurchaseResponse(user)
                                .flatMap(orderResponse -> addStatusInOrderPurchaseResponse(orderResponse, orderId));
                        } else if (value instanceof BasicOrderGuest guest) {
                            return createOrderPurchaseResponseForGuest(guest)
                                .flatMap(orderResponse -> addStatusInOrderPurchaseResponse(orderResponse, orderId));
                        } else {
                            return Mono.error(new OrderProcessingException("Unexpected result type for order ID: " + orderId));
                        }
                    });
            })
            .doOnError(e -> log.error("Error retrieving orders for courier: {}", e.getMessage(), e));
    }

    private Mono<OrderPurchaseResponse> createOrderPurchaseResponseForGuest(BasicOrderGuest guest) {
        return basicOrderRepository.findById(guest.getBasicOrderId())
            .flatMap(basicOrder -> {
                OrderPurchaseResponse orderResponse = createOrderPurchaseResponse(basicOrder);

                // Добавляем адрес, если он существует
                Mono<Void> addressMono = basicOrder.getAddressOrderId() != null
                    ? addressOrderRepository.findById(basicOrder.getAddressOrderId())
                    .doOnNext(address -> {
                        orderResponse.setCity(address.getCity());
                        orderResponse.setStreet(address.getStreet());
                        orderResponse.setHouseNumber(address.getHouseNumber());
                        orderResponse.setFlatNumber(address.getFlatNumber());
                        orderResponse.setFloorNumber(address.getFloorNumber());
                    })
                    .then()
                    : Mono.empty();

                // Получаем элементы заказа
                Mono<List<PurchaseItemResponse>> purchaseItemsMono = purchaseRepository.findByBasicOrderGuestId(guest.getBasicOrderId())
                    .flatMap(purchase -> productFeignClient.getProductById(purchase.getProductId())
                        .switchIfEmpty(Mono.error(new OrderProcessingException("Product not found for ID: " + purchase.getProductId())))
                        .map(product -> new PurchaseItemResponse(
                            product.id(),
                            product.image01(),
                            product.title(),
                            product.price(),
                            purchase.getQuantity(),
                            purchase.getTotalPrice()
                        )))
                    .collectList();

                return purchaseItemsMono.flatMap(purchaseItems -> {
                    orderResponse.setPurchaseItems(purchaseItems);
                    return addressMono.thenReturn(orderResponse);
                });
            });
    }

    private Mono<AddressOrder> saveAddressOrderIfDelivery(OrderPurchaseRequest orderPurchaseRequest, AddressOrder addressOrder) {
        if (orderPurchaseRequest.getDelivery()) {
            return addressOrderRepository.save(addressOrder);
        }
        return Mono.empty();
    }

    private <T> Mono<Purchase> addPurchaseItem(PurchaseItemRequest purchaseItemRequest, T basicOrder) {
        Long basicOrderUserId = basicOrder instanceof BasicOrderUser ? ((BasicOrderUser) basicOrder).getBasicOrderId() : null;
        Long basicOrderGuestId = basicOrder instanceof BasicOrderGuest ? ((BasicOrderGuest) basicOrder).getBasicOrderId() : null;
        return productFeignClient.getProductById(purchaseItemRequest.getIdProduct())
            .switchIfEmpty(Mono.error(new OrderProcessingException("Product not found...")))
            .map(product -> Purchase.builder()
                .productId(product.id())
                .totalPrice(purchaseItemRequest.getTotalPrice())
                .quantity(purchaseItemRequest.getQuantity())
                .basicOrderUserId(basicOrderUserId)
                .basicOrderGuestId(basicOrderGuestId)
                .build())
            .flatMap(purchaseRepository::save);
    }

    private Mono<OrderPurchaseResponse> createOrderPurchaseResponse(BasicOrderUser orderUser) {
        return basicOrderRepository.findById(orderUser.getBasicOrderId())
            .flatMap(basicOrder -> {
                OrderPurchaseResponse orderResponse = createOrderPurchaseResponse(basicOrder);
                // Fetch purchases and convert them into PurchaseItemResponse
                Mono<List<PurchaseItemResponse>> purchaseItemsMono = purchaseRepository.findByBasicOrderUserId(orderUser.getBasicOrderId())
                    .flatMap(purchase -> productFeignClient.getProductById(purchase.getProductId())
                        .switchIfEmpty(Mono.error(new OrderProcessingException("Product not found...")))
                        .map(productResponseDto -> {
                            PurchaseItemResponse purchaseResponse = new PurchaseItemResponse();
                            purchaseResponse.setId(productResponseDto.id());
                            purchaseResponse.setImage01(productResponseDto.image01());
                            purchaseResponse.setTitle(productResponseDto.title());
                            purchaseResponse.setPrice(productResponseDto.price());
                            purchaseResponse.setQuantity(purchase.getQuantity());
                            purchaseResponse.setTotalPrice(purchase.getTotalPrice());
                            return purchaseResponse;
                        }))
                    .collectList();

                // Fetch address details if present
                Mono<Void> addressUpdateMono;
                if (basicOrder.getAddressOrderId() != null) {
                    addressUpdateMono = addressOrderRepository.findById(basicOrder.getAddressOrderId())
                        .doOnNext(addressOrder -> {
                            orderResponse.setCity(addressOrder.getCity());

                            orderResponse.setStreet(addressOrder.getStreet());
                            orderResponse.setHouseNumber(addressOrder.getHouseNumber());
                            orderResponse.setFlatNumber(addressOrder.getFlatNumber());
                            orderResponse.setFloorNumber(addressOrder.getFloorNumber());
                        })
                        .then();
                } else {
                    addressUpdateMono = Mono.empty();
                }

                // Combine both flows and build the final response
                return purchaseItemsMono
                    .flatMap(purchaseItems -> {
                        orderResponse.setPurchaseItems(purchaseItems);
                        return addressUpdateMono.then(Mono.just(orderResponse));
                    });
            });
    }

    private OrderPurchaseResponse createOrderPurchaseResponse(BasicOrder basicOrder) {
        OrderPurchaseResponse orderResponse = new OrderPurchaseResponse();
        orderResponse.setId(basicOrder.getId());
        orderResponse.setPhone(basicOrder.getPhone());
        orderResponse.setDateArrived(basicOrder.getOrderDateArrived());
        orderResponse.setTimeArrived(basicOrder.getOrderTimeArrived());
        orderResponse.setTotalAmount(basicOrder.getTotalAmount());
        orderResponse.setDelivery(basicOrder.getAddressOrderId() != null);
        orderResponse.setOrderTimestamp(basicOrder.getOrderTimestamp());
        return orderResponse;
    }


    private AddressOrder createAddressOrder(OrderPurchaseRequest orderPurchaseRequest) {
        return AddressOrder.builder()
            .city(orderPurchaseRequest.getCity())
            .street(orderPurchaseRequest.getStreet())
            .houseNumber(orderPurchaseRequest.getHouseNumber())
            .flatNumber(orderPurchaseRequest.getFlatNumber())
            .floorNumber(orderPurchaseRequest.getFloorNumber())
            .build();
    }

    private BasicOrder createBasicOrder(OrderPurchaseRequest orderPurchaseRequest, AddressOrder addressOrder) {
        return BasicOrder.builder()
            .phone(orderPurchaseRequest.getPhone())
            .orderDateArrived(orderPurchaseRequest.getDateArrived())
            .orderTimeArrived(orderPurchaseRequest.getTimeArrived())
            .totalAmount(orderPurchaseRequest.getTotalAmount())
            .addressOrderId(orderPurchaseRequest.getDelivery() ? addressOrder.getId() : null)
            .cashPayment(orderPurchaseRequest.getCashPayment())
            .orderTimestamp(LocalDateTime.now())
            .build();
    }
}
