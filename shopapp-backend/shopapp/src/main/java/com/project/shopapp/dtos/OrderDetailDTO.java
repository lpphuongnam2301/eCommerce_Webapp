package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("product_id")
    private Long productId;

    private Long price;

    @Min(value = 1)
    @JsonProperty("number_of_product")
    private int number_of_product;

    @JsonProperty("total_money")
    @Min(value = 0)
    private int totalMoney;

    private String color;
}
