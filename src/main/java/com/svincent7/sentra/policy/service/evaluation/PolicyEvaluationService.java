package com.svincent7.sentra.policy.service.evaluation;

import com.svincent7.sentra.common.policy.AuthorizationRequest;
import com.svincent7.sentra.policy.dto.EvaluationResult;
import com.svincent7.sentra.policy.dto.PolicyDocument;

public interface PolicyEvaluationService {
    EvaluationResult evaluate(AuthorizationRequest request, PolicyDocument policyDocument);
}
