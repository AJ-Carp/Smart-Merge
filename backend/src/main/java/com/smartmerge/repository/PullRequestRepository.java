package com.smartmerge.repository;

import com.smartmerge.model.PullRequest;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PullRequestRepository extends JpaRepository<PullRequest, Long> {

    List<PullRequest> findAllByRepoId(long repoId);

    @Transactional
    void deleteAllByInstallationId(long id);

    void deleteAllByRepoIdIn(List<Long> repoIds);
}
