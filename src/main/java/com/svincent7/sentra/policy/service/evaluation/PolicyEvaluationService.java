package com.svincent7.sentra.policy.service.evaluation;

import com.svincent7.sentra.common.policy.AuthorizationRequest;
import com.svincent7.sentra.common.policy.PolicyResponse;

public interface PolicyEvaluationService {
    EvaluationResponse evaluate(AuthorizationRequest request, PolicyResponse policyResponse);
}
