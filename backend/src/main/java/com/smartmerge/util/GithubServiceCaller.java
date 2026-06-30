package com.smartmerge.util;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import static com.smartmerge.SmartMergeConstants.GITHUB_REQUEST_BODY_TYPE;

@Service
public class GithubServiceCaller {

    private final RestClient restClient;

    public GithubServiceCaller() {
        restClient = RestClient.builder()
            .defaultHeader("User-Agent", "SmartMerge")
            .defaultHeader("Accept", GITHUB_REQUEST_BODY_TYPE)
            .build();
    }

    public <T> T get(String uri, String accessToken, ParameterizedTypeReference<T> responseType) {
        return restClient
            .get()
            .uri(java.net.URI.create(uri))
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .body(responseType);
    }

    public <T> T post(String uri, String accessToken, Object body, ParameterizedTypeReference<T> responseType) {
        RestClient.RequestBodySpec request = restClient
            .post()
            .uri(java.net.URI.create(uri))
            .header("Authorization", "Bearer " + accessToken);
        if (body != null) {
            request.body(body);
        }
        if (responseType == null) {
            request.retrieve().toBodilessEntity();
            return null;
        }
        return request.retrieve().body(responseType);
    }
}