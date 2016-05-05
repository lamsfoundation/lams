package org.lamsfoundation.lams.dbupdates;

import javax.servlet.ServletContextEvent;

import com.tacitknowledge.util.migration.jdbc.WebAppJNDIMigrationLauncher;

/**
 * Synchronizes patching across modules, which in vanilla version can be run in parallel.
 * WildFly loads modules simultaneously and AutoPatch uses static methods which cause errors
 * when they are entered at the same time.
 *
 * @author Marcin Cieslak, Marek Bubala
 */
public class WebAppJNDIMigrationLauncherSynchronizer extends WebAppJNDIMigrationLauncher {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
	AutoPatchServiceSynchronizer.lock.lock();

	try {
	    super.contextInitialized(sce);
	} finally {
	    AutoPatchServiceSynchronizer.lock.unlock();
	}
    }
}