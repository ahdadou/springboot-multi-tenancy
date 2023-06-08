package ma.multitenant.config.liquibase;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class PersistanceConfig {

    @Autowired
    private PersistenceProps persistenceProps;

    @Bean
    @ConditionalOnExpression("${application.persistance.multi-tenancy:false}")
    public DataSource createMultiTenantDatasource(@Value("${application-name}") String serviceName) {
        return this.bootstrapDatasource(new MultitenantDataSource(persistenceProps.getTenants(),serviceName));
    }


    @Bean
    @ConditionalOnExpression("!${application.persistance.multi-tenancy:false}")
    public DataSource createSimpleDataSource(@Value("${application-name}") String serviceName,@Value("${application-database-url}") String databaseUrl) {

        DataSourceProperties dataSourceProperties = DataSourceProperties.create(serviceName,databaseUrl);
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
        return bootstrapDatasource(dataSource);
    }


    private DataSource bootstrapDatasource(DataSource dataSource) {
        LiquibaseUtil.bootstrap(dataSource);
        return dataSource;
    }

}
