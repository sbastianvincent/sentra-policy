package com.svincent7.sentra.policy.service.information;

import com.svincent7.sentra.common.policy.PolicyInformationResponse;
import com.svincent7.sentra.policy.config.PolicyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyInformationEndpointServiceImpl implements PolicyInformationEndpointService {
    private final RestOperations restOperations;
    private final PolicyConfig config;

    @Override
    public PolicyInformationResponse getPolicyInformation(final String principalSrn) {
        log.debug("Getting policy information for principal {}", principalSrn);

        final ResponseEntity<PolicyInformationResponse> responseEntity = restOperations.getForEntity(
                config.getPolicyInformationUrl() + "/" + principalSrn, PolicyInformationResponse.class);
        return responseEntity.getBody();
    }
}
