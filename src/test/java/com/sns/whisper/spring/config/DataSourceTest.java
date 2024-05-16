package com.sns.whisper.spring.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class DataSourceTest {

    public final String TEST_METHOD = "determineCurrentLookupKey";

    @Test
    @DisplayName("Master Data Source")
    @Transactional
    void masterDataSourceTest(@Qualifier("routingDataSource") DataSource routingDataSource)
            throws Exception {
        Method determineCurrentLookupKey = AbstractRoutingDataSource.class.getDeclaredMethod(
                TEST_METHOD);
        determineCurrentLookupKey.setAccessible(true);

        String dataSourceType = (String) determineCurrentLookupKey.invoke(routingDataSource);

        assertThat(dataSourceType).isEqualTo("master");
    }

    @Test
    @DisplayName("Slave Data Source")
    @Transactional(readOnly = true)
    void slaveDataSourceTest(@Qualifier("routingDataSource") DataSource routingDataSource)
            throws Exception {
        Method determineCurrentLookupKey = AbstractRoutingDataSource.class.getDeclaredMethod(
                TEST_METHOD);
        determineCurrentLookupKey.setAccessible(true);

        String dataSourceType = (String) determineCurrentLookupKey.invoke(routingDataSource);

        assertThat(dataSourceType).contains("slave");
    }
}
