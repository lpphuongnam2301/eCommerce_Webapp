package com.project.shopapp.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.core.SpringVersion;
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    @NotEmpty(message = "Category's name is required")
    private String name;
}
