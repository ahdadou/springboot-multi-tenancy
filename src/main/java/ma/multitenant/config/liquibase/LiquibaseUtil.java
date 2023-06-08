package ma.multitenant.config.liquibase;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import lombok.SneakyThrows;
import ma.multitenant.exceptions.TechnicalException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;

import javax.sql.DataSource;
import java.util.Map;

public class LiquibaseUtil {

    public static void bootstrap(DataSource  dataSource) {
        try {
            if(dataSource instanceof HikariDataSource ds){
                LiquibaseUtil.bootstrap(ds);
            } else if(dataSource instanceof MultitenantDataSource multitenantDataSource) {
                for(DataSource ds:multitenantDataSource.getDatasourceList()){
                    bootstrap((HikariDataSource) ds);
                }
            } else {
                throw new TechnicalException("Datasource not supported");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @SneakyThrows
    public static void bootstrap(HikariDataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:db/changelog-master.xml");
        liquibase.setDropFirst(false);
        liquibase.setChangeLogParameters(Map.of("service.name", dataSource.getPoolName()));
        liquibase.setDatabaseChangeLogLockTable(dataSource.getPoolName() + "_changelog_lock");
        liquibase.setDatabaseChangeLogTable(dataSource.getPoolName() + "_changelog");
        liquibase.setResourceLoader(new DefaultResourceLoader());
        liquibase.setDataSource(dataSource);
        if(StringUtils.isNotBlank(dataSource.getSchema())) {
            liquibase.setDefaultSchema(dataSource.getSchema());
            liquibase.setLiquibaseSchema(dataSource.getSchema());
        }
        liquibase.afterPropertiesSet();
    }
}
