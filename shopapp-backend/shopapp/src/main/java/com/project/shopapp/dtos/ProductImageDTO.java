package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Product;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductImageDTO {
    @JsonProperty("product_id")
    private Product productId;

    @JsonProperty("image_url")
    private String imageUrl;
}
