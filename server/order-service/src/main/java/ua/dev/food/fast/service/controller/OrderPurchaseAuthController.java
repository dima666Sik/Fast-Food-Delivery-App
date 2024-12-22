package ua.dev.food.fast.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.dto.request.OrderPurchaseRequest;
import ua.dev.food.fast.service.domain.dto.response.OrderPurchaseResponse;
import ua.dev.food.fast.service.service.OrderPurchaseService;

import java.util.List;

/***
 * Controller for handling order purchase related requests.
 * Provides endpoints for adding orders, retrieving orders with purchase users,
 * accepting orders, and retrieving orders in courier processes.
 */
@RestController
@RequestMapping("/api/v2/order-purchase/private")
@RequiredArgsConstructor
public class OrderPurchaseAuthController {

    private final OrderPurchaseService orderPurchaseServiceImpl;

    /**
     * Endpoint to add an order with purchase user details.
     *
     * @param authHeader           The authorization header for authentication.
     * @param orderPurchaseRequest The order purchase details in the request body.
     * @return A response indicating the success of the operation.
     */
    @PostMapping("/add-order-with-purchase-user")
    public Mono<ResponseEntity<String>> addOrderWithPurchaseUser(@RequestHeader(value = "Authorization") String authHeader,
                                                                 @RequestBody OrderPurchaseRequest orderPurchaseRequest) {
        return orderPurchaseServiceImpl.addOrderWithPurchaseUser(authHeader, orderPurchaseRequest)
            .then(Mono.fromCallable(() -> ResponseEntity.ok("Order was add successfully")));
    }

    /**
     * Endpoint to retrieve orders with associated purchase users.
     *
     * @param authHeader The authorization header for authentication.
     * @return A response containing a list of orders with purchase users.
     */
    @GetMapping("/get-orders-with-purchase-user")
    public Mono<ResponseEntity<Flux<OrderPurchaseResponse>>> getOrdersWithPurchaseUser(@RequestHeader(value = "Authorization") String authHeader) {
        return Mono.just(ResponseEntity.ok().body(orderPurchaseServiceImpl.getOrdersWithPurchaseUser(authHeader)));
    }

    /**
     * Endpoint to accept orders and update their status.
     *
     * @param authHeader The authorization header for authentication.
     * @param orderIds   The list of order IDs to be updated.
     * @param status     The status to be applied to the orders.
     * @return A response indicating the success of the operation.
     */
    @PostMapping("/order-acceptance")
    public Mono<ResponseEntity<String>> orderAcceptance(@RequestHeader(value = "Authorization") String authHeader,
                                                        @RequestParam("order_ids") List<Long> orderIds,
                                                        @RequestParam("status") String status) {
        return orderPurchaseServiceImpl.orderAcceptance(authHeader, orderIds, status)
            .then(Mono.fromCallable(() -> ResponseEntity.ok("Orders status was add successfully")));
    }

    /**
     * Endpoint to retrieve orders in the courier process.
     *
     * @param authHeader The authorization header for authentication.
     * @return A response containing a list of orders in the courier process.
     */
    @GetMapping("/get-orders-courier-processes")
    public Mono<ResponseEntity<Flux<OrderPurchaseResponse>>> getOrdersCourierProcesses(@RequestHeader(value = "Authorization") String authHeader) {
        return Mono.just(ResponseEntity.ok().body(orderPurchaseServiceImpl.getOrdersCourierProcesses(authHeader)));
    }
}
