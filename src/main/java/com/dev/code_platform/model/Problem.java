package com.dev.code_platform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "problems")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Problem {

    @Id
    @Column(name = "problem_id")
    private String problemId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String difficulty;

    @Column(name = "time_limit_ms")
    private Integer timeLimitMs;

    @Column(name = "memory_limit_mb")
    private Integer memoryLimitMb;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}