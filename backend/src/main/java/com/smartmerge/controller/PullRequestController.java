package com.smartmerge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.smartmerge.model.PullRequest;
import com.smartmerge.service.PullRequestService;
import lombok.RequiredArgsConstructor;
import static com.smartmerge.SmartMergeConstants.PULL_REQUEST_ROUTE;
import java.util.List;

@RestController
@RequestMapping(PULL_REQUEST_ROUTE)
@RequiredArgsConstructor
public class PullRequestController {
    
    private final PullRequestService pullRequestService;

    @GetMapping("/repository/{repoId}")
    public ResponseEntity<List<PullRequest>> getPullRequestsByRepoId(@PathVariable long repoId) {
        return ResponseEntity.ok(pullRequestService.getPullRequestsByRepoId(repoId));
    }
}
