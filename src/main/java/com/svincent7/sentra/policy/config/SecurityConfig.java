package com.svincent7.sentra.policy.config;

import com.svincent7.sentra.common.auth.SpiffeSanPrincipalExtractor;
import com.svincent7.sentra.common.policy.action.Action;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
        UserDetails monitoring = User.builder()
                .username("ns/homelab/sa/monitoring")
                .password("noop-password")
                .authorities(
                        EndpointRuleProvider.SCOPE_PREFIX + Action.ACTUATOR_GET_ALL.getPermission()
                )
                .build();
        UserDetails kms = User.builder()
                .username("ns/homelab/sa/kms")
                .password("noop-password")
                .build();
        UserDetails vault = User.builder()
                .username("ns/homelab/sa/vault")
                .password("noop-password")
                .build();
        UserDetails audit = User.builder()
                .username("ns/homelab/sa/audit")
                .password("noop-password")
                .build();
        UserDetails policy = User.builder()
                .username("ns/homelab/sa/policy")
                .password("noop-password")
                .build();
        UserDetails iam = User.builder()
                .username("ns/homelab/sa/iam")
                .password("noop-password")
                .build();

        return new InMemoryUserDetailsManager(monitoring, kms, vault, audit, policy, iam);
    }

    @Bean
    public RestOperations customRestOperations(final RestTemplateBuilder builder, final SslBundles sslBundles) {
        if (config.isUseClientCertificate()) {
            return builder.sslBundle(sslBundles.getBundle(config.getSslBundleName())).build();
        }
        return builder.build();
    }
}
