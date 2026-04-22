package com.example.microservice_notify.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "compliance_records")
@Getter
@Setter
@NoArgsConstructor
public class ComplianceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compliance_id")
    private Integer complianceId;

    @Column(name = "entity_id", nullable = false)
    private Integer entityId;

    @Column(name = "entity_type", nullable = false)
    private String entityType; // "Project" or "Resource"

    @Column(name = "result")
    private String result; // e.g., "Passed", "Failed", "Warning"

    @Column(name = "date")
    private LocalDateTime date;


    @Column(name = "notes")
    private String notes;

    /**
     * One ComplianceRecord can be referenced by many Audit rows.
     * Ref: audits.compliance_id -> compliance_records.compliance_id
     */
    @OneToMany(mappedBy = "complianceRecord")
    private List<Audit> audits;

}