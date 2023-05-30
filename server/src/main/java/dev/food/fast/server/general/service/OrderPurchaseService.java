package dev.food.fast.server.general.service;

import dev.food.fast.server.auth.models.User;
import dev.food.fast.server.auth.pojo.MessageResponse;
import dev.food.fast.server.auth.repository.UserRepository;
import dev.food.fast.server.auth.service.JwtService;
import dev.food.fast.server.general.models.order.*;
import dev.food.fast.server.general.models.product.Product;
import dev.food.fast.server.general.pojo.PurchaseItemRequest;
import dev.food.fast.server.general.pojo.OrderPurchaseRequest;
import dev.food.fast.server.general.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderPurchaseService {
    private final JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private AddressOrderRepository addressOrderRepository;
    @Autowired
    private BasicOrderRepository basicOrderRepository;
    @Autowired
    private BasicOrderUserRepository basicOrderUserRepository;
    @Autowired
    private BasicOrderGuestRepository basicOrderGuestRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;

    public ResponseEntity<?> addOrderWithPurchaseUser(HttpServletRequest request, OrderPurchaseRequest orderPurchaseRequest) {
        final String authHeader = request.getHeader("Authorization");

        final String jwt = authHeader.substring(7);

        final String userEmail;

        try {
            userEmail = jwtService.extractUsername(jwt);
            var userOptional = userRepository.findByEmail(userEmail);
            if (userOptional.isEmpty()) {
                return ResponseEntity.ok()
                        .body(MessageResponse.builder()
                                .message("User not found...")
                                .status(false)
                                .build());
            }
            User user = userOptional.get();

            AddressOrder addressOrder = AddressOrder.builder()
                    .city(orderPurchaseRequest.getCity())
                    .street(orderPurchaseRequest.getStreet())
                    .houseNumber(orderPurchaseRequest.getHouseNumber())
                    .flatNumber(orderPurchaseRequest.getFlatNumber())
                    .floorNumber(orderPurchaseRequest.getFloorNumber())
                    .build();

            BasicOrder basicOrder = BasicOrder.builder()
                    .phone(orderPurchaseRequest.getPhone())
                    .orderDate(orderPurchaseRequest.getDate())
                    .orderTime(orderPurchaseRequest.getTime())
                    .totalAmount(orderPurchaseRequest.getTotalAmount())
                    .addressOrder(addressOrder)
                    .build();

            BasicOrderUser basicOrderUser = BasicOrderUser.builder()
                    .user(user)
                    .basicOrder(basicOrder)
                    .build();

            List<PurchaseItemRequest> purchaseItemRequests = orderPurchaseRequest.getPurchaseItems();
            List<Purchase> purchaseList = new ArrayList<>();

            for (PurchaseItemRequest purchaseItemRequest : purchaseItemRequests) {
                var productOptional = productsRepository.findById(purchaseItemRequest.getId());

                if (productOptional.isEmpty()) {
                    return ResponseEntity.ok().body(MessageResponse.builder()
                            .message("Product not found...")
                            .status(false)
                            .build());
                }

                Product product = productOptional.get();

                Purchase purchase = Purchase.builder()
                        .product(product)
                        .totalPrice(purchaseItemRequest.getTotalPrice())
                        .quantity(purchaseItemRequest.getQuantity())
                        .basicOrderUser(basicOrderUser)
                        .build();
                purchaseList.add(purchase);
            }

            addressOrderRepository.save(addressOrder);
            basicOrderRepository.save(basicOrder);
            basicOrderUserRepository.save(basicOrderUser);
            purchaseRepository.saveAll(purchaseList);

            return ResponseEntity.ok("Order was add successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Order was add unsuccessfully");
        }

    }

    public ResponseEntity<?> addOrderWithPurchaseGuest(OrderPurchaseRequest orderPurchaseRequest) {

        AddressOrder addressOrder = AddressOrder.builder()
                .city(orderPurchaseRequest.getCity())
                .street(orderPurchaseRequest.getStreet())
                .houseNumber(orderPurchaseRequest.getHouseNumber())
                .flatNumber(orderPurchaseRequest.getFlatNumber())
                .floorNumber(orderPurchaseRequest.getFloorNumber())
                .build();

        BasicOrder basicOrder = BasicOrder.builder()
                .phone(orderPurchaseRequest.getPhone())
                .orderDate(orderPurchaseRequest.getDate())
                .orderTime(orderPurchaseRequest.getTime())
                .totalAmount(orderPurchaseRequest.getTotalAmount())
                .addressOrder(addressOrder)
                .build();

        BasicOrderGuest basicOrderGuest = BasicOrderGuest.builder()
                .name(orderPurchaseRequest.getName())
                .contactEmail(orderPurchaseRequest.getEmail())
                .basicOrder(basicOrder)
                .build();

        List<PurchaseItemRequest> purchaseItemRequests = orderPurchaseRequest.getPurchaseItems();
        List<Purchase> purchaseList = new ArrayList<>();

        for (PurchaseItemRequest purchaseItemRequest : purchaseItemRequests) {
            var productOptional = productsRepository.findById(purchaseItemRequest.getId());

            if (productOptional.isEmpty()) {
                return ResponseEntity.ok().body(MessageResponse.builder()
                        .message("Product not found...")
                        .status(false)
                        .build());
            }

            Product product = productOptional.get();

            Purchase purchase = Purchase.builder()
                    .product(product)
                    .totalPrice(purchaseItemRequest.getTotalPrice())
                    .quantity(purchaseItemRequest.getQuantity())
                    .basicOrderGuest(basicOrderGuest)
                    .build();
            purchaseList.add(purchase);
        }

        addressOrderRepository.save(addressOrder);
        basicOrderRepository.save(basicOrder);
        basicOrderGuestRepository.save(basicOrderGuest);
        purchaseRepository.saveAll(purchaseList);

        return ResponseEntity.ok("Order was add successfully");
    }

}
