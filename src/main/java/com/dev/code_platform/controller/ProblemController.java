package com.dev.code_platform.controller;

import com.dev.code_platform.dto.ProblemRequest;
import com.dev.code_platform.dto.ProblemResponse;
import com.dev.code_platform.dto.TestCaseRequest;
import com.dev.code_platform.model.Problem;
import com.dev.code_platform.model.TestCase;
import com.dev.code_platform.service.ProblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
@Slf4j
public class ProblemController {

    private final ProblemService problemService;

    /**
     * Create a new problem
     * POST /api/problems
     */
    @PostMapping
    public ResponseEntity<Problem> createProblem(@RequestBody ProblemRequest request) {
        log.info("Creating problem: {}", request.getTitle());

        Problem problem = problemService.createProblem(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(problem);
    }

    /**
     * Get all problems
     * GET /api/problems
     */
    @GetMapping
    public ResponseEntity<List<Problem>> getAllProblems() {
        log.info("Fetching all problems");

        List<Problem> problems = problemService.getAllProblems();

        return ResponseEntity.ok(problems);
    }

    /**
     * Get a specific problem with sample test cases
     * GET /api/problems/{problemId}
     */
    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemResponse> getProblem(@PathVariable String problemId) {
        log.info("Fetching problem: {}", problemId);

        ProblemResponse response = problemService.getProblem(problemId);

        return ResponseEntity.ok(response);
    }

    /**
     * Add a test case to a problem
     * POST /api/problems/{problemId}/testcases
     */
    @PostMapping("/{problemId}/testcases")
    public ResponseEntity<TestCase> addTestCase(
            @PathVariable String problemId,
            @RequestBody TestCaseRequest request) {

        log.info("Adding test case to problem: {} (difficulty: {})",
                problemId, request.getDifficultyLevel());

        TestCase testCase = problemService.addTestCase(problemId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(testCase);
    }
}
