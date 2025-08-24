package com.svincent7.sentra.policy.service;

import com.svincent7.sentra.common.audit.EventRequest;
import com.svincent7.sentra.common.policy.AuthorizationDecision;
import com.svincent7.sentra.common.policy.AuthorizationRequest;
import com.svincent7.sentra.common.policy.AuthorizationResponse;
import com.svincent7.sentra.common.policy.context.ContextEnum;
import com.svincent7.sentra.common.srn.SentraResourceNameParser;
import com.svincent7.sentra.policy.audit.AuditClient;
import com.svincent7.sentra.policy.dto.EvaluationResult;
import com.svincent7.sentra.policy.dto.PolicyDocument;
import com.svincent7.sentra.policy.service.evaluation.PolicyEvaluationService;
import com.svincent7.sentra.policy.service.information.PolicyInformationEndpointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {
    private final SentraResourceNameParser sentraResourceNameParser;
    private final AuditClient auditClient;
    private final PolicyInformationEndpointService policyInformationEndpointService;
    private final PolicyEvaluationService policyEvaluationService;

    @Override
    public AuthorizationResponse authorizeRequest(final AuthorizationRequest request) {
        Set<PolicyDocument> policyDocuments = policyInformationEndpointService
                .getPolicyInformation(request.getPrincipalSrn());
        AuthorizationResponse response = new AuthorizationResponse();
        response.setDecision(AuthorizationDecision.DENY); // DENY By DEFAULT
        response.setMessage("No policy explicitly allows this request");

        for (PolicyDocument policy : policyDocuments) {
            EvaluationResult evaluationResult = policyEvaluationService.evaluate(request, policy);
            if (evaluationResult == null || evaluationResult.getDecision() == null) {
                continue;
            }
            response.setDecision(evaluationResult.getDecision());
            response.setMessage(evaluationResult.getMessage());

            if (evaluationResult.getDecision() == AuthorizationDecision.DENY) {
                break;
            }
        }

        auditRecord(request, response);
        return response;
    }

    private void auditRecord(final AuthorizationRequest request, final AuthorizationResponse response) {
        EventRequest eventRequest = new EventRequest();

        Map<String, String> context = request.getContext();

        eventRequest.setAccountId(sentraResourceNameParser.parseAccountId(request.getPrincipalSrn()));
        eventRequest.setEventTime(Long.parseLong(context.get(ContextEnum.REQUEST_TIME.name())));
        eventRequest.setAction(request.getAction());
        eventRequest.setPrincipalSrn(request.getPrincipalSrn());
        eventRequest.setResourceSrn(request.getResourceSrn());
        eventRequest.setContext(request.getContext());
        eventRequest.setSourceIp(context.get(ContextEnum.IP_ADDRESS.name()));
        eventRequest.setUserAgent(context.get(ContextEnum.USER_AGENT.name()));
        eventRequest.setDecision(response.getDecision());
        eventRequest.setMessage(response.getMessage());

        auditClient.record(eventRequest);
    }
}
