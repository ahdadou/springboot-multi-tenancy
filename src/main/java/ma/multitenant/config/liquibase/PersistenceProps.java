package ma.multitenant.config.liquibase;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix="application.persistence")
@Configuration
public class PersistenceProps {

    private Map<String, String> databaseUrls = new HashMap<>();
    private String databaseUrl;

    public Map<String, String> getDatabaseUrls() {
        return databaseUrls;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }
}
