package com.example.microservice_notify.dto.resource;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceUpdateRequest {

    @Size(max = 50)
    private String type;

    @Size(max = 255)
    private String location;

    @Min(0)
    private Double capacity;

    @Size(max = 50)
    private String status;
}
