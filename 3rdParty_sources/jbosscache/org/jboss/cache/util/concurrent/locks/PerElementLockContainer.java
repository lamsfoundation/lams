package org.jboss.cache.util.concurrent.locks;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * A lock container that maintains a new lock per element
 *
 * @author Manik Surtani
 * @since 3.1.0
 */
public abstract class PerElementLockContainer<E> implements LockContainer<E>
{
   protected final ConcurrentMap<E, Lock> locks;

   protected PerElementLockContainer(int concurrencyLevel)
   {
      locks = new ConcurrentHashMap<E, Lock>(16, .75f, concurrencyLevel);
   }

   protected abstract Lock newLock();

   public final Lock getLock(E object)
   {
      Lock l = locks.get(object);
      if (l == null) l = newLock();
      Lock tmp = locks.putIfAbsent(object, l);
      if (tmp != null) l = tmp;
      return l;
   }

   public int getNumLocksHeld()
   {
      return locks.size();
   }

   public void reset()
   {
      for (Lock l: locks.values())
      {
         try
         {
            l.unlock();
         }
         catch (Exception e)
         {
            // no-op
         }
      }
      locks.clear();
   }

   public int size()
   {
      return locks.size();
   }

   public void acquireLock(E object)
   {
      while (true)
      {
         Lock lock = getLock(object);
         lock.lock();
         // now check that the lock is still valid...
         Lock currentLock = locks.putIfAbsent(object, lock);
         if (currentLock != null && lock != currentLock)
         {
            // we acquired the wrong lock!
            lock.unlock();
         }
         else
         {
            // we got the right lock!
            break;
         }
      }
   }

   public boolean acquireLock(E object, long timeout, TimeUnit unit) throws InterruptedException
   {
      while (true)
      {
         Lock lock = getLock(object);
         if (lock.tryLock(timeout, unit))
         {
            // now check that the lock is still valid...
            Lock currentLock = locks.putIfAbsent(object, lock);
            if (currentLock != null && lock != currentLock)
            {
               // we acquired the wrong lock!
               lock.unlock();
            }
            else
            {
               // we got the right lock!
               return true;
            }
         }
         else
         {
            // if we haven't acquired the lock (i.e., timed out) return false
            return false;
         }
      }
   }

   public void releaseLock(E object)
   {
      Lock l = locks.remove(object);
      if (l != null) l.unlock();
   }
}
