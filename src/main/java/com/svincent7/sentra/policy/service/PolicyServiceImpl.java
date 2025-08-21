package com.svincent7.sentra.policy.service;

import com.svincent7.sentra.common.audit.EventRequest;
import com.svincent7.sentra.common.policy.AuthorizationDecision;
import com.svincent7.sentra.common.policy.AuthorizationRequest;
import com.svincent7.sentra.common.policy.AuthorizationResponse;
import com.svincent7.sentra.common.srn.SentraResourceNameParser;
import com.svincent7.sentra.policy.audit.AuditClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {
    private final SentraResourceNameParser sentraResourceNameParser;
    private final AuditClient auditClient;

    @Override
    public AuthorizationResponse authorizeRequest(final AuthorizationRequest request) {
        AuthorizationResponse response = new AuthorizationResponse();
        response.setDecision(AuthorizationDecision.ALLOW);
        response.setMessage("Allowing All Requests for now");
        auditRecord(request, response);
        return response;
    }

    private void auditRecord(final AuthorizationRequest request, final AuthorizationResponse response) {
        EventRequest eventRequest = new EventRequest();

        eventRequest.setAccountId(sentraResourceNameParser.parseAccountId(request.getPrincipalSrn()));
        eventRequest.setEventTime(System.currentTimeMillis());
        eventRequest.setAction(request.getAction());
        eventRequest.setPrincipalSrn(request.getPrincipalSrn());
        eventRequest.setResourceSrn(request.getResourceSrn());
        eventRequest.setContext(request.getContext());
        eventRequest.setSourceIp(request.getSourceIp());
        eventRequest.setUserAgent(request.getUserAgent());
        eventRequest.setDecision(response.getDecision());
        eventRequest.setMessage(response.getMessage());

        auditClient.record(eventRequest);
    }
}
