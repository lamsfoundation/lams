package org.jgroups.util;

import java.util.concurrent.Executor;

/**
 * @author Bela Ban
 * @version $Id$
 */
public class DirectExecutor implements Executor {
    public void execute(Runnable command) {
        command.run();
    }
}
