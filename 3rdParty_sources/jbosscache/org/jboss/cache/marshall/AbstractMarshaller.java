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
package org.jboss.cache.marshall;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.Fqn;
import org.jboss.cache.RegionManager;
import org.jboss.cache.commands.DataCommand;
import org.jboss.cache.commands.ReplicableCommand;
import org.jboss.cache.commands.WriteCommand;
import org.jboss.cache.commands.read.ExistsCommand;
import org.jboss.cache.commands.read.GetChildrenNamesCommand;
import org.jboss.cache.commands.read.GetDataMapCommand;
import org.jboss.cache.commands.read.GetKeyValueCommand;
import org.jboss.cache.commands.read.GetKeysCommand;
import org.jboss.cache.commands.read.GravitateDataCommand;
import org.jboss.cache.commands.remote.AnnounceBuddyPoolNameCommand;
import org.jboss.cache.commands.remote.AssignToBuddyGroupCommand;
import org.jboss.cache.commands.remote.ClusteredGetCommand;
import org.jboss.cache.commands.remote.DataGravitationCleanupCommand;
import org.jboss.cache.commands.remote.RemoveFromBuddyGroupCommand;
import org.jboss.cache.commands.remote.ReplicateCommand;
import org.jboss.cache.commands.remote.StateTransferControlCommand;
import org.jboss.cache.commands.tx.AbstractTransactionCommand;
import org.jboss.cache.commands.tx.CommitCommand;
import org.jboss.cache.commands.tx.OptimisticPrepareCommand;
import org.jboss.cache.commands.tx.PrepareCommand;
import org.jboss.cache.commands.tx.RollbackCommand;
import org.jboss.cache.commands.write.EvictCommand;
import org.jboss.cache.commands.write.InvalidateCommand;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.io.ByteBuffer;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jgroups.util.Buffer;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract AbstractMarshaller for JBoss Cache.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 */
public abstract class AbstractMarshaller implements Marshaller
{
   protected boolean useRegionBasedMarshalling;
   protected RegionManager regionManager;
   protected boolean defaultInactive;
   protected Log log;
   protected boolean trace;

   /**
    * Map<GlobalTransaction, Fqn> for prepared tx that have not committed
    */
   private Map<GlobalTransaction, Fqn> transactions = new ConcurrentHashMap<GlobalTransaction, Fqn>(16);
   protected Configuration configuration;
   protected ClassLoader defaultClassLoader;
   protected boolean useRefs = false;

   @Inject
   void injectDependencies(RegionManager regionManager, Configuration configuration, ClassLoader defaultClassLoader)
   {
      this.defaultClassLoader = defaultClassLoader;
      this.regionManager = regionManager;
      this.configuration = configuration;
   }

   @Start
   @SuppressWarnings("deprecation")
   protected void init()
   {
      this.useRegionBasedMarshalling = configuration.isUseRegionBasedMarshalling();
      this.defaultInactive = configuration.isInactiveOnStartup();
   }

   protected void initLogger()
   {
      log = LogFactory.getLog(getClass());
      trace = log.isTraceEnabled();
   }

   // implement the basic contract set in RPCDispatcher.AbstractMarshaller
   public byte[] objectToByteBuffer(Object obj) throws Exception
   {
      Buffer b = objectToBuffer(obj);
      byte[] bytes = new byte[b.getLength()];
      System.arraycopy(b.getBuf(), b.getOffset(), bytes, 0, b.getLength());
      return bytes;
   }

   public ByteBuffer objectToBuffer(Object o) throws Exception
   {
      throw new RuntimeException("Needs to be overridden!");
   }

   public Object objectFromByteBuffer(byte[] bytes) throws Exception
   {
      return objectFromByteBuffer(bytes, 0, bytes.length);
   }

   public Object objectFromByteBuffer(byte[] bytes, int offset, int len) throws Exception
   {
      throw new RuntimeException("Needs to be overridden!");
   }

   public Object objectFromStream(InputStream in) throws Exception
   {
      throw new RuntimeException("Needs to be overridden!");
   }

   public RegionalizedMethodCall regionalizedMethodCallFromByteBuffer(byte[] buffer) throws Exception
   {
      throw new RuntimeException("Needs to be overridden!");
   }

   public RegionalizedMethodCall regionalizedMethodCallFromObjectStream(ObjectInputStream in) throws Exception
   {
      throw new RuntimeException("Needs to be overridden!");
   }

   protected Fqn extractFqn(ReplicableCommand cmd)
   {
      if (cmd == null) throw new NullPointerException("Command is null");

      Fqn fqn = null;
      //Object[] args = cmd.getParameters();
      switch (cmd.getCommandId())
      {
         case OptimisticPrepareCommand.METHOD_ID:
         case PrepareCommand.METHOD_ID:

            // Prepare method has a list of modifications. We will just take the first one and extract.
            PrepareCommand pc = (PrepareCommand) cmd;
            List<WriteCommand> modifications = pc.getModifications();
            fqn = extractFqn(modifications.get(0));

            // If this is two phase commit, map the FQN to the GTX so
            // we can find it when the commit/rollback comes through
            if (!pc.isOnePhaseCommit()) transactions.put(pc.getGlobalTransaction(), fqn);

            break;

         case RollbackCommand.METHOD_ID:
         case CommitCommand.METHOD_ID:
            // We stored the fqn in the transactions map during the prepare phase
            fqn = transactions.remove(((AbstractTransactionCommand) cmd).getGlobalTransaction());
            break;

         case GravitateDataCommand.METHOD_ID:
         case EvictCommand.METHOD_ID:
         case EvictCommand.VERSIONED_METHOD_ID:
         case InvalidateCommand.METHOD_ID:
         case GetChildrenNamesCommand.METHOD_ID:
         case GetDataMapCommand.METHOD_ID:
         case GetKeyValueCommand.METHOD_ID:
         case GetKeysCommand.METHOD_ID:
         case ExistsCommand.METHOD_ID:
            fqn = ((DataCommand) cmd).getFqn();
            break;

         case DataGravitationCleanupCommand.METHOD_ID:
            fqn = ((DataGravitationCleanupCommand) cmd).getFqn();
            break;

         case AnnounceBuddyPoolNameCommand.METHOD_ID:
         case AssignToBuddyGroupCommand.METHOD_ID:
         case RemoveFromBuddyGroupCommand.METHOD_ID:
         case StateTransferControlCommand.METHOD_ID:
            break;

            // possible when we have a replication queue.
         case ReplicateCommand.SINGLE_METHOD_ID:
            fqn = extractFqn(((ReplicateCommand) cmd).getSingleModification());
            break;
         case ReplicateCommand.MULTIPLE_METHOD_ID:
            fqn = extractFqn(((ReplicateCommand) cmd).getModifications().get(0));
            break;
         case ClusteredGetCommand.METHOD_ID:
            fqn = ((ClusteredGetCommand) cmd).getDataCommand().getFqn();
            break;
         default:
            if (cmd instanceof DataCommand)
            {
               fqn = ((DataCommand) cmd).getFqn();
            }
            else
            {
               throw new IllegalArgumentException("AbstractMarshaller.extractFqn(): Unknown id in method call: " + cmd);
            }
            break;
      }

      if (trace) log.trace("extract(): received " + cmd + "extracted fqn: " + fqn);

      return fqn;
   }
}
