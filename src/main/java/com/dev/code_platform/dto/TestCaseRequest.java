package com.dev.code_platform.dto;

import lombok.Data;

@Data
public class TestCaseRequest {
    private String input;
    private String expectedOutput;
    private Integer difficultyLevel;
    private Boolean isSample;
}
