package com.cognizant.citizenservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "feedback")
@Getter
@Setter
@NoArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedbackid")
    private Integer feedbackId;
    @Column(name = "citizen_id")
    private Integer citizen;
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;
    @Column(name = "comments")
    private String comments;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "status")
    private String status;

    public enum Category{
        Waste,Energy,Water;
    }
}
