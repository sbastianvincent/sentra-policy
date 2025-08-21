package com.svincent7.sentra.policy.config;

import com.svincent7.sentra.common.auth.AuthService;
import com.svincent7.sentra.common.auth.AuthServiceImpl;
import com.svincent7.sentra.common.config.ConfigProperties;
import com.svincent7.sentra.common.srn.SentraResourceNameImpl;
import com.svincent7.sentra.common.srn.SentraResourceNameParser;
import com.svincent7.sentra.policy.audit.AuditClientConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.svincent7.sentra.policy.config")
@Data
public class PolicyConfig implements ConfigProperties, AuditClientConfig {
    private boolean shouldRunInitializer;
    private boolean useClientCertificate;
    private String sslBundleName;

    private String auditUrl;

    @Bean
    public SentraResourceNameParser sentraResourceNameParser() {
        return new SentraResourceNameImpl();
    }

    @Bean
    public AuthService authService() {
        return new AuthServiceImpl();
    }
}
