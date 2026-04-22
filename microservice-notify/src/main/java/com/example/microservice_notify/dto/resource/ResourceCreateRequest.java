package com.example.microservice_notify.dto.resource;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceCreateRequest {

    @NotNull(message = "Project ID is needed")
    private Integer projectId;

    @NotBlank(message = "Type is needed")
    @Size(max = 50)
    private String type;

    @NotBlank(message = "Location is needed")
    @Size(max = 255)
    private String location;

    @NotNull(message = "Capacity is needed")
    @PositiveOrZero(message = "Capacity cannot be negative")
    private Double capacity;

    @NotBlank(message = "Status is needed")
    @Size(max = 50)
    private String status;
}