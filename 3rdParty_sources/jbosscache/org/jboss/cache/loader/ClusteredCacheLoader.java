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
package org.jboss.cache.loader;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheStatus;
import org.jboss.cache.Fqn;
import org.jboss.cache.Modification;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.RegionManager;
import org.jboss.cache.ReplicationException;
import org.jboss.cache.commands.CommandsFactory;
import org.jboss.cache.commands.DataCommand;
import org.jboss.cache.commands.read.ExistsCommand;
import org.jboss.cache.commands.read.GetChildrenNamesCommand;
import org.jboss.cache.commands.read.GetDataMapCommand;
import org.jboss.cache.commands.read.GetKeyValueCommand;
import org.jboss.cache.commands.remote.ClusteredGetCommand;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.lock.StripedLock;
import org.jgroups.Address;
import org.jgroups.blocks.GroupRequest;
import org.jgroups.blocks.RspFilter;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A cache loader that consults other members in the cluster for values.  Does
 * not propagate update methods since replication should take care of this.  A
 * <code>timeout</code> property is required, a <code>long</code> that
 * specifies in milliseconds how long to wait for results before returning a
 * null.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 */
@ThreadSafe
public class ClusteredCacheLoader extends AbstractCacheLoader
{
   private static final Log log = LogFactory.getLog(ClusteredCacheLoader.class);
   private static final boolean trace = log.isTraceEnabled();
   private StripedLock lock = new StripedLock();
   private ClusteredCacheLoaderConfig config;
   private CommandsFactory commandsFactory;

   /**
    * A test to check whether the cache is in its started state.  If not, calls should not be made as the channel may
    * not have properly started, blocks due to state transfers may be in progress, etc.
    *
    * @return true if the cache is in its STARTED state.
    */
   protected boolean isCacheReady()
   {
      return cache.getCacheStatus() == CacheStatus.STARTED;
   }

   @Inject
   public void setCommandsFactory(CommandsFactory commandsFactory)
   {
      this.commandsFactory = commandsFactory;
   }

   /**
    * Sets the configuration.
    * A property <code>timeout</code> is used as the timeout value.
    */
   public void setConfig(IndividualCacheLoaderConfig base)
   {
      if (base instanceof ClusteredCacheLoaderConfig)
      {
         this.config = (ClusteredCacheLoaderConfig) base;
      }
      else
      {
         config = new ClusteredCacheLoaderConfig(base);
      }
   }

   public IndividualCacheLoaderConfig getConfig()
   {
      return config;
   }

   public Set getChildrenNames(Fqn fqn) throws Exception
   {
      if (!isCacheReady() || !cache.getInvocationContext().isOriginLocal()) return Collections.emptySet();
      lock.acquireLock(fqn, true);
      try
      {
         GetChildrenNamesCommand command = commandsFactory.buildGetChildrenNamesCommand(fqn);
         Object resp = callRemote(command);
         return (Set) resp;
      }
      finally
      {
         lock.releaseLock(fqn);
      }
   }

   @SuppressWarnings("deprecation")
   private Object callRemote(DataCommand dataCommand) throws Exception
   {
      if (trace) log.trace("cache=" + cache.getLocalAddress() + "; calling with " + dataCommand);
      ClusteredGetCommand clusteredGet = commandsFactory.buildClusteredGetCommand(false, dataCommand);
      List resps;
      // JBCACHE-1186
      resps = cache.getRPCManager().callRemoteMethods(null, clusteredGet, GroupRequest.GET_ALL, config.getTimeout(), new ResponseValidityFilter(cache.getMembers(), cache.getLocalAddress()), false);

      if (resps == null)
      {
         if (log.isInfoEnabled())
            log.info("No replies to call " + dataCommand + ".  Perhaps we're alone in the cluster?");
         throw new ReplicationException("No replies to call " + dataCommand + ".  Perhaps we're alone in the cluster?");
      }
      else
      {
         // test for and remove exceptions
         Iterator i = resps.iterator();
         Object result = null;
         while (i.hasNext())
         {
            Object o = i.next();
            if (o instanceof Exception)
            {
               if (log.isDebugEnabled())
                  log.debug("Found remote exception among responses - removing from responses list", (Exception) o);
            }
            else if (o != null)
            {
               // keep looping till we find a FOUND answer.
               List<Boolean> clusteredGetResp = (List<Boolean>) o;
               // found?
               if (clusteredGetResp.get(0))
               {
                  result = clusteredGetResp.get(1);
                  break;
               }
            }
            else if (!cache.getConfiguration().isUseRegionBasedMarshalling())
            {
               throw new IllegalStateException("Received unexpected null response to " + clusteredGet);
            }
            // else region was inactive on peer;
            // keep looping to see if anyone else responded
         }

         if (trace) log.trace("got responses " + resps);
         return result;
      }
   }

   public Map get(Fqn name) throws Exception
   {
      return get0(name);
   }

