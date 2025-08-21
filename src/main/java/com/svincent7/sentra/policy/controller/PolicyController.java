package com.svincent7.sentra.policy.controller;

import com.svincent7.sentra.common.policy.AuthorizationRequest;
import com.svincent7.sentra.common.policy.AuthorizationResponse;
import com.svincent7.sentra.policy.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/policy/v1/authorize")
public class PolicyController {
    private final PolicyService policyService;

    @PostMapping
    public ResponseEntity<AuthorizationResponse> authorize(final @RequestBody AuthorizationRequest request) {
        AuthorizationResponse response = policyService.authorizeRequest(request);
        return ResponseEntity.ok(response);
    }
}
