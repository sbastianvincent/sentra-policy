package com.svincent7.sentra.policy.service;

import com.svincent7.sentra.common.policy.AuthorizationRequest;
import com.svincent7.sentra.common.policy.AuthorizationResponse;

public interface PolicyService {
    AuthorizationResponse authorizeRequest(AuthorizationRequest request);
}
