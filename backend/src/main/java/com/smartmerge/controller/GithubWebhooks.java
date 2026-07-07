package com.smartmerge.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import static com.smartmerge.SmartMergeConstants.GITHUB_WEB_HOOK_ROUTE;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.smartmerge.handler.BaseEventHandler;
import com.smartmerge.handler.InstallationEventHandler;
import com.smartmerge.handler.PullReqEventHandler;
import com.smartmerge.handler.RepoEventHandler;
import jakarta.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(GITHUB_WEB_HOOK_ROUTE)
public class GithubWebhooks {
    
    @Value("${webhookSecret}")
    String webhookSecret;

    private final InstallationEventHandler installationEventHandler;
    private final RepoEventHandler repoEventHandler;
    private final PullReqEventHandler pullReqEventHandler;
    private final ObjectMapper objectMapper;
    private Map<String, BaseEventHandler> eventHandlerMap;
    
    @PostConstruct
    public void buildEventHandlerMap() {
        eventHandlerMap = Map.of(
            "created", installationEventHandler,
            "deleted", installationEventHandler,
            "added", repoEventHandler,
            "removed", repoEventHandler,
            "opened", pullReqEventHandler,
            "closed", pullReqEventHandler
        );
    }

    @PostMapping
    public void handleEvent(@RequestHeader("X-Hub-Signature-256") String signature, @RequestBody String rawBody) throws Exception {
        // recreate the signature using the same process github did (running HMAC on the payload and the secret key)
        // then compare to githubs signature
        if (!isValidSignature(rawBody, signature)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid webhook signature");
        }
        Map<String, Object> payload = objectMapper.readValue(rawBody, Map.class);
        String action = (String) payload.get("action");
        BaseEventHandler baseEventHandler = eventHandlerMap.getOrDefault(action, null);
        if (baseEventHandler != null) {
            baseEventHandler.triggerEvent(payload, action);
        } else {
            log.info("no handler for action={}", action);
        }
    }

    private boolean isValidSignature(String rawBody, String signatureHeader) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] hash = mac.doFinal(rawBody.getBytes(StandardCharsets.UTF_8));
        String expectedSignature = "sha256=" + HexFormat.of().formatHex(hash);
        // compares every single character even after a differnece is found so attackers cant guess the signature based off timing.
        return MessageDigest.isEqual(
            expectedSignature.getBytes(StandardCharsets.UTF_8),
            signatureHeader.getBytes(StandardCharsets.UTF_8)
        );
    }
}
