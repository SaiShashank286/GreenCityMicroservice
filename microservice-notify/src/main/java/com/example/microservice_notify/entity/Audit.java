package com.example.microservice_notify.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "audits")
@Getter
@Setter
@NoArgsConstructor

public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Integer auditId;
   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "officer_id", nullable = false)
    private User officer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compliance_id", nullable = false)
    private ComplianceRecord complianceRecord;

    @Column(name = "scope")
    private String scope; // e.g., "Resource", "Project", "Follow-up"

    @Column(name = "findings", columnDefinition = "TINYTEXT")
    private String findings;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "status")
    private String status; // e.g., "Open", "In Review", "Closed", "Approved"

}