package com.svincent7.sentra.policy.config;

import com.svincent7.sentra.common.config.ConfigProperties;
import com.svincent7.sentra.common.srn.SentraResourceNameParser;
import com.svincent7.sentra.common.srn.SentraResourceNameParserImpl;
import com.svincent7.sentra.policy.audit.AuditClientConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
@ConfigurationProperties(prefix = "com.svincent7.sentra.policy.config")
@Data
public class PolicyConfig implements ConfigProperties, AuditClientConfig {
    private boolean shouldRunInitializer;
    private boolean useClientCertificate;
    private String sslBundleName;

    private String auditUrl;

    private String stsAssumeRoleUrl;
    private int defaultStsDurationMinutes;

    @Bean
    public SentraResourceNameParser sentraResourceNameParser() {
        return new SentraResourceNameParserImpl();
    }

    @Bean
    public RestOperations customRestOperations(final RestTemplateBuilder builder, final SslBundles sslBundles) {
        if (isUseClientCertificate()) {
            return builder.sslBundle(sslBundles.getBundle(getSslBundleName())).build();
        }
        return builder.build();
    }
}
