package com.dev.code_platform.repository;

import com.dev.code_platform.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, String> {

    List<TestCase> findByProblemId(String problemId);

    List<TestCase> findByProblemIdAndIsSample(String problemId, Boolean isSample);

    long countByProblemId(String problemId);
}
