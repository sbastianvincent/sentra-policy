package com.svincent7.sentra.policy.service.evaluation;

import com.svincent7.sentra.common.policy.AuthorizationDecision;
import com.svincent7.sentra.common.policy.AuthorizationRequest;
import com.svincent7.sentra.policy.dto.Condition;
import com.svincent7.sentra.policy.dto.EvaluationResult;
import com.svincent7.sentra.policy.dto.PolicyDocument;
import com.svincent7.sentra.policy.dto.Statement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyEvaluationServiceImpl implements PolicyEvaluationService {

    @Override
    public EvaluationResult evaluate(final AuthorizationRequest request, final PolicyDocument policyDocument) {
        EvaluationResult response = new EvaluationResult();
        for (Statement statement : policyDocument.getStatements()) {
            if (evaluateStatement(statement, request)) {
                if (statement.getEffect().equals(AuthorizationDecision.DENY.name())) {
                    response.setDecision(AuthorizationDecision.DENY);
                    response.setMessage("Explicitly denied by policy SID: " + statement.getSid());
                    return response;
                } else {
                    response.setDecision(AuthorizationDecision.GRANTED);
                }
            }
        }
        return response;
    }

    private boolean evaluateStatement(final Statement statement, final AuthorizationRequest request) {
        return matches(statement.getActions(), request.getAction()) &&
                matches(statement.getResources(), request.getResourceSrn()) &&
                matchesConditions(statement.getConditions(), request.getContext());
    }

    private boolean matches(final List<String> policyActions, final String requestAction) {
        if (policyActions == null || policyActions.isEmpty()) {
            return false;
        }

        for (String policyAction : policyActions) {
            if (policyAction.equals("*") || policyAction.equalsIgnoreCase(requestAction)) {
                return true;
            }

            if (policyAction.contains("*")) {
                String regex = policyAction.replace("*", ".*");
                if (Pattern.matches(regex, requestAction)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchesConditions(final Condition conditions, final Map<String, String> requestContext) {
        if (conditions == null || conditions.getConditionsMap() == null) {
            return true; // No conditions means always match
        }

        for (Map.Entry<String, Map<String, Object>> conditionEntry : conditions.getConditionsMap().entrySet()) {
            String operator = conditionEntry.getKey();
            Map<String, Object> conditionValues = conditionEntry.getValue();

            for (Map.Entry<String, Object> valueEntry : conditionValues.entrySet()) {
                String conditionKey = valueEntry.getKey();
                Object expectedValue = valueEntry.getValue();

                String actualValue = requestContext.get(extractContextKey(conditionKey));

                if (!evaluateCondition(operator, conditionKey, expectedValue, actualValue)) {
                    return false;
                }
            }
        }
        return true;
    }

    private String extractContextKey(final String conditionKey) {
        if (conditionKey.startsWith("context:")) {
            return conditionKey.substring("context:".length());
        }
        return conditionKey;
    }

    private boolean evaluateCondition(final String operator, final String key, final Object expected,
                                      final String actual) {
        if (actual == null) {
            return false;
        }

        try {
            switch (operator) {
                case "StringEquals":
                    return actual.equals(expected.toString());
                case "StringNotEquals":
                    return !actual.equals(expected.toString());
                case "StringEqualsIgnoreCase":
                    return actual.equalsIgnoreCase(expected.toString());
                case "StringLike":
                    return Pattern.matches(expected.toString().replace("*", ".*"), actual);
                case "NumericEquals":
                    return Double.parseDouble(actual) == Double.parseDouble(expected.toString());
                case "NumericGreaterThan":
                    return Double.parseDouble(actual) > Double.parseDouble(expected.toString());
                case "NumericLessThan":
                    return Double.parseDouble(actual) < Double.parseDouble(expected.toString());
                case "Bool":
                    return Boolean.parseBoolean(actual) == Boolean.parseBoolean(expected.toString());
                default:
                    log.warn("Unknown condition operator: {}", operator);
                    return false;
            }
        } catch (NumberFormatException e) {
            log.warn("Failed to parse numeric value for condition {}: {}", key, actual);
            return false;
        }
    }
}
