package com.smartmerge.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.smartmerge.model.Repo;

// used by InstallationEventHandler and RepoEventHandler
@Component
public class RepoMapper {

    public List<Repo> createRepos(List<Map<String, Object>> repoData, Map<String, Object> installationData, Map<String, Object> accountData) {
        List<Repo> repos = new ArrayList<>();

        for (Map<String, Object> repo : repoData) {
            repos.add(Repo.builder()
                .repoId(((Number) repo.get("id")).longValue())
                .userId(((Number) accountData.get("id")).longValue())
                .installationId(((Number) installationData.get("id")).longValue())
                .repoName((String) repo.get("full_name"))
                .isPrivate((boolean) repo.get("private"))
                .build()
            );
        }
        return repos;
    }
}
