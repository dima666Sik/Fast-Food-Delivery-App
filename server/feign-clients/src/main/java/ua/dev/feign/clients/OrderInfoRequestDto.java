package ua.dev.feign.clients;

import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
public record OrderInfoRequestDto(String email, String firstName, Long orderGuestId,
                                  String phone, String dateArrived, String timeArrived, BigDecimal totalAmount,
                                  Boolean delivery) implements Serializable {
}
