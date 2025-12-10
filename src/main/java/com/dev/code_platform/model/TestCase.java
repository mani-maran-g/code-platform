package com.dev.code_platform.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;


import java.time.LocalDateTime;

@Entity
@Table(name = "test_cases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCase {

    @Id
    @Column(name = "test_case_id")
    private String testCaseId;

    @Column(name = "problem_id")
    private String problemId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String input;

    @Column(name = "expected_output", nullable = false, columnDefinition = "TEXT")
    private String expectedOutput;

    @Column(name = "difficulty_level")
    private Integer difficultyLevel;

    @Column(name = "is_sample")
    private Boolean isSample;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
