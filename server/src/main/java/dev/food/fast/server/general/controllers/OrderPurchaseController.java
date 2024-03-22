package dev.food.fast.server.general.controllers;

import dev.food.fast.server.general.dto.request.OrderPurchaseRequest;
import dev.food.fast.server.general.service.OrderPurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order-purchase")
@RequiredArgsConstructor
public class OrderPurchaseController {
    private final OrderPurchaseService orderPurchaseService;

    @PostMapping("/add-order-with-purchase-guest")
    public ResponseEntity<?> addOrderWithPurchaseGuest(@RequestBody OrderPurchaseRequest orderPurchaseRequest) {
        return orderPurchaseService.addOrderWithPurchaseGuest(orderPurchaseRequest);
    }

}
