package org.tsxuehu.scriptrunner;

import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.records.ContainerId;

public class ContainerLaunchFailAppMaster extends ApplicationMaster {

    private static final Log LOG =
            LogFactory.getLog(ContainerLaunchFailAppMaster.class);

    public ContainerLaunchFailAppMaster() {
        super();
    }

    @Override
    NMCallbackHandler createNMCallbackHandler() {
        return new FailContainerLaunchNMCallbackHandler(this);
    }

    class FailContainerLaunchNMCallbackHandler
            extends ApplicationMaster.NMCallbackHandler {

        public FailContainerLaunchNMCallbackHandler(
                ApplicationMaster applicationMaster) {
            super(applicationMaster);
        }

        @Override
        public void onContainerStarted(ContainerId containerId,
                                       Map<String, ByteBuffer> allServiceResponse) {
            super.onStartContainerError(containerId,
                    new RuntimeException("Inject Container Launch failure"));
        }

    }

    public static void main(String[] args) {
        boolean result = false;
        try {
            ContainerLaunchFailAppMaster appMaster =
                    new ContainerLaunchFailAppMaster();
            LOG.info("Initializing ApplicationMaster");
            boolean doRun = appMaster.init(args);
            if (!doRun) {
                System.exit(0);
            }
            appMaster.run();
            result = appMaster.finish();
        } catch (Throwable t) {
            LOG.fatal("Error running ApplicationMaster", t);
            System.exit(1);
        }
        if (result) {
            LOG.info("Application Master completed successfully. exiting");
            System.exit(0);
        } else {
            LOG.info("Application Master failed. exiting");
            System.exit(2);
        }
    }

}
