package com.example.microservice_notify.dto.external;

import lombok.Data;

@Data
public class ResourceResponse {
    private Long resourceId;
    private String type;
}
