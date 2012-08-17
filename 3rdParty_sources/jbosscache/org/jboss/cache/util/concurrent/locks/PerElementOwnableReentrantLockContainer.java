package org.jboss.cache.util.concurrent.locks;

import org.jboss.cache.invocation.InvocationContextContainer;

import java.util.concurrent.locks.Lock;

/**
 * Per element container for {@link org.jboss.cache.util.concurrent.locks.OwnableReentrantLock}s
 *
 * @author Manik Surtani
 * @since 3.1.0
 */
public class PerElementOwnableReentrantLockContainer<E> extends PerElementLockContainer<E>
{
   private InvocationContextContainer icc;
   
   public PerElementOwnableReentrantLockContainer(int concurrencyLevel, InvocationContextContainer icc)
   {
      super(concurrencyLevel);
      this.icc = icc;
   }

   public boolean ownsLock(E object, Object owner)
   {
      OwnableReentrantLock l = getLockFromMap(object);
      return l != null && owner.equals(l.getOwner());
   }

   public boolean isLocked(E object)
   {
      OwnableReentrantLock l = getLockFromMap(object);
      return l != null && l.isLocked();
   }

   private OwnableReentrantLock getLockFromMap(E key)
   {
      return (OwnableReentrantLock) locks.get(key);
   }

   protected final Lock newLock()
   {
      return new OwnableReentrantLock(icc);
   }
}
