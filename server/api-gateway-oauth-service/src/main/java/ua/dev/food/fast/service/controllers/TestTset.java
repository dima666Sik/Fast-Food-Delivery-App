package ua.dev.food.fast.service.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.dto.response.MessageResponse;

@RestController
public class TestTset {
    @GetMapping("/mmm")
    public Mono<ResponseEntity<MessageResponse>> test() {
        MessageResponse response = new MessageResponse("Your message here",true, null);
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response));
    }

}
