package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("fullname")
    private String fullName;

    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Phone number is invalid")
    private String phoneNumber;

    private String address;
    private String note;

    @JsonProperty("total_money")
    private Float totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_date")
    private Date shippingDate;
    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("shipping_address")
    //@NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @JsonProperty("payment_method")
    private String paymentMethod;
}
