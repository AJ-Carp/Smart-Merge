package com.smartmerge.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "pullRequests")
public class PullRequest {

    @Id
    private long id;

    private String title;

    private long repoOwnerId;

    private long authorId;

    private String authorName;

    private long repoId;

    private long installationId;

    @Enumerated(EnumType.STRING)
    private Status status;

    private OffsetDateTime openedAt;

    private OffsetDateTime reviewedAt;

    private OffsetDateTime closedAt;

    private String url;
}