package com.smartmerge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.smartmerge.model.Account;
import com.smartmerge.model.Profile;
import com.smartmerge.service.ProfileService;
import lombok.RequiredArgsConstructor;
import static com.smartmerge.SmartMergeConstants.PROFILE_ROUTE;

@RequiredArgsConstructor
@RequestMapping(PROFILE_ROUTE)
@RestController
public class ProfileController {

    private final ProfileService profileService;
    
    @GetMapping
    public ResponseEntity<Profile> getProfile(@AuthenticationPrincipal Account account) {
        return ResponseEntity.ok(profileService.getProfileByUserId(account.getUserId()));
    }
}
