package ma.multitenant.config.liquibase;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix="datasource")
@Configuration
public class PersistenceProps {

    private Map<String, String> tenants = new HashMap<>();
    private String url;

    public Map<String, String> getTenants() {
        return tenants;
    }

    public String getDatabaseUrl() {
        return url;
    }
}
