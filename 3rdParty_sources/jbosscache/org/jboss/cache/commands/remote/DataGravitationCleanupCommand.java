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
package org.jboss.cache.commands.remote;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.buddyreplication.BuddyFqnTransformer;
import org.jboss.cache.buddyreplication.BuddyManager;
import org.jboss.cache.commands.CommandsFactory;
import org.jboss.cache.commands.ReplicableCommand;
import org.jboss.cache.commands.write.EvictCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.interceptors.InterceptorChain;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.TransactionTable;

import java.util.List;

/**
 * Data gravitation cleanup handler.  Primarily used by the {@link org.jboss.cache.interceptors.DataGravitatorInterceptor}.
 * This is not a {@link org.jboss.cache.commands.VisitableCommand} and hence
 * not passed up the {@link org.jboss.cache.interceptors.base.CommandInterceptor} chain.
 * <p/>
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
public class DataGravitationCleanupCommand implements ReplicableCommand
{
   public static final int METHOD_ID = 34;
   private static final Log log = LogFactory.getLog(DataGravitationCleanupCommand.class);
   private static final boolean trace = log.isTraceEnabled();

   /* dependencies */
   private BuddyManager buddyManager;
   private TransactionTable transactionTable;
   private InterceptorChain invoker;
   private CommandsFactory commandsFactory;
   private DataContainer dataContainer;

   /* parameters */
   private GlobalTransaction globalTransaction;
   private Fqn fqn;
   private Fqn backup;
   private BuddyFqnTransformer buddyFqnTransformer;


   public DataGravitationCleanupCommand(Fqn primary, Fqn backup)
   {
      this.fqn = primary;
      this.backup = backup;
   }

   public DataGravitationCleanupCommand()
   {
   }

   public void initialize(BuddyManager buddyManager, InterceptorChain invoker, TransactionTable transactionTable,
                          CommandsFactory commandsFactory, DataContainer dataContainer, BuddyFqnTransformer buddyFqnTransformer)
   {
      this.buddyManager = buddyManager;
      this.invoker = invoker;
      this.transactionTable = transactionTable;
      this.commandsFactory = commandsFactory;
      this.dataContainer = dataContainer;
      this.buddyFqnTransformer = buddyFqnTransformer;
   }

   /**
    * Performs a cleanup on nodes that would have been previously gravitated away from the current cache instance.
    */
   public Object perform(InvocationContext ctx) throws Throwable
   {
      if (buddyManager.isDataGravitationRemoveOnFind())
      {
         if (trace)
            log.trace("DataGravitationCleanup: Removing primary (" + fqn + ") and backup (" + backup + ")");

         GlobalTransaction gtx = transactionTable.getCurrentTransaction();
         if (!executeRemove(gtx, fqn))
         {
            // only attempt to clean up the backup if the primary did not exist - a waste of a call otherwise.
            Object result = executeRemove(gtx, backup);
            if (wasNodeRemoved(result))
            {
               // if this is a DIRECT child of a DEAD buddy backup region, then remove the empty dead region structural node.
               Fqn deadBackupRootFqn = null;
               if (buddyFqnTransformer.isDeadBackupFqn(backup) && buddyFqnTransformer.isDeadBackupRoot(backup.getAncestor(backup.size() - 2))
                     && !dataContainer.hasChildren((deadBackupRootFqn = backup.getParent())))
               {
                  if (trace) log.trace("Removing dead backup region " + deadBackupRootFqn);
                  executeRemove(gtx, deadBackupRootFqn);

                  // now check the grand parent and see if we are free of versions
                  deadBackupRootFqn = deadBackupRootFqn.getParent();
                  if (!dataContainer.hasChildren(deadBackupRootFqn))
                  {
                     if (trace) log.trace("Removing dead backup region " + deadBackupRootFqn);
                     executeRemove(gtx, deadBackupRootFqn);
                  }
               }
            }
         }
         else
         {
            if (trace) log.trace("Managed to remove primary (" + fqn + ").  Not bothering with backups.");
         }
      }
      else
      {
         if (trace)
            log.trace("DataGravitationCleanup: Evicting primary (" + fqn + ") and backup (" + backup + ")");
         evictNode(fqn);
         evictNode(backup);
      }
      return null;
   }

   /**
    * Returns true if such a node was removed.
    */
   private boolean executeRemove(GlobalTransaction gtx, Fqn toRemove) throws Throwable
   {
      Object result;
      RemoveNodeCommand removeBackupCommand = commandsFactory.buildRemoveNodeCommand(gtx, toRemove);

      InvocationContext ctx = invoker.getInvocationContext();
      ctx.getOptionOverrides().setCacheModeLocal(true);
      result = invoker.invoke(ctx, removeBackupCommand);
      return result != null && (Boolean) result;
   }

   private boolean wasNodeRemoved(Object result)
   {
      return result != null && (Boolean) result;
   }

   private void evictNode(Fqn fqn) throws Throwable
   {
      if (dataContainer.exists(fqn))
      {
         List<Fqn> toEvict = dataContainer.getNodesForEviction(fqn, true);
         for (Fqn aFqn : toEvict)
         {
            EvictCommand evictFqnCommand = commandsFactory.buildEvictFqnCommand(aFqn);
            invoker.invoke(evictFqnCommand);
         }
      }
      else
      {
         if (trace) log.trace("Not evicting " + fqn + " as it doesn't exist");
      }
   }

   public int getCommandId()
   {
      return METHOD_ID;
   }

   public Fqn getBackup()
   {
      return backup;
   }

   public GlobalTransaction getGlobalTransaction()
   {
      return globalTransaction;
   }

   public void setGlobalTransaction(GlobalTransaction gtx)
   {
      this.globalTransaction = gtx;
   }

   public Fqn getFqn()
   {
      return fqn;
   }

   public Object[] getParameters()
   {
      return new Object[]{fqn, backup};  //To change body of implemented methods use File | Settings | File Templates.
   }

   public void setParameters(int commandId, Object[] args)
   {
      fqn = (Fqn) args[0];
      backup = (Fqn) args[1];
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      if (!super.equals(o)) return false;

      DataGravitationCleanupCommand that = (DataGravitationCleanupCommand) o;

      if (backup != null ? !backup.equals(that.backup) : that.backup != null) return false;
      if (globalTransaction != null ? !globalTransaction.equals(that.globalTransaction) : that.globalTransaction != null)
         return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (globalTransaction != null ? globalTransaction.hashCode() : 0);
      result = 31 * result + (backup != null ? backup.hashCode() : 0);
      return result;
   }

   @Override
   public String toString()
   {
      return "DataGravitationCleanupCommand{" +
            "fqn=" + fqn +
            ", backup=" + backup +
            '}';
   }
}
