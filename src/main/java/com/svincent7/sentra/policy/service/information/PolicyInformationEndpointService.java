package com.svincent7.sentra.policy.service.information;

import com.svincent7.sentra.policy.dto.PolicyDocument;

import java.util.Set;

public interface PolicyInformationEndpointService {
    Set<PolicyDocument> getPolicyInformation(String principalSrn);
}
