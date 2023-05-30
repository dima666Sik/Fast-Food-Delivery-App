package dev.food.fast.server.general.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
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

    @JsonProperty("order_date")
    private String date;
    @JsonProperty("order_time")
    private String time;

    @JsonProperty("total_amount")
    private Double totalAmount;

    private List<PurchaseItemRequest> purchaseItems;
}
