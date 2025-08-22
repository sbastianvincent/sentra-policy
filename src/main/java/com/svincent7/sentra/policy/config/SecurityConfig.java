package com.svincent7.sentra.policy.config;

import com.svincent7.sentra.common.auth.SentraUserDetailsServiceImpl;
import com.svincent7.sentra.common.auth.SpiffeSanPrincipalExtractor;
import com.svincent7.sentra.common.auth.endpoint.EndpointRule;
import com.svincent7.sentra.common.auth.endpoint.EndpointRuleProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestOperations;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final EndpointRuleProvider endpointRuleProvider;
    private final PolicyConfig config;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(endpointRuleProvider.getPermittedEndpoints()).permitAll();
                    for (EndpointRule rule : endpointRuleProvider.getEndpointRules()) {
                        auth.requestMatchers(rule.method(), rule.path()).hasAnyAuthority(
                                rule.authority().toArray(new String[0]));
                    }
                    auth.anyRequest().authenticated();
                })
                .x509(x509 -> x509
                        .x509PrincipalExtractor(new SpiffeSanPrincipalExtractor())
                        .userDetailsService(userDetailsService()));

        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new SentraUserDetailsServiceImpl().getUserDetailsService();
    }

    @Bean
    public RestOperations customRestOperations(final RestTemplateBuilder builder, final SslBundles sslBundles) {
        if (config.isUseClientCertificate()) {
            return builder.sslBundle(sslBundles.getBundle(config.getSslBundleName())).build();
        }
        return builder.build();
    }
}
