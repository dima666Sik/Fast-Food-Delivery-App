package dev.food.fast.server.general.service;

import dev.food.fast.server.auth.models.User;
import dev.food.fast.server.auth.dto.response.MessageResponse;
import dev.food.fast.server.auth.repository.UserRepository;
import dev.food.fast.server.auth.service.JwtService;
import dev.food.fast.server.general.dto.response.OrderPurchaseResponse;
import dev.food.fast.server.general.dto.response.PurchaseItemResponse;
import dev.food.fast.server.general.models.order.*;
import dev.food.fast.server.general.models.product.Product;
import dev.food.fast.server.general.dto.request.PurchaseItemRequest;
import dev.food.fast.server.general.dto.request.OrderPurchaseRequest;
import dev.food.fast.server.general.repository.*;
import dev.food.fast.server.mail.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderPurchaseService {
    private final UserRepository userRepository;
    private final ProductsRepository productsRepository;
    private final AddressOrderRepository addressOrderRepository;
    private final BasicOrderRepository basicOrderRepository;
    private final BasicOrderUserRepository basicOrderUserRepository;
    private final BasicOrderGuestRepository basicOrderGuestRepository;
    private final PurchaseRepository purchaseRepository;
    private final EmailService emailService;

    public ResponseEntity<?> addOrderWithPurchaseUser(Authentication authentication, OrderPurchaseRequest orderPurchaseRequest) {
        try {
            String userEmail = authentication.getName();
            var userOptional = userRepository.findByEmail(userEmail);
            if (userOptional.isEmpty()) {
                return ResponseEntity.ok()
                        .body(MessageResponse.builder()
                                .message("User not found...")
                                .status(false)
                                .build());
            }
            User user = userOptional.get();

            AddressOrder addressOrder = createAddressOrder(orderPurchaseRequest);
            BasicOrder basicOrder = createBasicOrder(orderPurchaseRequest, addressOrder);

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

            if (orderPurchaseRequest.getDelivery()) addressOrderRepository.save(addressOrder);
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

        AddressOrder addressOrder = createAddressOrder(orderPurchaseRequest);
        BasicOrder basicOrder = createBasicOrder(orderPurchaseRequest, addressOrder);

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

        if (orderPurchaseRequest.getDelivery()) addressOrderRepository.save(addressOrder);
        basicOrderRepository.save(basicOrder);
        basicOrderGuestRepository.save(basicOrderGuest);
        purchaseRepository.saveAll(purchaseList);

        return emailService.sendOrderOnEmailGuest(orderPurchaseRequest,basicOrder.getId(),purchaseList);
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

        if (orderPurchaseRequest.getDelivery()) {
            return BasicOrder.builder()
                    .phone(orderPurchaseRequest.getPhone())
                    .orderDateArrived(orderPurchaseRequest.getDateArrived())
                    .orderTimeArrived(orderPurchaseRequest.getTimeArrived())
                    .totalAmount(orderPurchaseRequest.getTotalAmount())
                    .addressOrder(addressOrder)
                    .cashPayment(orderPurchaseRequest.getCashPayment())
                    .build();
        }
        else {
            return BasicOrder.builder()
                    .phone(orderPurchaseRequest.getPhone())
                    .orderDateArrived(orderPurchaseRequest.getDateArrived())
                    .orderTimeArrived(orderPurchaseRequest.getTimeArrived())
                    .totalAmount(orderPurchaseRequest.getTotalAmount())
                    .cashPayment(orderPurchaseRequest.getCashPayment())
                    .build();
        }
    }

    public ResponseEntity<?> getOrderWithPurchaseUser(Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            var userOptional = userRepository.findByEmail(userEmail);
            if (userOptional.isEmpty()) {
                return ResponseEntity.ok()
                        .body(MessageResponse.builder()
                                .message("User not found...")
                                .status(false)
                                .build());
            }
            User user = userOptional.get();

            List<BasicOrderUser> orders = basicOrderUserRepository.findByUserOrderByIdDesc(user);
            List<OrderPurchaseResponse> orderResponses = new ArrayList<>();

            for (BasicOrderUser orderUser : orders) {
                OrderPurchaseResponse orderResponse = createOrderPurchaseResponse(orderUser);
                orderResponses.add(orderResponse);
            }

            return ResponseEntity.ok(orderResponses);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Failed to retrieve orders");
        }
    }

    private OrderPurchaseResponse createOrderPurchaseResponse(BasicOrderUser orderUser) {
        BasicOrder basicOrder = orderUser.getBasicOrder();
        AddressOrder addressOrder = basicOrder.getAddressOrder();

        OrderPurchaseResponse orderResponse = new OrderPurchaseResponse();
        orderResponse.setId(basicOrder.getId());
        orderResponse.setPhone(basicOrder.getPhone());
        orderResponse.setDateArrived(basicOrder.getOrderDateArrived());
        orderResponse.setTimeArrived(basicOrder.getOrderTimeArrived());
        orderResponse.setTotalAmount(basicOrder.getTotalAmount());
        orderResponse.setDelivery(basicOrder.getAddressOrder() != null);

        if (addressOrder != null) {
            orderResponse.setCity(addressOrder.getCity());
            orderResponse.setStreet(addressOrder.getStreet());
            orderResponse.setHouseNumber(addressOrder.getHouseNumber());
            orderResponse.setFlatNumber(addressOrder.getFlatNumber());
            orderResponse.setFloorNumber(addressOrder.getFloorNumber());
        }

        List<Purchase> purchases = purchaseRepository.findByBasicOrderUser(orderUser);
        List<PurchaseItemResponse> purchaseResponses = new ArrayList<>();

        for (Purchase purchase : purchases) {
            PurchaseItemResponse purchaseResponse = new PurchaseItemResponse();
            purchaseResponse.setId(purchase.getProduct().getId());
            purchaseResponse.setImage01(purchase.getProduct().getImage01());
            purchaseResponse.setTitle(purchase.getProduct().getTitle());
            purchaseResponse.setPrice(purchase.getProduct().getPrice());
            purchaseResponse.setQuantity(purchase.getQuantity());
            purchaseResponse.setTotalPrice(purchase.getTotalPrice());

            purchaseResponses.add(purchaseResponse);
        }

        orderResponse.setPurchaseItems(purchaseResponses);
        return orderResponse;
    }
}
