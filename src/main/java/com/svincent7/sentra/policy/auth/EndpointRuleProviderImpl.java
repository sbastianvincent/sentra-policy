package com.svincent7.sentra.policy.auth;

import com.svincent7.sentra.common.policy.action.Action;
import com.svincent7.sentra.common.auth.endpoint.EndpointRule;
import com.svincent7.sentra.common.auth.endpoint.EndpointRuleProvider;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class EndpointRuleProviderImpl implements EndpointRuleProvider {

    @Override
    public List<EndpointRule> getEndpointRules() {
        return List.of(
            new EndpointRule(HttpMethod.GET, "/actuator/**",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Action.ACTUATOR_GET_ALL.getPermission()
                    ))
        );
    }

    @Override
    public String[] getPermittedEndpoints() {
        return new String[0];
    }
}
