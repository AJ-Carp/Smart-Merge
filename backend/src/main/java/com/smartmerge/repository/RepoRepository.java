package com.smartmerge.repository;

import com.smartmerge.model.Repo;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoRepository extends JpaRepository<Repo, Long> {

    @Transactional
    void deleteAllByInstallationId(long id);

    List<Repo> findAllByUserId(long userId);
}
