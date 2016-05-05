package org.lamsfoundation.lams.dbupdates;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.jdbc.AutoPatchService;

/**
 * Synchronizes patching across modules, which in vanilla version can be run in parallel.
 * WildFly loads modules simultaneously and AutoPatch uses static methods which cause errors
 * when they are entered at the same time.
 *
 * @author Marcin Cieslak, Marek Bubala
 */
public class AutoPatchServiceSynchronizer extends AutoPatchService {

    protected static final Lock lock = new ReentrantLock();

    @Override
    public void patch() throws MigrationException {
	AutoPatchServiceSynchronizer.lock.lock();

	try {
	    super.patch();
	} finally {
	    AutoPatchServiceSynchronizer.lock.unlock();
	}
    }
}