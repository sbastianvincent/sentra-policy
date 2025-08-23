package com.svincent7.sentra.policy.service.information;

import com.svincent7.sentra.common.policy.PolicyInformationResponse;

public interface PolicyInformationEndpointService {
    PolicyInformationResponse getPolicyInformation(String principalSrn);
}
