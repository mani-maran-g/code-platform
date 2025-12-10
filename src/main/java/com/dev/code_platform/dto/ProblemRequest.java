package com.dev.code_platform.dto;

import lombok.Data;

@Data
public class ProblemRequest {
    private String title;
    private String description;
    private String difficulty;
    private Integer timeLimitMs;
    private Integer memoryLimitMb;
}