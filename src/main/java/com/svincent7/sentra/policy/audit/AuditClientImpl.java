package com.svincent7.sentra.policy.audit;

import com.svincent7.sentra.common.audit.EventRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
@Slf4j
public class AuditClientImpl implements AuditClient {
    private final RestOperations restOperations;
    private final AuditClientConfig auditClientConfig;

    public AuditClientImpl(final RestOperations restOperations, final AuditClientConfig auditClientConfig) {
        this.restOperations = restOperations;
        this.auditClientConfig = auditClientConfig;
    }

    @Override
    public void record(final EventRequest eventRequest) {
        log.debug("Recording data {}", eventRequest);
        try {
            restOperations.postForLocation(auditClientConfig.getAuditUrl(), eventRequest);
        } catch (Throwable throwable) {
            log.error("Failed to record event {}", eventRequest, throwable);
        }
    }
}
