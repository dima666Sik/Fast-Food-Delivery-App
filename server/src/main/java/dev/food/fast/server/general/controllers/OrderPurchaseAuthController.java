package dev.food.fast.server.general.controllers;

import dev.food.fast.server.general.pojo.OrderPurchaseRequest;
import dev.food.fast.server.general.service.OrderPurchaseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/private/order-purchase")
@RequiredArgsConstructor
public class OrderPurchaseAuthController {
    private final OrderPurchaseService orderPurchaseService;

    @PostMapping("/add-order-with-purchase-user")
    public ResponseEntity<?> addOrderWithPurchaseUser(@NonNull HttpServletRequest request,
                                                  @RequestBody OrderPurchaseRequest orderPurchaseRequest) {
        return orderPurchaseService.addOrderWithPurchaseUser(request, orderPurchaseRequest);
    }

    @PostMapping("/add-order-with-purchase-guest")
    public ResponseEntity<?> addOrderWithPurchaseGuest(@RequestBody OrderPurchaseRequest orderPurchaseRequest) {
        return orderPurchaseService.addOrderWithPurchaseGuest(orderPurchaseRequest);
    }
}
