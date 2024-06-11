package com.sns.whisper.global.config.database;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Profile("!test")
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class DataSourceConfiguration {

    private final ReplicationDataSourceProperties dataSourceProperties;
    private final JpaProperties jpaProperties;


    @Bean
    public DataSource routingDataSource() {

        DataSource masterDataSource = createDataSource(
                dataSourceProperties.getDriverClassName(),
                dataSourceProperties.getUrl(),
                dataSourceProperties.getUsername(),
                dataSourceProperties.getPassword()
        );

        ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DataSourceType.MASTER.getName(), masterDataSource);

        dataSourceProperties.getSlaves()
                            .forEach((key, slave) ->
                                    dataSourceMap.put(slave.getName(), createDataSource(
                                            slave.getDriverClassName(), slave.getUrl(),
                                            slave.getUsername(),
                                            slave.getPassword())));

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    private DataSource createDataSource(String driverClassName, String url, String userName,
            String password) {
        return DataSourceBuilder.create()
                                .type(HikariDataSource.class)
                                .driverClassName(driverClassName)
                                .url(url)
                                .username(userName)
                                .password(password)
                                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        EntityManagerFactoryBuilder entityManagerFactoryBuilder = new EntityManagerFactoryBuilder(
                jpaVendorAdapter, jpaProperties.getProperties(), null);

        return entityManagerFactoryBuilder.dataSource(lazyRoutingDataSource())
                                          .packages("com.sns.whisper")
                                          .build();
    }

    //     실제 쿼리 실행 시 실제 사용할 DataSource의 Connection 획득
    @Bean
    @Primary
    public DataSource lazyRoutingDataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            @Qualifier(value = "lazyRoutingDataSource") DataSource lazyRoutingDataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(lazyRoutingDataSource);
        return new DataSourceTransactionManager(lazyRoutingDataSource);
    }

}
