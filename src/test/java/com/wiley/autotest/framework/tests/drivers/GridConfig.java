package com.wiley.autotest.framework.tests.drivers;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.services.Configuration;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.utils.SelfRegisteringRemote;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;
import org.openqa.grid.web.Hub;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * User: ntyukavkin
 * Date: 31.05.2017
 * Time: 11:50
 */
public class GridConfig extends BaseTest {

    //Start grid hub and node for test
    @Autowired
    private void setConfiguration(Configuration configuration) throws Exception {
        GridHubConfiguration config = GridHubConfiguration.loadFromJSON(getClass().getResource("/grid/hubConfig.json").getPath());

        Hub hub = new Hub(config);
        hub.getConfiguration();
        hub.start();

        RegistrationRequest req = new RegistrationRequest(GridNodeConfiguration.loadFromJSON(getClass().getResource("/grid/node.json").getPath()));

        SelfRegisteringRemote remote = new SelfRegisteringRemote(req);
        remote.startRegistrationProcess();
    }

    @Test
    public void grid_config_test() throws Exception {
        openPage("main.html");

        System.out.println("");
    }
}
