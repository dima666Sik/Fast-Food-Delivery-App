package dev.food.fast.server.general.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPurchaseResponse {
    private Long id;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("order_date_arrived")
    private String dateArrived;
    @JsonProperty("order_time_arrived")
    private String timeArrived;
    @JsonProperty("total_amount")
    private Double totalAmount;
    @JsonProperty("delivery")
    private Boolean delivery;
    @JsonProperty("city")
    private String city;
    @JsonProperty("street")
    private String street;
    @JsonProperty("house_number")
    private String houseNumber;
    @JsonProperty("flat_number")
    private String flatNumber;
    @JsonProperty("floor_number")
    private String floorNumber;

    private List<PurchaseItemResponse> purchaseItems;
}
