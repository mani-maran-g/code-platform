package com.dev.code_platform.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Submission {

    @Id
    @Column(name = "submission_id")
    private String submissionId;

    @Column(name = "problem_id")
    private String problemId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String code;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private String status;

    @Column(name = "runtime_ms")
    private Integer runtimeMs;

    @Column(name = "memory_kb")
    private Integer memoryKb;

    @Column(name = "test_cases_passed")
    private Integer testCasesPassed;

    @Column(name = "total_test_cases")
    private Integer totalTestCases;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "evaluated_at")
    private LocalDateTime evaluatedAt;

    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
        if (status == null) {
            status = "QUEUED";
        }
    }
}
