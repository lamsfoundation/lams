/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2000 - 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.cache;

import net.jcip.annotations.ThreadSafe;
import org.jboss.cache.buddyreplication.BuddyManager;
import static org.jboss.cache.lock.LockType.WRITE;

import java.util.ArrayList;
import java.util.Set;

/**
 * For optimistic and pessimistically locked caches
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani</a>
 * @since 2.0.0
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@ThreadSafe
@Deprecated
public class LegacyRegionManagerImpl extends RegionManagerImpl
{
   /**
    * Causes the cache to stop accepting replication events for the subtree
    * rooted at <code>subtreeFqn</code> and evict all nodes in that subtree.
    * <p/>
    * This is legacy code and should not be called directly.  This is a private method for now and will be refactored out.
    * You should be using {@link #activate(Fqn)} and {@link #deactivate(Fqn)}
    * <p/>
    *
    * @param fqn Fqn string indicating the uppermost node in the
    *            portion of the cache that should be activated.
    * @throws CacheException        if there is a problem evicting nodes
    * @throws IllegalStateException if {@link org.jboss.cache.config.Configuration#isUseRegionBasedMarshalling()} is <code>false</code>
    */
   @Override
   protected void inactivateRegion(Fqn fqn) throws CacheException
   {
      NodeSPI parent = null;
      NodeSPI subtreeRoot = null;
      boolean parentLocked = false;
      boolean subtreeLocked = false;

      try
      {
         // Record that this fqn is in status change, so can't provide state
         lock(fqn);

         if (!isInactive(fqn))
         {
            deactivate(fqn);
         }

         // Create a list with the Fqn in the main cache and any buddy backup trees
         BuddyManager buddyManager = cache.getBuddyManager();
         ArrayList<Fqn> list = new ArrayList<Fqn>();
         list.add(fqn);

         if (buddyManager != null)
         {
            Set buddies = cache.peek(BuddyManager.BUDDY_BACKUP_SUBTREE_FQN, false, false).getChildrenNames();
            if (buddies != null)
            {
               for (Object buddy : buddies)
               {
                  list.add(buddyFqnTransformer.getBackupFqn((String) buddy, fqn));
               }
            }
         }

         long stateFetchTimeout = cache.getConfiguration().getLockAcquisitionTimeout() + 5000;
         // Remove the subtree from the main cache  and any buddy backup trees
         for (Fqn subtree : list)
         {
            subtreeRoot = cache.peek(subtree, false);
            if (subtreeRoot != null)
            {
               // Acquire locks
               subtreeLocked = lockManager.lockAll(subtreeRoot, WRITE, getOwnerForLock(), stateFetchTimeout);

               // Lock the parent, as we're about to write to it
               parent = subtreeRoot.getParentDirect();
               if (parent != null)
                  parentLocked = lockManager.lockAll(parent, WRITE, getOwnerForLock(), stateFetchTimeout);

               // Remove the subtree
               cache.evict(subtree, true);

               // Release locks
               if (parent != null)
               {
                  log.debug("forcing release of locks in parent");
                  lockManager.unlockAll(parent);
               }

               parentLocked = false;

               log.debug("forcing release of all locks in subtree");
               lockManager.unlockAll(subtreeRoot);
               subtreeLocked = false;
            }
         }
      }
      catch (InterruptedException e)
      {
         throw new CacheException(e);
      }
      finally
      {
         // If we didn't succeed, undo the marshalling change
         // NO. Since we inactivated, we may have missed changes
         //if (!success && !inactive)
         //   marshaller_.activate(subtreeFqn);

         // If necessary, release locks
         if (parentLocked)
         {
            log.debug("forcing release of locks in parent");
            try
            {
               if (parent != null) lockManager.unlockAll(parent);
            }
            catch (Throwable t)
            {
               log.error("failed releasing locks", t);
            }
         }
         if (subtreeLocked)
         {
            log.debug("forcing release of all locks in subtree");
            try
            {
               if (subtreeRoot != null) lockManager.unlockAll(subtreeRoot);
            }
            catch (Throwable t)
            {
               log.error("failed releasing locks", t);
            }
         }

         unlock(fqn);
      }
   }

   private Object getOwnerForLock()
   {
      Object owner = cache.getCurrentTransaction();
      return owner == null ? Thread.currentThread() : owner;
   }
}