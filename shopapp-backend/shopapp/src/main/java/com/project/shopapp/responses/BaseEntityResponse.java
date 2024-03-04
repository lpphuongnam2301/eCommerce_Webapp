package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDateTime;
@MappedSuperclass
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntityResponse {
//    @Column(name = "created_at")
    private LocalDateTime createdAt;

//    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
