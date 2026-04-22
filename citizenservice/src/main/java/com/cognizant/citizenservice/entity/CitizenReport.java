package com.cognizant.citizenservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "citizen_reports")
@Getter
@Setter
@NoArgsConstructor
public class CitizenReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reportid")
    private Integer reportId;
    @Column(name = "citizen_id")
    private Integer citizen;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ReportType type;
    @Column(name = "location")
    private String location;
    @Column(name = "date")
    private LocalDateTime date = LocalDateTime.now();
    @Column(name = "status")
    private String status;
    public enum ReportType {
        POLLUTION, WASTE , ENERGY , WATER;
    }

}