   protected Map get0(Fqn name) throws Exception
   {
      // DON'T make a remote call if this is a remote call in the first place - leads to deadlocks - JBCACHE-1103
      if (!isCacheReady() || !cache.getInvocationContext().isOriginLocal())
            return null;
//         return Collections.emptyMap();
      lock.acquireLock(name, true);
      try
      {
         GetDataMapCommand command = commandsFactory.buildGetDataMapCommand(name);
         Object resp = callRemote(command);
         return (Map) resp;
      }
      finally
      {
         lock.releaseLock(name);
      }
   }

   public boolean exists(Fqn name) throws Exception
   {
      // DON'T make a remote call if this is a remote call in the first place - leads to deadlocks - JBCACHE-1103
      if (!isCacheReady() || !cache.getInvocationContext().isOriginLocal()) return false;

      lock.acquireLock(name, false);
      try
      {
         ExistsCommand command = commandsFactory.buildExistsNodeCommand(name);
         Object resp = callRemote(command);
         return resp != null && (Boolean) resp;
      }
      finally
      {
         lock.releaseLock(name);
      }
   }

   public Object put(Fqn name, Object key, Object value) throws Exception
   {
      // DON'T make a remote call if this is a remote call in the first place - leads to deadlocks - JBCACHE-1103
      if (!isCacheReady() || !cache.getInvocationContext().isOriginLocal()) return null;
      lock.acquireLock(name, true);
      try
      {
         NodeSPI n = cache.peek(name, false);
         if (n == null)
         {
            GetKeyValueCommand command = commandsFactory.buildGetKeyValueCommand(name, key, true);
            return callRemote(command);
         }
         else
         {
            // dont bother with a remote call
            return n.getDirect(key);
         }
      }
      finally
      {
         lock.releaseLock(name);
      }
   }

   /**
    * Does nothing; replication handles put.
    */
   public void put(Fqn name, Map attributes) throws Exception
   {
   }

   /**
    * Does nothing; replication handles put.
    */
   @Override
   public void put(List<Modification> modifications) throws Exception
   {
   }

   /**
    * Fetches the remove value, does not remove.  Replication handles
    * removal.
    */
   public Object remove(Fqn name, Object key) throws Exception
   {
      // DON'T make a remote call if this is a remote call in the first place - leads to deadlocks - JBCACHE-1103
      if (!isCacheReady() || !cache.getInvocationContext().isOriginLocal()) return false;
      lock.acquireLock(name, true);
      try
      {
         NodeSPI n = cache.peek(name, true);
         if (n == null)
         {
            GetKeyValueCommand command = commandsFactory.buildGetKeyValueCommand(name, key, true);
            return callRemote(command);
         }
         else
         {
            // dont bother with a remote call
            return n.getDirect(key);
         }
      }
      finally
      {
         lock.releaseLock(name);
      }
   }

   /**
    * Does nothing; replication handles removal.
    */
   public void remove(Fqn name) throws Exception
   {
      // do nothing
   }

   /**
    * Does nothing; replication handles removal.
    */
   public void removeData(Fqn name) throws Exception
   {
   }

   /**
    * Does nothing.
    */
   @Override
   public void prepare(Object tx, List modifications, boolean one_phase) throws Exception
   {
   }

   /**
    * Does nothing.
    */
   @Override
   public void commit(Object tx) throws Exception
   {
   }

   /**
    * Does nothing.
    */
   @Override
   public void rollback(Object tx)
   {
   }

   @Override
   public void loadEntireState(ObjectOutputStream os) throws Exception
   {
      //intentional no-op      
   }

   @Override
   public void loadState(Fqn subtree, ObjectOutputStream os) throws Exception
   {
      // intentional no-op      
   }

   @Override
   public void storeEntireState(ObjectInputStream is) throws Exception
   {
      // intentional no-op      
   }

   @Override
   public void storeState(Fqn subtree, ObjectInputStream is) throws Exception
   {
      // intentional no-op      
   }

   @Override
   public void setRegionManager(RegionManager manager)
   {
   }

   public static class ResponseValidityFilter implements RspFilter
   {
      private int numValidResponses = 0;
      private List<Address> pendingResponders;

      public ResponseValidityFilter(List<Address> expected, Address localAddress)
      {
         this.pendingResponders = new ArrayList<Address>(expected);
         // We'll never get a response from ourself
         this.pendingResponders.remove(localAddress);
      }

      public boolean isAcceptable(Object object, Address address)
      {
         pendingResponders.remove(address);

         if (object instanceof List)
         {
            List response = (List) object;
            Boolean foundResult = (Boolean) response.get(0);
            if (foundResult) numValidResponses++;
         }
         // always return true to make sure a response is logged by the JGroups RpcDispatcher.
         return true;
      }

      public boolean needMoreResponses()
      {
         return numValidResponses < 1 && pendingResponders.size() > 0;
      }

   }

}
