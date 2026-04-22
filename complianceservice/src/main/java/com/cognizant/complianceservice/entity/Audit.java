package com.cognizant.complianceservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    @JoinColumn(name = "officer_id", nullable = false)
    private Integer officer;

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