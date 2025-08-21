package com.svincent7.sentra.policy.audit;

import com.svincent7.sentra.common.audit.EventRequest;
import org.springframework.web.servlet.HandlerInterceptor;

public interface AuditClient extends HandlerInterceptor {
    void record(EventRequest eventRequest);
}
