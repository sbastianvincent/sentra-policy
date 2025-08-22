package com.svincent7.sentra.policy.service;

import com.svincent7.sentra.common.audit.EventRequest;
import com.svincent7.sentra.common.policy.AuthorizationDecision;
import com.svincent7.sentra.common.policy.AuthorizationRequest;
import com.svincent7.sentra.common.policy.AuthorizationResponse;
import com.svincent7.sentra.common.policy.context.ContextEnum;
import com.svincent7.sentra.common.srn.SentraResourceNameParser;
import com.svincent7.sentra.policy.audit.AuditClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {
    private final SentraResourceNameParser sentraResourceNameParser;
    private final AuditClient auditClient;

    @Override
    public AuthorizationResponse authorizeRequest(final AuthorizationRequest request) {
        AuthorizationResponse response = new AuthorizationResponse();
        response.setDecision(AuthorizationDecision.GRANTED);
        response.setMessage("GRANTED All Requests for now");
        auditRecord(request, response);
        return response;
    }

    private void auditRecord(final AuthorizationRequest request, final AuthorizationResponse response) {
        EventRequest eventRequest = new EventRequest();

        Map<String, Object> context = request.getContext();

        eventRequest.setAccountId(sentraResourceNameParser.parseAccountId(request.getPrincipalSrn()));
        eventRequest.setEventTime((Long) context.get(ContextEnum.REQUEST_TIME.name()));
        eventRequest.setAction(request.getAction());
        eventRequest.setPrincipalSrn(request.getPrincipalSrn());
        eventRequest.setResourceSrn(request.getResourceSrn());
        eventRequest.setContext(request.getContext());
        eventRequest.setSourceIp(context.get(ContextEnum.IP_ADDRESS.name()).toString());
        eventRequest.setUserAgent(context.get(ContextEnum.USER_AGENT.name()).toString());
        eventRequest.setDecision(response.getDecision());
        eventRequest.setMessage(response.getMessage());

        auditClient.record(eventRequest);
    }
}
