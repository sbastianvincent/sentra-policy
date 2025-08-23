package com.svincent7.sentra.policy.config;

import com.svincent7.sentra.common.auth.SentraSecurityFilterChain;
import com.svincent7.sentra.common.auth.SentraSecurityFilterChainImpl;
import com.svincent7.sentra.common.auth.endpoint.EndpointRuleProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final EndpointRuleProvider endpointRuleProvider;

    @Bean
    public SecurityFilterChain certificateSecurityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        return sentraSecurityFilterChain().getCertificateSecurityFilterChain(httpSecurity);
    }

    @Bean
    public SentraSecurityFilterChain sentraSecurityFilterChain() {
        return new SentraSecurityFilterChainImpl(null,
                endpointRuleProvider, null, null);
    }
}
