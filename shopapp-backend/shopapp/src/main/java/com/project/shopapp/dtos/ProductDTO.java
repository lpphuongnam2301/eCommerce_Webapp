package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String name;

    @Min(value = 0, message = "Price must be greater than or equal to 0!")
    @Max(value = 20000000, message = "Price too high, must be < 20.000.000")
    private Float price;
    private String thumbnail;
    private String description;

    @JsonProperty("category_id")
    private long categoryId;
}
