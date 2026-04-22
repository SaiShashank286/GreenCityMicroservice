package com.example.microservice_notify.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "impacts")
@Getter
@Setter
@NoArgsConstructor
public class Impact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "impact_id")
    private Integer impactId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "metrics_json", columnDefinition = "TEXT")
    private String metricsJson;

    @Column(name = "status")
    private String status;

}

