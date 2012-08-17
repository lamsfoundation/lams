package org.jboss.cache.util.concurrent.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Per-element container for {@link java.util.concurrent.locks.ReentrantLock}s
 *
 * @author Manik Surtani
 * @since 3.1.0
 */
public class PerElementReentrantLockContainer<E> extends PerElementLockContainer<E>
{
   public PerElementReentrantLockContainer(int concurrencyLevel)
   {
      super(concurrencyLevel);
   }

   public boolean ownsLock(E object, Object owner)
   {
      ReentrantLock l = getLockFromMap(object);
      return l != null && l.isHeldByCurrentThread();
   }

   public boolean isLocked(E object)
   {
      ReentrantLock l = getLockFromMap(object);
      return l != null && l.isLocked();
   }

   private ReentrantLock getLockFromMap(E key)
   {
      return (ReentrantLock) locks.get(key);
   }

   protected final Lock newLock()
   {
      return new ReentrantLock();
   }
}
