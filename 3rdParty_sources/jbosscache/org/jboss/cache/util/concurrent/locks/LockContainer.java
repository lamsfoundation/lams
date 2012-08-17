package org.jboss.cache.util.concurrent.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * A container for locks
 *
 * @author Manik Surtani
 * @since 3.1.0
 */
public interface LockContainer<E>
{
   /**
    * Tests if a give owner owns a lock on a specified object.
    *
    * @param object object to check
    * @param owner  owner to test
    * @return true if owner owns lock, false otherwise
    */
   boolean ownsLock(E object, Object owner);

   /**
    * @param object object
    * @return true if an object is locked, false otherwise
    */
   boolean isLocked(E object);

   /**
    * @param object object
    * @return the lock for a specific object
    */
   Lock getLock(E object);

   /**
    * @return number of locks held
    */
   int getNumLocksHeld();

   /**
    * Clears all locks held and re-initialises stripes.
    */
   void reset();

   int size();

   void acquireLock(E object);

   boolean acquireLock(E object, long timeout, TimeUnit unit) throws InterruptedException;

   void releaseLock(E object);
}
