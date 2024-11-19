package ua.dev.feign.clients;

import lombok.Builder;

@Builder
public record UserDto(String email, String firstName, String lastName) {
}
