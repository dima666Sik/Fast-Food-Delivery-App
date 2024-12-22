package ua.dev.food.fast.service.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderPurchaseRequest {
    @JsonProperty("name")
    private String name;
    @JsonProperty("contact_email")
    private String email;
    @JsonProperty("phone")
    private String phone;

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

    @JsonProperty("order_date_arrived")
    private String dateArrived;
    @JsonProperty("order_time_arrived")
    private String timeArrived;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;
    @JsonProperty("delivery")
    private Boolean delivery;

    @JsonProperty("cash_payment")
    private Boolean cashPayment;

    private List<PurchaseItemRequest> purchaseItems;
}
