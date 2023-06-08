package ma.multitenant.config.liquibase;

import com.zaxxer.hikari.HikariDataSource;
import ma.multitenant.config.multitenency.TenantContext;
import org.apache.commons.collections4.MapUtils;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MultiTenantDataSource extends AbstractRoutingDataSource {

    private final Map<Object, Object> resolvedDataSources = new ConcurrentHashMap();
    private final List<DataSource> datasourceList;

    public List<DataSource> getDatasourceList() {
        return datasourceList;
    }

    @Override
    protected String determineCurrentLookupKey() {
        return TenantContext.getCurrentTenant().map(Enum::name).orElse(null);
    }

    public MultiTenantDataSource(final Map<String, String> databases, final String serviceName) {
        if(MapUtils.isEmpty(databases)) {
            throw new IllegalArgumentException("No databases configured");
        }

        databases.forEach((tenant, url) -> {
            resolvedDataSources.put(tenant, createDatasource(serviceName,DataSourceProperties.create(tenant, url.trim())));
        });
        super.setTargetDataSources(resolvedDataSources);
        datasourceList = resolvedDataSources.values().stream().map(dataSource -> (DataSource) dataSource).collect(Collectors.toList());
    }

    private DataSource createDatasource(String serviceName, DataSourceProperties dataSourceProperties) {
        HikariDataSource dataSource = (HikariDataSource) DataSourceBuilder.create()
                .driverClassName(dataSourceProperties.getDriverClassName())
                .url(dataSourceProperties.getUrl())
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword())
                .build();

        dataSource.setSchema(dataSourceProperties.getSchema());
        dataSource.setConnectionTestQuery("SELECT 1");
        dataSource.setIdleTimeout(60000);
        dataSource.setMaximumPoolSize(4);
        dataSource.setMinimumIdle(0);
        dataSource.setPoolName(serviceName);
        dataSource.setValidationTimeout(10000);
        return dataSource;
    }
}
