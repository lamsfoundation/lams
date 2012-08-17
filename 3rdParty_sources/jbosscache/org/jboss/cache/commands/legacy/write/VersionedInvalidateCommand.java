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
package org.jboss.cache.commands.legacy.write;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheException;
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.VersionedDataCommand;
import org.jboss.cache.commands.write.InvalidateCommand;
import org.jboss.cache.config.Option;
import org.jboss.cache.optimistic.DataVersion;
import org.jboss.cache.optimistic.DataVersioningException;
import org.jboss.cache.transaction.GlobalTransaction;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import java.util.Collections;

/**
 * Behaves like {@link org.jboss.cache.commands.write.InvalidateCommand}. Also, potentially throws a cache exception if
 * data versioning is used and the node in memory has a newer data version than what is passed in.
 * <p/>
 * Finally, the data version of the in-memory node is updated to the version being evicted to prevent versions
 * going out of sync.
 * <p/>
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class VersionedInvalidateCommand extends InvalidateCommand implements VersionedDataCommand
{
   private static final Log log = LogFactory.getLog(VersionedInvalidateCommand.class);
   private static final boolean trace = log.isTraceEnabled();

   /*
     dependencies
    */
   private TransactionManager transactionManager;

   /**
    * Params.
    */
   protected GlobalTransaction globalTransaction;
   private DataVersion dataVersion;

   public VersionedInvalidateCommand(Fqn fqn)
   {
      super(fqn);
   }

   public VersionedInvalidateCommand()
   {
   }

   public void initialize(TransactionManager txManager)
   {
      this.transactionManager = txManager;
   }

   @Override
   public Object perform(InvocationContext ctx)
   {
      NodeSPI node = enforceNodeLoading();
      if (trace) log.trace("Invalidating fqn:" + fqn);
      if (node == null)
      {
         // check if a tombstone already exists
         NodeSPI nodeSPI = dataContainer.peek(fqn, true, true);
         if (nodeSPI == null)
         {
            if (dataVersion == null)
            {
               if (trace)
                  log.trace("Would have created a tombstone since the node doesn't exist, but the version to invalidate is null and hence cannot create a tombstone!");
               return null;
            }
            createTombstone(ctx);
            nodeSPI = (NodeSPI) dataContainer.getRoot().getChild(fqn);
         }
         node = nodeSPI;
      }
      else if (node.getVersion() == null)
      {
         throw new NullPointerException("Node " + node.getFqn() + " has a null data version, and is of type " + node.getClass().getSimpleName() + ".  This command expects versioned nodes.");
      }
      else
      if (dataVersion != null && node.getVersion().newerThan(dataVersion)) // dataVersion *could* be null if the invalidate was triggered by removing a node that did not exist in the first place.
      {
         String errMsg = new StringBuilder("Node found, but version is not equal to or less than the expected [").append(dataVersion).append("].  Is [").append(node.getVersion()).append("] instead!").toString();
         log.warn(errMsg);
         throw new DataVersioningException(errMsg);
      }

      removeData(node, ctx);
      invalidateNode(node);
      node.setVersion(dataVersion);
      return null;
   }

   protected void createTombstone(InvocationContext ctx)
   {
      if (trace)
         log.trace("Node doesn't exist; creating a tombstone with data version " + dataVersion);
      // create the node we need.
      Option o = ctx.getOptionOverrides();
      boolean origCacheModeLocal = o.isCacheModeLocal();
      o.setCacheModeLocal(true);
      o.setDataVersion(dataVersion);
      // if we are in a tx this call should happen outside of any tx
      try
      {
         Transaction suspended = null;
         if (transactionManager != null)
         {
            suspended = transactionManager.suspend();
         }
         spi.put(fqn, Collections.emptyMap());
         if (suspended != null) transactionManager.resume(suspended);
         ctx.getOptionOverrides().setCacheModeLocal(origCacheModeLocal);
      }
      catch (Exception e)
      {
         log.error("Unable to create tombstone!", e);
      }
   }

   protected void removeData(NodeSPI n, InvocationContext ctx) throws CacheException
   {
      notifier.notifyNodeInvalidated(fqn, true, ctx);
      n.clearDataDirect();
      n.setDataLoaded(false);
      notifier.notifyNodeInvalidated(fqn, false, ctx);
   }

   public DataVersion getDataVersion()
   {
      return dataVersion;
   }

   public void setDataVersion(DataVersion dataVersion)
   {
      this.dataVersion = dataVersion;
   }

   public GlobalTransaction getGlobalTransaction()
   {
      return globalTransaction;
   }

   public void setGlobalTransaction(GlobalTransaction gtx)
   {
      this.globalTransaction = gtx;
   }

   public boolean isVersioned()
   {
      return dataVersion != null;
   }

   @Override
   public String toString()
   {
      return "OptimisticInvalidateCommand{" +
            "dataVersion=" + dataVersion +
            " ,fqn=" + fqn +
            '}';
   }

   @Override
   public Object[] getParameters()
   {
      return new Object[]{fqn, dataVersion};
   }

   @Override
   public void setParameters(int commandId, Object[] args)
   {
      fqn = (Fqn) args[0];
      dataVersion = (DataVersion) args[1];
   }
}
