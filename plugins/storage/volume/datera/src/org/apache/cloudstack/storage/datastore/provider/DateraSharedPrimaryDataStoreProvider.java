package org.apache.cloudstack.storage.datastore.provider;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.apache.cloudstack.engine.subsystem.api.storage.DataStoreLifeCycle;
import org.apache.cloudstack.engine.subsystem.api.storage.HypervisorHostListener;
import org.apache.cloudstack.engine.subsystem.api.storage.PrimaryDataStoreDriver;
import org.apache.cloudstack.engine.subsystem.api.storage.PrimaryDataStoreProvider;
import org.apache.cloudstack.storage.datastore.driver.DateraPrimaryDataStoreDriver;
import org.apache.cloudstack.storage.datastore.lifecycle.DateraSharedPrimaryDataStoreLifeCycle;
import org.apache.cloudstack.storage.datastore.utils.DateraUtil;

import com.cloud.utils.component.ComponentContext;

@Component
public class DateraSharedPrimaryDataStoreProvider implements PrimaryDataStoreProvider {
    private static final Logger s_logger = Logger.getLogger(DateraSharedPrimaryDataStoreProvider.class);
    private DataStoreLifeCycle lifecycle;
    private PrimaryDataStoreDriver driver;
    private HypervisorHostListener listener;

    DateraSharedPrimaryDataStoreProvider() {
    }

    @Override
    public String getName() {
        return DateraUtil.SHARED_PROVIDER_NAME;
    }

    @Override
    public DataStoreLifeCycle getDataStoreLifeCycle() {
        return lifecycle;
    }

    @Override
    public PrimaryDataStoreDriver getDataStoreDriver() {
        return driver;
    }

    @Override
    public HypervisorHostListener getHostListener() {
        return listener;
    }

    @Override
    public boolean configure(Map<String, Object> params) {

        s_logger.debug("Datera shared primary datastore provider configure");

        lifecycle = ComponentContext.inject(DateraSharedPrimaryDataStoreLifeCycle.class);
        driver = ComponentContext.inject(DateraPrimaryDataStoreDriver.class);
        //listener = ComponentContext.inject(DateraSharedHostListener.class);
        listener = ComponentContext.inject(new HypervisorHostListener() {
            public boolean hostConnect(long hostId, long poolId) {
                return true;
            }

            public boolean hostDisconnected(long hostId, long poolId) {
                return true;
            }
        });

        return true;
    }

    @Override
    public Set<DataStoreProviderType> getTypes() {
        Set<DataStoreProviderType> types = new HashSet<DataStoreProviderType>();

        types.add(DataStoreProviderType.PRIMARY);

        return types;
    }
}
