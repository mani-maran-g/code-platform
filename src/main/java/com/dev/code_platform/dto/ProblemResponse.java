package com.dev.code_platform.dto;

import com.dev.code_platform.model.TestCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemResponse {
    private String problemId;
    private String title;
    private String description;
    private String difficulty;
    private Integer timeLimitMs;
    private Integer memoryLimitMb;
    private List<TestCase> sampleTestCases;  // Only public test cases
    private Integer totalTestCases;           // Total count (including hidden)
}
