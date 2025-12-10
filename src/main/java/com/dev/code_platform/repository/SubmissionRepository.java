package com.dev.code_platform.repository;

import com.dev.code_platform.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, String> {

    List<Submission> findByProblemId(String problemId);

    List<Submission> findByProblemIdAndStatus(String problemId, String status);

    List<Submission> findByProblemIdAndStatusOrderByRuntimeMsAsc(String problemId, String status);
}
