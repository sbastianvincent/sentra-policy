package com.svincent7.sentra.policy.service.evaluation;

import com.svincent7.sentra.common.policy.AuthorizationDecision;
import com.svincent7.sentra.common.policy.AuthorizationRequest;
import com.svincent7.sentra.common.policy.PolicyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyEvaluationServiceImpl implements PolicyEvaluationService {

    @Override
    public EvaluationResponse evaluate(final AuthorizationRequest request, final PolicyResponse policyResponse) {
        EvaluationResponse response = new EvaluationResponse();
        response.setDecision(AuthorizationDecision.GRANTED);
        response.setMessage("All Policy granted for now");
        return response;
    }
}
