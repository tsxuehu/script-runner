package org.tsxuehu.scriptrunner;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.exceptions.YarnException;

public class TestDSFailedAppMaster extends ApplicationMaster {

    private static final Log LOG = LogFactory.getLog(TestDSFailedAppMaster.class);

    @Override
    public void run() throws YarnException, IOException {
        super.run();

        // for the 2nd attempt.
        if (appAttemptID.getAttemptId() == 2) {
            // should reuse the earlier running container, so numAllocatedContainers
            // should be set to 1. And should ask no more containers, so
            // numRequestedContainers should be the same as numTotalContainers.
            // The only container is the container requested by the AM in the first
            // attempt.
            if (numAllocatedContainers.get() != 1
                    || numRequestedContainers.get() != numTotalContainers) {
                LOG.info("NumAllocatedContainers is " + numAllocatedContainers.get()
                        + " and NumRequestedContainers is " + numAllocatedContainers.get()
                        + ".Application Master failed. exiting");
                System.exit(200);
            }
        }
    }

    public static void main(String[] args) {
        boolean result = false;
        try {
            TestDSFailedAppMaster appMaster = new TestDSFailedAppMaster();
            boolean doRun = appMaster.init(args);
            if (!doRun) {
                System.exit(0);
            }
            appMaster.run();
            if (appMaster.appAttemptID.getAttemptId() == 1) {
                try {
                    // sleep some time, wait for the AM to launch a container.
                    Thread.sleep(3000);
                } catch (InterruptedException e) {}
                // fail the first am.
                System.exit(100);
            }
            result = appMaster.finish();
        } catch (Throwable t) {
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
