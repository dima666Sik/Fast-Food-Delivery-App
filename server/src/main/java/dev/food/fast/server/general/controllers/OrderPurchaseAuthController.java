package dev.food.fast.server.general.controllers;

import dev.food.fast.server.general.dto.request.OrderPurchaseRequest;
import dev.food.fast.server.general.service.OrderPurchaseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/private/order-purchase")
@RequiredArgsConstructor
public class OrderPurchaseAuthController {
    private final OrderPurchaseService orderPurchaseService;

    @PostMapping("/add-order-with-purchase-user")
    public ResponseEntity<?> addOrderWithPurchaseUser(Authentication authentication,
                                                      @RequestBody OrderPurchaseRequest orderPurchaseRequest) {
        return orderPurchaseService.addOrderWithPurchaseUser(authentication, orderPurchaseRequest);
    }

    @GetMapping("/get-order-with-purchase-user")
    public ResponseEntity<?> getOrderWithPurchaseUser(Authentication authentication) {
        return orderPurchaseService.getOrderWithPurchaseUser(authentication);
    }


}
