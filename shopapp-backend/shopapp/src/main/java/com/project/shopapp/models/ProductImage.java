package com.project.shopapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductImage {

    public static final int MAXIMUM_IMAGES = 10;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @JsonProperty("image_url")
    @Column(name = "image_url", length = 300)
    private String imageUrl;
}
