package com.moneymate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "savings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Saving {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Long amount;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private LocalDate savedDate;

    @PrePersist
    protected void onCreate() {
        if (savedDate == null) {
            savedDate = LocalDate.now();
        }
    }
}
