package com.example.microservice_notify.dto.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceResponse {

    private Integer resourceId;
    private Integer projectId;
    private String type;
    private String location;
    private Double capacity;
    private String status;
}
