package com.svincent7.sentra.policy.dto;

import com.svincent7.sentra.common.policy.AuthorizationDecision;
import lombok.Data;

@Data
public class EvaluationResult {
    private AuthorizationDecision decision;
    private String message;
}
