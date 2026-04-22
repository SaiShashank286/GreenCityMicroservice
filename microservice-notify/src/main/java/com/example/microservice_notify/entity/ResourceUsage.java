package com.example.microservice_notify.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "resource_usage")
@Getter
@Setter
@NoArgsConstructor
public class ResourceUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usageid", updatable = false, nullable = false)
    private Integer usageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resourceid", nullable = false)
    private Resource resource;

    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

}
