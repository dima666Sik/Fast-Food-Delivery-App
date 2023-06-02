package dev.food.fast.server.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataUserResponse {
    @JsonProperty("first_name")
    private String firstname;
    @JsonProperty("last_name")
    private String lastname;
    private String email;
}
