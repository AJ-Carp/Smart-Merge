package com.smartmerge.repository;

import com.smartmerge.model.Repo;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoRepository extends JpaRepository<Repo, Integer> {

    @Transactional
    void deleteAllByInstallationId(int id);

    Optional<List<Repo>> findAllByUserId(int userId);
}
