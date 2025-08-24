package com.svincent7.sentra.policy.service.information;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svincent7.sentra.common.policy.PolicyResponse;
import com.svincent7.sentra.policy.config.PolicyConfig;
import com.svincent7.sentra.policy.dto.PolicyDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyInformationEndpointServiceImpl implements PolicyInformationEndpointService {
    private final RestOperations restOperations;
    private final PolicyConfig config;
    private final ObjectMapper objectMapper;

    @Override
    public Set<PolicyDocument> getPolicyInformation(final String principalSrn) {
        log.debug("Getting policy information for principal {}", principalSrn);

        final ResponseEntity<Set<PolicyResponse>> responseEntity = restOperations.exchange(
                config.getPolicyInformationUrl() + "/" + principalSrn, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() { });
        Set<PolicyResponse> policyInformationResponse = responseEntity.getBody();
        if (policyInformationResponse == null) {
            return Set.of();
        }
        return convertToPolicyDocuments(policyInformationResponse);
    }

    private Set<PolicyDocument> convertToPolicyDocuments(final Set<PolicyResponse> policyResponses) {
        Set<PolicyDocument> policies = new HashSet<>();

        for (PolicyResponse response : policyResponses) {
            try {
                PolicyDocument policyDoc = objectMapper.readValue(
                        response.getPolicyDocument(),
                        PolicyDocument.class
                );
                policies.add(policyDoc);
            } catch (Exception e) {
                log.error("Failed to parse policy document for policy: {}", response.getId(), e);
            }
        }

        return policies;
    }
}
