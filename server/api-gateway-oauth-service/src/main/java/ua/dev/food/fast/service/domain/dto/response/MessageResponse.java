package ua.dev.food.fast.service.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {

    @JsonProperty("message_response")
    private String message;
    @JsonProperty("status_response")
    private boolean status;
    @JsonProperty("main_response")
    private AuthenticationResponse mainResponse;
}
