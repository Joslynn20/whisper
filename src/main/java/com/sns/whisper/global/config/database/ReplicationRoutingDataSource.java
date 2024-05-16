package com.sns.whisper.global.config.database;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    private SlaveNames<String> slaveNames;


    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);

        List<String> slaves = targetDataSources.keySet()
                                               .stream()
                                               .map(Object::toString)
                                               .filter(string -> string.contains(
                                                       DataSourceType.SLAVE.getName()))
                                               .collect(toList());

        this.slaveNames = new SlaveNames<>(slaves);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        if (isReadOnly) {
            String slaveName = slaveNames.getNextName();
            log.info("Slave connected: {}", slaveName);
            return slaveName;
        }
        return DataSourceType.MASTER.getName();
    }
}
