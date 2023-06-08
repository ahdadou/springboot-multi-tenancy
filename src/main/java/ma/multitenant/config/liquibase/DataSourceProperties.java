package ma.multitenant.config.liquibase;


import liquibase.util.StringUtil;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import ma.multitenant.exceptions.TechnicalException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Data
@Builder
public class DataSourceProperties {

    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private String schema;
    private String poolName;
    private String connectionTestQuery;
    private String name;
    private int idleTimeout;
    private int maximumPoolSize;
    private int minimumIdle;
    private int validationTimeout;

    public static final String H2 = "h2";
    public static final String PG = "postgresql";
    public static final String MYSQL = "mysql";
    public static final String ORACLE = "oracle";

    public boolean hasSchema() {
        return StringUtils.isNotBlank(schema);
    }

    @SneakyThrows
    public static DataSourceProperties create(final String name, final String datasourceUrl) {

        String databaseUrl = datasourceUrl.trim();
        String provider;

        if (databaseUrl.startsWith("h2://")) {
            provider = H2;
        } else if (databaseUrl.startsWith("mysql://")) {
            provider = MYSQL;
        } else if (databaseUrl.startsWith("oracle://")) {
            provider = ORACLE;
        } else if (databaseUrl.matches("^(pg|postgres(ql)?)://.*")) {
            provider = PG;
        } else {
            throw new TechnicalException("Database protocol not implemented yet: " + databaseUrl);
        }

        URL url = new URL(databaseUrl.replaceAll("^([^:]+)://(.*)$", "http://$2"));
        String userInfos = url.getUserInfo();
        String username = "";
        String password = null;
        String jdbcUrl;
        String jdbcDriver;
        String hostname = url.getHost();
        String path = url.getPath().replaceAll("^/", "");

        MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUriString(url.toString()).build().getQueryParams();
        String schema = StringUtil.trimToEmpty(parameters.getFirst("schema"));

        if (userInfos != null) {
            if (userInfos.contains(":")) {
                String[] userAndPassword = userInfos.trim().split(":");
                password = URLDecoder.decode(userAndPassword[1], StandardCharsets.UTF_8.toString());
                username = URLDecoder.decode(userAndPassword[0], StandardCharsets.UTF_8.toString());
            } else {
                username = userInfos.trim();
            }
        }

        if (H2.equals(provider)) {
            jdbcDriver = "org.h2.Driver";
            jdbcUrl = String.format("jdbc:h2:%1$s:%2$s;MODE=PostgreSQL;DB_CLOSE_ON_EXIT=FALSE;DATABASE_TO_UPPER=false", hostname, path);
            if (StringUtils.isNotBlank(schema)) {
                jdbcUrl += String.format(";INIT=CREATE SCHEMA IF NOT EXISTS %1$s", schema.toUpperCase());
            }
        } else {
            jdbcDriver = "org.postgresql.Driver";
            if (url.getPort() == -1) {
                hostname += ":5432";
            } else {
                hostname += ":" + url.getPort();
            }
            jdbcUrl = String.format("jdbc:postgresql://%1$s/%2$s", hostname, path);
            if (StringUtils.isNotBlank(schema)) {
                schema = schema.toLowerCase();
                jdbcUrl += "?currentSchema=" + schema;
            }
        }
        return DataSourceProperties.builder()
                .name(name)
                .username(username)
                .password(password)
                .schema(schema)
                .url(jdbcUrl)
                .driverClassName(jdbcDriver)
                .build();
    }


}
