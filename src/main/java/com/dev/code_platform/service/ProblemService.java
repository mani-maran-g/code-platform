package com.dev.code_platform.service;

import com.dev.code_platform.datastructures.TestCaseTree;
import com.dev.code_platform.dto.ProblemRequest;
import com.dev.code_platform.dto.ProblemResponse;
import com.dev.code_platform.dto.TestCaseRequest;
import com.dev.code_platform.model.Problem;
import com.dev.code_platform.model.TestCase;
import com.dev.code_platform.repository.ProblemRepository;
import com.dev.code_platform.repository.TestCaseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;

    // In-memory cache: problemId -> Problem
    private final ConcurrentHashMap<String, Problem> problemCache = new ConcurrentHashMap<>();

    // In-memory BST: problemId -> TestCaseTree
    private final ConcurrentHashMap<String, TestCaseTree> testCaseTrees = new ConcurrentHashMap<>();

    /**
     * Load all problems and build test case trees on application startup
     */
    @PostConstruct
    public void initialize() {
        log.info("Loading problems and building test case trees...");

        List<Problem> allProblems = problemRepository.findAll();
        log.info("Found {} problems in database", allProblems.size());

        for (Problem problem : allProblems) {
            // Cache problem
            problemCache.put(problem.getProblemId(), problem);

            // Build test case tree
            TestCaseTree tree = new TestCaseTree();
            List<TestCase> testCases = testCaseRepository.findByProblemId(problem.getProblemId());

            for (TestCase testCase : testCases) {
                tree.insert(testCase);
            }

            testCaseTrees.put(problem.getProblemId(), tree);

            log.info("Loaded problem '{}' with {} test cases",
                    problem.getTitle(), tree.getCount());
        }

        log.info("Initialization complete. {} problems cached.", problemCache.size());
    }

    /**
     * Create a new problem
     */
    @Transactional
    public Problem createProblem(ProblemRequest request) {
        // Generate unique problem ID
        String problemId = generateProblemId(request.getTitle());

        // Check if already exists
        if (problemRepository.existsByProblemId(problemId)) {
            throw new RuntimeException("Problem with ID " + problemId + " already exists");
        }

        // Create problem entity
        Problem problem = new Problem();
        problem.setProblemId(problemId);
        problem.setTitle(request.getTitle());
        problem.setDescription(request.getDescription());
        problem.setDifficulty(request.getDifficulty());
        problem.setTimeLimitMs(request.getTimeLimitMs() != null ? request.getTimeLimitMs() : 2000);
        problem.setMemoryLimitMb(request.getMemoryLimitMb() != null ? request.getMemoryLimitMb() : 256);

        // Save to database
        Problem savedProblem = problemRepository.save(problem);

        // Add to cache
        problemCache.put(problemId, savedProblem);

        // Initialize empty test case tree
        testCaseTrees.put(problemId, new TestCaseTree());

        log.info("Created problem: {} (ID: {})", savedProblem.getTitle(), problemId);

        return savedProblem;
    }

    /**
     * Get a problem by ID with sample test cases
     */
    public ProblemResponse getProblem(String problemId) {
        // Check cache first
        Problem problem = problemCache.get(problemId);

        if (problem == null) {
            // Not in cache, load from database
            problem = problemRepository.findById(problemId)
                    .orElseThrow(() -> new RuntimeException("Problem not found: " + problemId));

            // Add to cache
            problemCache.put(problemId, problem);
        }

        // Get ONLY sample test cases (isSample = true)
        List<TestCase> sampleTestCases = testCaseRepository
                .findByProblemIdAndIsSample(problemId, true);

        // Get total count (including hidden)
        TestCaseTree tree = testCaseTrees.get(problemId);
        int totalCount = tree != null ? tree.getCount() : 0;

        // Build response
        ProblemResponse response = new ProblemResponse();
        response.setProblemId(problem.getProblemId());
        response.setTitle(problem.getTitle());
        response.setDescription(problem.getDescription());
        response.setDifficulty(problem.getDifficulty());
        response.setTimeLimitMs(problem.getTimeLimitMs());
        response.setMemoryLimitMb(problem.getMemoryLimitMb());
        response.setSampleTestCases(sampleTestCases);
        response.setTotalTestCases(totalCount);

        return response;
    }

    /**
     * Get all problems (without test cases)
     */
    public List<Problem> getAllProblems() {
        // Return from cache if available
        if (!problemCache.isEmpty()) {
            return problemCache.values().stream().collect(Collectors.toList());
        }

        // Otherwise load from database
        return problemRepository.findAll();
    }

    /**
     * Add a test case to a problem
     */
    @Transactional
    public TestCase addTestCase(String problemId, TestCaseRequest request) {
        // Verify problem exists
        if (!problemCache.containsKey(problemId) && !problemRepository.existsByProblemId(problemId)) {
            throw new RuntimeException("Problem not found: " + problemId);
        }

        // Generate test case ID
        String testCaseId = UUID.randomUUID().toString();

        // Create test case entity
        TestCase testCase = new TestCase();
        testCase.setTestCaseId(testCaseId);
        testCase.setProblemId(problemId);
        testCase.setInput(request.getInput());
        testCase.setExpectedOutput(request.getExpectedOutput());
        testCase.setDifficultyLevel(request.getDifficultyLevel());
        testCase.setIsSample(request.getIsSample() != null ? request.getIsSample() : false);

        // Save to database
        TestCase savedTestCase = testCaseRepository.save(testCase);

        // Add to BST
        TestCaseTree tree = testCaseTrees.computeIfAbsent(problemId, k -> new TestCaseTree());
        tree.insert(savedTestCase);

        log.info("Added test case to problem {} (difficulty: {}, sample: {})",
                problemId, request.getDifficultyLevel(), request.getIsSample());

        return savedTestCase;
    }

    /**
     * Get all test cases for a problem in sorted order (from BST)
     * Used internally by ExecutionService
     */
    public List<TestCase> getTestCasesInOrder(String problemId) {
        TestCaseTree tree = testCaseTrees.get(problemId);

        if (tree == null || tree.isEmpty()) {
            log.warn("No test cases found for problem: {}", problemId);
            return List.of();
        }

        return tree.inOrderTraversal();  // Returns sorted list (easy → hard)
    }

    /**
     * Get test case tree for a problem
     */
    public TestCaseTree getTestCaseTree(String problemId) {
        return testCaseTrees.get(problemId);
    }

    /**
     * Generate a URL-friendly problem ID from title
     */
    private String generateProblemId(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")  // Remove special characters
                .replaceAll("\\s+", "-")          // Replace spaces with hyphens
                .trim();
    }
}