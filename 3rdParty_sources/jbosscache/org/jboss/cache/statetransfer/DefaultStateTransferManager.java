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
package org.jboss.cache.statetransfer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheException;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.RegionEmptyException;
import org.jboss.cache.RegionManager;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.loader.CacheLoaderManager;
import org.jboss.cache.marshall.InactiveRegionException;
import org.jboss.cache.marshall.Marshaller;
import org.jboss.cache.marshall.NodeData;
import org.jboss.cache.marshall.NodeDataMarker;
import org.jboss.cache.util.CachePrinter;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The default state transfer manager to be used when using MVCC locking.
 */
public class DefaultStateTransferManager implements StateTransferManager
{
   protected final static Log log = LogFactory.getLog(DefaultStateTransferManager.class);
   protected static final boolean trace = log.isTraceEnabled();

   public static final NodeData STREAMING_DELIMITER_NODE = new NodeDataMarker();
   public static final String PARTIAL_STATE_DELIMITER = "_PARTIAL_STATE_DELIMITER";

   protected CacheSPI cache;
   protected Marshaller marshaller;
   protected RegionManager regionManager;
   protected Configuration configuration;
   private CacheLoaderManager cacheLoaderManager;
   boolean fetchTransientState;
   boolean fetchPersistentState;
   protected long stateRetrievalTimeout;
   protected StateTransferIntegrator integrator;
   protected StateTransferGenerator generator;


   @Inject
   public void injectDependencies(CacheSPI cache, Marshaller marshaller, RegionManager regionManager, Configuration configuration, CacheLoaderManager cacheLoaderManager, StateTransferIntegrator integrator, StateTransferGenerator generator)
   {
      this.cache = cache;
      this.regionManager = regionManager;
      this.marshaller = marshaller;
      this.configuration = configuration;
      this.cacheLoaderManager = cacheLoaderManager;
      this.integrator = integrator;
      this.generator = generator;
   }

   @Start(priority = 14)
   public void start()
   {
      fetchTransientState = configuration.isFetchInMemoryState();
      // do not do state transfers if the cache loader config is shared
      fetchPersistentState = cacheLoaderManager != null && cacheLoaderManager.isFetchPersistentState() && !configuration.getCacheLoaderConfig().isShared();
      stateRetrievalTimeout = configuration.getStateRetrievalTimeout();
   }

   public void getState(ObjectOutputStream out, Fqn fqn, long timeout, boolean force, boolean suppressErrors) throws Exception
   {
      // can't give state for regions currently being activated/inactivated
      boolean canProvideState = cache.getCacheStatus().allowInvocations()
                                && !regionManager.isInactive(fqn) && cache.peek(fqn, false) != null;
      if (trace) log.trace("Can provide state? " + canProvideState);
      if (canProvideState && (fetchPersistentState || fetchTransientState))
      {
         marshaller.objectToObjectStream(true, out);
         long startTime = System.currentTimeMillis();
         InternalNode subtreeRoot = fqn.isRoot() ? cache.getRoot().getDelegationTarget() : cache.getNode(fqn).getDelegationTarget();

         // we don't need READ locks for MVCC based state transfer!
         if (log.isDebugEnabled())
            log.debug("Generating in-memory (transient) state for subtree " + fqn);

         // this method will throw a StateProviderBusyException if the state provider is busy providing state to someone else.
         generator.generateState(out, subtreeRoot, fetchTransientState, fetchPersistentState, suppressErrors);

         if (log.isDebugEnabled())
            log.debug("Successfully generated state in " + CachePrinter.prettyPrint(System.currentTimeMillis() - startTime));
      }
      else
      {
         marshaller.objectToObjectStream(false, out);
         Exception e = null;
         if (!canProvideState)
         {
            String exceptionMessage = "Cache instance at " + cache.getLocalAddress() + " cannot provide state for fqn " + fqn + ".";

            if (!cache.getCacheStatus().allowInvocations())
               exceptionMessage = " [Cache is not in the correct status]";

            if (regionManager.isInactive(fqn))
            {
               exceptionMessage += " Region for fqn " + fqn + " is inactive.";
               e = new InactiveRegionException(exceptionMessage);
            }
            // this is not really an exception.  Just provide empty state. The exception is just a signal.  Yes, lousy.  - JBCACHE-1349
            if (cache.peek(fqn, false, false) == null)
            {
               e = new RegionEmptyException();
            }
         }
         if (!fetchPersistentState && !fetchTransientState)
         {
            e = new CacheException("Cache instance at " + cache.getLocalAddress() + " is not configured to provide state");
         }
         marshaller.objectToObjectStream(e, out);
         if (e != null) throw e;
      }
   }

   public void setState(ObjectInputStream in, Fqn targetRoot) throws Exception
   {
      if (trace) log.trace("Setting state on Fqn root " + targetRoot);
      cache.getInvocationContext().getOptionOverrides().setSkipCacheStatusCheck(true);
      NodeSPI target = cache.getNode(targetRoot);
      if (target == null)
      {
         // Create the integration root, but do not replicate
         cache.getInvocationContext().getOptionOverrides().setCacheModeLocal(true);

         //needed for BR state transfers
         cache.getInvocationContext().getOptionOverrides().setSkipCacheStatusCheck(true);
         cache.put(targetRoot, null);
         cache.getInvocationContext().getOptionOverrides().setSkipCacheStatusCheck(true);
         target = cache.getNode(targetRoot);
      }
      Object o = marshaller.objectFromObjectStream(in);
      Boolean hasState = (Boolean) o;
      if (hasState)
      {
         setState(in, target);
      }
      else
      {
         throw new CacheException("Cache instance at " + cache.getLocalAddress()
               + " cannot integrate state since state provider could not provide state due to " + marshaller.objectFromObjectStream(in));
      }
   }

   /**
    * Set the portion of the cache rooted in <code>targetRoot</code>
    * to match the given state. Updates the contents of <code>targetRoot</code>
    * to reflect those in <code>new_state</code>.
    * <p/>
    * <strong>NOTE:</strong> This method performs no locking of nodes; it
    * is up to the caller to lock <code>targetRoot</code> before calling
    * this method.
    *
    * @param state      a serialized byte[][] array where element 0 is the
    *                   transient state (or null) , and element 1 is the
    *                   persistent state (or null)
    * @param targetRoot node into which the state should be integrated
    */
   protected void setState(ObjectInputStream state, NodeSPI targetRoot) throws Exception
   {
      long startTime = System.currentTimeMillis();
      /*
       * Vladimir/Manik/Brian (Dec 7,2006)
       *
       * integrator.integrateState(in,targetRoot, cl) will call cache.put for each
       * node read from stream. Having option override below allows nodes read
       * to be directly stored into a tree since we bypass interceptor chain.
       *
       */
      if (log.isDebugEnabled())
         log.debug("starting state integration at node " + targetRoot + ".  Fetch Persistent State = " + fetchPersistentState);
      integrator.integrateState(state, targetRoot.getDelegationTarget(), targetRoot.getFqn(), fetchPersistentState);

      if (log.isDebugEnabled())
         log.debug("successfully integrated state in " + CachePrinter.prettyPrint(System.currentTimeMillis() - startTime));
   }
}
