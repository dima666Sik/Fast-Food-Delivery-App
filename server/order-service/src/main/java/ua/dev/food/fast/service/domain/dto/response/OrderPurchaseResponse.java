package ua.dev.food.fast.service.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private BigDecimal totalAmount;
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
    @JsonProperty("order_timestamp")
    private LocalDateTime orderTimestamp;
    @JsonProperty("status_order")
    private String statusOrder;

    private List<PurchaseItemResponse> purchaseItems;
}
