package com.example.microservice_notify.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer notificationId;

//    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Integer userId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "report_id")
//    private Report report;

    @Column(name = "entity_id")
    private Integer entityId;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "category")
    private String category;

    @Column(name = "status")
    private String status;

    @Column(name = "created_date")
    private LocalDateTime createdDate;


    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

//    public Report getReport() {
//        return report;
//    }

//    public void setReport(Report report) {
//        this.report = report;
//    }

}