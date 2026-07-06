package com.smartmerge.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.smartmerge.exception.PullRequestNotFoundException;
import com.smartmerge.model.PullRequest;
import com.smartmerge.repository.PullRequestRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PullRequestService {
    
    private final PullRequestRepository pullRequestRepository;

    public PullRequest getPullRequest(long id) {
        return pullRequestRepository.findById(id)
                .orElseThrow(() -> new PullRequestNotFoundException("No pull request found for id: " + id));
    }

    public List<PullRequest> getPullRequestsByRepoId(long repoId) {
        return pullRequestRepository.findAllByRepoId(repoId);
    }

    public PullRequest savePullRequest(PullRequest pullRequest) {
        return pullRequestRepository.save(pullRequest);
    }
}
