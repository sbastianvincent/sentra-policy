package com.svincent7.sentra.policy.service.evaluation;

import com.svincent7.sentra.common.policy.AuthorizationDecision;
import lombok.Data;

@Data
public class EvaluationResponse {
    private AuthorizationDecision decision;
    private String message;
}
