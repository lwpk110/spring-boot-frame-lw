package cn.tendata.mdcs.web.config;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Profile("test")
@Configuration
public class DbUnitTestConfig {

    @Bean
    public HsqldbDataTypeFactory dbUnitDataTypeFactory() {
        return new HsqldbDataTypeFactory();
    }

    @Bean
    public DatabaseConfigBean dbUnitDatabaseConfig() {
        DatabaseConfigBean databaseConfigBean = new DatabaseConfigBean();
        databaseConfigBean.setSkipOracleRecyclebinTables(true);
        databaseConfigBean.setDatatypeFactory(dbUnitDataTypeFactory());
        return databaseConfigBean;
    }

    @Bean
    public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection(DataSource dataSource) {
        DatabaseDataSourceConnectionFactoryBean factoryBean = new DatabaseDataSourceConnectionFactoryBean();
        factoryBean.setDatabaseConfig(dbUnitDatabaseConfig());
        factoryBean.setDataSource(dataSource);
        return factoryBean;
    }
}
