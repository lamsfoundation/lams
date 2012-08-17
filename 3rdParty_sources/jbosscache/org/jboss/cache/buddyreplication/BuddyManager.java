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
package org.jboss.cache.buddyreplication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheException;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.RPCManager;
import org.jboss.cache.Region;
import org.jboss.cache.RegionEmptyException;
import org.jboss.cache.RegionManager;
import org.jboss.cache.commands.CommandsFactory;
import org.jboss.cache.commands.ReplicableCommand;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.commands.remote.AnnounceBuddyPoolNameCommand;
import org.jboss.cache.commands.remote.AssignToBuddyGroupCommand;
import org.jboss.cache.commands.remote.RemoveFromBuddyGroupCommand;
import org.jboss.cache.commands.remote.ReplicateCommand;
import org.jboss.cache.config.BuddyReplicationConfig;
import org.jboss.cache.config.BuddyReplicationConfig.BuddyLocatorConfig;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.Option;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.factories.annotations.Stop;
import org.jboss.cache.io.ExposedByteArrayOutputStream;
import org.jboss.cache.jmx.annotations.ManagedAttribute;
import org.jboss.cache.lock.TimeoutException;
import org.jboss.cache.notifications.Notifier;
import org.jboss.cache.notifications.annotation.CacheListener;
import org.jboss.cache.notifications.annotation.ViewChanged;
import org.jboss.cache.notifications.event.ViewChangedEvent;
import org.jboss.cache.statetransfer.StateTransferManager;
import org.jboss.cache.util.concurrent.ConcurrentHashSet;
import org.jboss.cache.util.reflect.ReflectionUtil;
import org.jboss.util.stream.MarshalledValueInputStream;
import org.jboss.util.stream.MarshalledValueOutputStream;
import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.View;
import org.jgroups.util.Util;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Class that manages buddy replication groups.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 */
public class BuddyManager
{
   private final Log log = LogFactory.getLog(BuddyManager.class);
   private boolean trace;

   BuddyReplicationConfig config;

   BuddyLocator buddyLocator;

   Fqn2BuddyFqnVisitor fqnVisitorFqn2;

   CommandsFactory commandsFactory;

   /**
    * back-refernce to the CacheImpl object
    */
   private CacheSPI<?, ?> cache;
   private Configuration configuration;
   private RegionManager regionManager;
   private Notifier notifier;
   private StateTransferManager stateTransferManager;
   private RPCManager rpcManager;
   /**
    * The buddy group set up for this instance
    */
   BuddyGroup buddyGroup;

   /**
    * Map of buddy pools received from broadcasts
    */
   final Map<Address, String> buddyPool = new ConcurrentHashMap<Address, String>();

   /**
    * The nullBuddyPool is a set of addresses that have not specified buddy pools.
    */
   final Set<Address> nullBuddyPool = new ConcurrentHashSet<Address>();

   /**
    * Map of buddy groups the current instance participates in as a backup node.
    * Keyed on String group name, values are BuddyGroup objects.
    * Needs to deal with concurrent access - concurrent assignTo/removeFrom buddy grp
    */
   final Map<Address, BuddyGroup> buddyGroupsIParticipateIn = new ConcurrentHashMap<Address, BuddyGroup>();

   /**
    * Queue to deal with queued up view change requests - which are handled asynchronously
    */
   private final BlockingQueue<MembershipChange> queue = new LinkedBlockingQueue<MembershipChange>();

   /**
    * Async thread that handles items on the view change queue
    */
   private final AsyncViewChangeHandlerThread asyncViewChangeHandler = new AsyncViewChangeHandlerThread();

   /**
    * Constants representng the buddy backup subtree
    */
   public static final String BUDDY_BACKUP_SUBTREE = "_BUDDY_BACKUP_";
   public static final Fqn BUDDY_BACKUP_SUBTREE_FQN = Fqn.fromString(BUDDY_BACKUP_SUBTREE);

   /**
    * number of times to retry communicating with a selected buddy if the buddy has not been initialised.
    */
   private final static int UNINIT_BUDDIES_RETRIES = 5;
   /**
    * wait time between retries
    */
   private static final long[] UNINIT_BUDDIES_RETRY_NAPTIME = {500, 1000, 1500, 2000, 2500};

   /**
    * Lock to synchronise on to ensure buddy pool info is received before buddies are assigned to groups.
    */
   private final Object poolInfoNotifierLock = new Object();

   private final CountDownLatch initialisationLatch = new CountDownLatch(1);
   // a dummy MembershipChange - a poison-pill to be placed on the membership change queue to notify async handler
   // threads to exit gracefully when the BuddyManager has been stopped.
   private static final MembershipChange STOP_NOTIFIER = new MembershipChange(null, null);

   private ViewChangeListener viewChangeListener; // the view-change viewChangeListener

   private boolean receivedBuddyInfo;
   private DataContainer dataContainer;
   private BuddyFqnTransformer buddyFqnTransformer;

   public BuddyManager()
   {
   }

   public BuddyManager(BuddyReplicationConfig config)
   {
      setupInternals(config);
   }


   private void setupInternals(BuddyReplicationConfig config)
   {
      this.config = config;
      trace = log.isTraceEnabled();
      BuddyLocatorConfig blc = config.getBuddyLocatorConfig();
      try
      {
         // it's OK if the buddy locator config is null.
         buddyLocator = (blc == null) ? createDefaultBuddyLocator() : createBuddyLocator(blc);
      }
      catch (Exception e)
      {
         log.warn("Caught exception instantiating buddy locator", e);
         log.error("Unable to instantiate specified buddyLocatorClass [" + blc + "].  Using default buddyLocator [" + NextMemberBuddyLocator.class.getName() + "] instead, with default properties.");
         buddyLocator = createDefaultBuddyLocator();
      }

      // Update the overall config with the BuddyLocatorConfig actually used
      if (blc != buddyLocator.getConfig())
      {
         config.setBuddyLocatorConfig(buddyLocator.getConfig());
      }
   }

   @Inject
   public void injectDependencies(CacheSPI cache, Configuration configuration, RegionManager regionManager,
                                  StateTransferManager stateTransferManager, RPCManager rpcManager, Notifier notifier,
                                  CommandsFactory factory, DataContainer dataContainer, BuddyFqnTransformer transformer)
   {
      this.cache = cache;
      this.configuration = configuration;
      this.regionManager = regionManager;
      this.stateTransferManager = stateTransferManager;
      this.rpcManager = rpcManager;
      this.notifier = notifier;
      this.commandsFactory = factory;
      this.dataContainer = dataContainer;
      buddyFqnTransformer = transformer;
   }

   public BuddyReplicationConfig getConfig()
   {
      return config;
   }

   protected BuddyLocator createBuddyLocator(BuddyLocatorConfig config) throws ClassNotFoundException, IllegalAccessException, InstantiationException
   {
      BuddyLocator bl = (BuddyLocator) Class.forName(config.getBuddyLocatorClass()).newInstance();
      bl.init(config);
      return bl;
   }

   protected BuddyLocator createDefaultBuddyLocator()
   {
      BuddyLocator bl = new NextMemberBuddyLocator();
      bl.init(null);
      return bl;
   }

   public boolean isEnabled()
   {
      return config.isEnabled();
   }

   public String getBuddyPoolName()
   {
      return config.getBuddyPoolName();
   }

   /**
    * Stops the buddy manager and the related async thread.
    */
   @Stop(priority = 5)
   public void stop()
   {
      if (isEnabled())
      {
         log.debug("Stopping BuddyManager");
         // unregister the viewChangeListener
         if (cache != null) cache.removeCacheListener(viewChangeListener);
         try
         {
            queue.clear();
            queue.put(STOP_NOTIFIER);
         }
         catch (InterruptedException ie)
         {
            // do nothing - we're stopping anyway
         }
      }
   }

   @Start(priority = 20)
   @SuppressWarnings("unchecked")
   public void init() throws CacheException
   {
      setupInternals(configuration.getBuddyReplicationConfig());
      if (isEnabled())
      {
         log.debug("Starting BuddyManager");
         dataContainer.registerInternalFqn(BuddyManager.BUDDY_BACKUP_SUBTREE_FQN);
         buddyGroup = new BuddyGroup();
         buddyGroup.setDataOwner(cache.getLocalAddress());
         Address localAddress = rpcManager.getLocalAddress();
         if (localAddress == null)
         {
            if (configuration.getCacheMode() == Configuration.CacheMode.LOCAL)
            {
               log.warn("Buddy replication is enabled but cache mode is LOCAL - not starting BuddyManager!");
               ReflectionUtil.setValue(config, "accessible", true);
               config.setEnabled(false);
               return;
            }
            else
            {
               throw new CacheException("Unable to initialize BuddyManager - the RPCManager has not connected to the cluster and local Address is null!");
            }
         }
         buddyGroup.setGroupName(buddyFqnTransformer.getGroupNameFromAddress(localAddress));

         if (config.getBuddyPoolName() != null)
         {
            buddyPool.put(buddyGroup.getDataOwner(), config.getBuddyPoolName());
         }

         broadcastBuddyPoolMembership();

         cache.getInvocationContext().getOptionOverrides().setSkipCacheStatusCheck(true);
         if (!cache.exists(BUDDY_BACKUP_SUBTREE_FQN))
         {
            // need to get the root DIRECTLY.  cache.getRoot() will pass a call up the interceptor chain and we will
            // have a problem with the cache not being started.

            cache.getInvocationContext().getOptionOverrides().setSkipCacheStatusCheck(true);
            cache.getInvocationContext().getOptionOverrides().setCacheModeLocal(true);
            cache.put(BUDDY_BACKUP_SUBTREE_FQN, (Map) Collections.emptyMap());
         }

         // allow waiting threads to process.
         initialisationLatch.countDown();

         // register a listener to reassign buddies as and when view changes occur
         viewChangeListener = new ViewChangeListener();

         cache.addCacheListener(viewChangeListener);

         // assign buddies based on what we know now
         reassignBuddies(cache.getMembers());
         queue.clear();
         asyncViewChangeHandler.start();

         initFqnTransformer(buddyGroup.getGroupName(), commandsFactory);
      }
   }

   void initFqnTransformer(String groupName, CommandsFactory commandsFactory)
   {
      fqnVisitorFqn2 = new Fqn2BuddyFqnVisitor(groupName, commandsFactory);
      fqnVisitorFqn2.setBuddyFqnTransformer(buddyFqnTransformer);
   }

   public boolean isAutoDataGravitation()
   {
      return config.isAutoDataGravitation();
   }

   public boolean isDataGravitationRemoveOnFind()
   {
      return config.isDataGravitationRemoveOnFind();
   }

   public boolean isDataGravitationSearchBackupTrees()
   {
      return config.isDataGravitationSearchBackupTrees();
   }

   public int getBuddyCommunicationTimeout()
   {
      return config.getBuddyCommunicationTimeout();
   }

   // -------------- methods to be called by the tree cache viewChangeListener --------------------

   static class MembershipChange
   {
      final List<Address> oldMembers;
      final List<Address> newMembers;

      public MembershipChange(List<Address> oldMembers, List<Address> newMembers)
      {
         this.oldMembers = oldMembers;
         this.newMembers = newMembers;
      }

      @Override
      public String toString()
      {
         return "MembershipChange: Old members = " + oldMembers + " New members = " + newMembers;
      }

      /**
       * Returns the list of nodes that were in the old view and are not in the new view, and which are also in the
       * <b>filter</b> param.
       */
      public Set<Address> getDroppedNodes(Collection<Address> filter)
      {
         if (oldMembers == null || oldMembers.isEmpty())
            return Collections.emptySet();
         Set<Address> result = new HashSet<Address>();
         for (Address oldMember : oldMembers)
         {
            if (!newMembers.contains(oldMember) && filter.contains(oldMember))
            {
               result.add(oldMember);
            }
         }
         return result;
      }
   }

   private synchronized void enqueueViewChange(MembershipChange mc)
   {
      // put this on a queue
      try
      {
         if (queue.peek() != STOP_NOTIFIER)
         {
            //first empty the queue.  All queued up view changes that have not been processed yet are now obsolete.
            /* Do not clear the queue here. It might happen that there is an new memebr added there, which must be
             * notified about the pool membership.
             */
            if (trace) log.trace("Enqueueing " + mc + " for async processing");
            queue.put(mc);
         }
      }
      catch (InterruptedException e)
      {
         log.warn("Caught interrupted exception trying to enqueue a view change event", e);
      }
   }

   /**
    * Called by the TreeCacheListener when a
    * view change is detected.  Used to find new buddies if
    * existing buddies have died or if new members to the cluster
    * have been added.  Makes use of the BuddyLocator and then
    * makes RPC calls to remote nodes to assign/remove buddies.
    */
   private void reassignBuddies(List<Address> members) throws CacheException
   {
      List<Address> membership = new ArrayList<Address>(members); // defensive copy

      if (log.isDebugEnabled())
      {
         log.debug("Data owner address " + cache.getLocalAddress());
         log.debug("Entering updateGroup.  Current group: " + buddyGroup + ".  Current View membership: " + membership);
      }
      // some of my buddies have died!
      List<Address> newBuddies = buddyLocator.locateBuddies(buddyPool, membership, buddyGroup.getDataOwner());
      List<Address> unreachableBuddies;
      if (!(unreachableBuddies = checkBuddyStatus(newBuddies)).isEmpty())
      {
         // some of the new buddies are unreachable.  Ditch them, try the algo again.
         membership.removeAll(unreachableBuddies);
         newBuddies = buddyLocator.locateBuddies(buddyPool, membership, buddyGroup.getDataOwner());
      }
      List<Address> uninitialisedBuddies = new ArrayList<Address>();
      List<Address> originalBuddies = buddyGroup.getBuddies();

      for (Address newBuddy : newBuddies)
      {
         if (!originalBuddies.contains(newBuddy))
         {
            uninitialisedBuddies.add(newBuddy);
         }
      }

      List<Address> obsoleteBuddies = new ArrayList<Address>();
      // find obsolete buddies
      for (Address origBuddy : originalBuddies)
      {
         if (!newBuddies.contains(origBuddy))
         {
            obsoleteBuddies.add(origBuddy);
         }
      }

      // Update buddy list
      boolean buddyGroupMutated = !obsoleteBuddies.isEmpty() || !uninitialisedBuddies.isEmpty();

      if (!obsoleteBuddies.isEmpty())
      {
         removeFromGroup(obsoleteBuddies);
      }
      else
      {
         log.trace("No obsolete buddies found, nothing to announce.");
      }

      if (!uninitialisedBuddies.isEmpty())
      {
         addBuddies(newBuddies);
      }
      else
      {
         log.trace("No uninitialized buddies found, nothing to announce.");
      }

      if (buddyGroupMutated)
      {
         if (log.isInfoEnabled()) log.info("Buddy group members have changed. New buddy group: " + buddyGroup);
         configuration.getRuntimeConfig().setBuddyGroup(buddyGroup);
         notifier.notifyBuddyGroupChange(buddyGroup, false);
      }
      else
         log.debug("Nothing has changed; new buddy list is identical to the old one.");

   }

   /**
    * Tests whether all members in the list are valid JGroups members.
    *
    * @param members
    * @return
    */
   private List<Address> checkBuddyStatus(List<Address> members)
   {
      Channel ch = configuration.getRuntimeConfig().getChannel();
      View currentView = ch.getView();
      List<Address> deadBuddies = new LinkedList<Address>();
      for (Address a : members) if (!currentView.containsMember(a)) deadBuddies.add(a);
      return deadBuddies;
   }

   // -------------- methods to be called by the tree cache  --------------------

   /**
    * Called by CacheImpl._remoteAnnounceBuddyPoolName(Address address, String buddyPoolName)
    * when a view change occurs and caches need to inform the cluster of which buddy pool it is in.
    */
   public void handlePoolNameBroadcast(Address address, String poolName)
   {
      if (log.isDebugEnabled())
      {
         log.debug("BuddyManager@" + Integer.toHexString(hashCode()) + ": received announcement that cache instance " + address + " is in buddy pool " + poolName);
      }
      if (poolName != null)
      {
         buddyPool.put(address, poolName);
      }
      else
      {
         synchronized (nullBuddyPool)
         {
            if (!nullBuddyPool.contains(address)) nullBuddyPool.add(address);
         }
      }

      // notify any waiting view change threads that buddy pool info has been received.
      synchronized (poolInfoNotifierLock)
      {
         log.trace("Notifying any waiting view change threads that we have received buddy pool info.");
         receivedBuddyInfo = true;
         poolInfoNotifierLock.notifyAll();
      }
   }

   /**
    * Called by CacheImpl._remoteRemoveFromBuddyGroup(String groupName)
    * when a method call for this is received from a remote cache.
    */
   public void handleRemoveFromBuddyGroup(String groupName) throws BuddyNotInitException
   {
      try
      {
         if (!initialisationLatch.await(0, TimeUnit.NANOSECONDS))
            throw new BuddyNotInitException("Not yet initialised");
      }
      catch (InterruptedException e)
      {
         log.debug("Caught InterruptedException", e);
      }

      if (log.isInfoEnabled()) log.info("Removing self from buddy group " + groupName);

      for (Map.Entry<Address, String> me : buddyPool.entrySet())
      {
         if (me.getValue().equals(groupName))
         {
            if (log.isTraceEnabled()) log.trace("handleRemoveFromBuddyGroup removing " + me.getKey());
            buddyGroupsIParticipateIn.remove(me.getKey());
            break;
         }
      }

      // remove backup data for this group
      if (log.isInfoEnabled()) log.info("Removing backup data for group " + groupName);

      try
      {
         // should be a LOCAL call.
         cache.getInvocationContext().getOptionOverrides().setCacheModeLocal(true);

         cache.removeNode(Fqn.fromRelativeElements(BUDDY_BACKUP_SUBTREE_FQN, groupName));
      }
      catch (CacheException e)
      {
         log.error("Unable to remove backup data for group " + groupName, e);
      }
      finally
      {
         cache.getInvocationContext().getOptionOverrides().setCacheModeLocal(false);
      }
   }

   /**
    * Called by {@link AssignToBuddyGroupCommand} thic command is received from a remote cache.
    *
    * @param newGroup the buddy group
    * @param state    Map<Fqn, byte[]> of any state from the DataOwner. Cannot
    *                 be <code>null</code>.
    */
   public void handleAssignToBuddyGroup(BuddyGroup newGroup, Map<Fqn, byte[]> state) throws Exception
   {
      try
      {
         if (!initialisationLatch.await(0, TimeUnit.NANOSECONDS))
         {
            if (log.isDebugEnabled())
               log.debug("Local buddy mamanger not initialized, rejecting assign call " + newGroup);
            throw new BuddyNotInitException("Not yet initialised");
         }
      }
      catch (InterruptedException e)
      {
         log.debug("Caught InterruptedException", e);
      }

      if (log.isInfoEnabled()) log.info("Assigning self to buddy group " + newGroup);
      buddyGroupsIParticipateIn.put(newGroup.getDataOwner(), newGroup);

      // Integrate state transfer from the data owner of the buddy group
      Fqn integrationBase = buddyFqnTransformer.getBackupRoot(newGroup.getDataOwner());

      if (state.isEmpty())
      {
         if (configuredToFetchState())
            log.info("Data owner has no state to set, even though buddy is configured to accept state.  Assuming there is no data on the data owner.");
         // create the backup region anyway
         Option o = cache.getInvocationContext().getOptionOverrides();
         o.setSkipCacheStatusCheck(true);
         o = cache.getInvocationContext().getOptionOverrides();
         o.setCacheModeLocal(true);
         o.setSkipCacheStatusCheck(true);
         cache.put(Fqn.fromElements(BUDDY_BACKUP_SUBTREE, newGroup.getGroupName()), (Map) Collections.emptyMap());
      }
      else
      {
         for (Map.Entry<Fqn, byte[]> entry : state.entrySet())
         {
            Fqn fqn = entry.getKey();
            if (!regionManager.isInactive(fqn))
            {
               if (trace) log.trace("Integrating state for region " + fqn);
               //ClassLoader cl = (marshaller == null) ? null : marshaller.getClassLoader(fqnS);
               Fqn integrationRoot = Fqn.fromRelativeFqn(integrationBase, fqn);

               byte[] stateBuffer = entry.getValue();
               MarshalledValueInputStream in = null;
               try
               {
                  ByteArrayInputStream bais = new ByteArrayInputStream(stateBuffer);
                  in = new MarshalledValueInputStream(bais);
                  //stateMgr.setState(in, integrationRoot, cl);
                  stateTransferManager.setState(in, integrationRoot);
               }
               catch (Throwable t)
               {
                  if (t instanceof CacheException)
                  {
                     //excepected/common and can happen due to inactive regions and so on
                     log.debug(t);
                  }
                  else
                  {
                     //something has gone wrong
                     log.error("State for fqn " + fqn
                           + " could not be transferred to a buddy at "
                           + cache.getLocalAddress(), t);
                  }
               }
               finally
               {
                  if (in != null)
                  {
                     in.close();
                  }
               }
            }
            else
            {
               log.trace("Received state for region " + fqn + " but region is inactive");
            }
         }
      }
   }

   /**
    * Returns a List<IpAddress> identifying the DataOwner for each buddy
    * group for which this node serves as a backup node.
    */
   public List<Address> getBackupDataOwners()
   {
      List<Address> owners = new ArrayList<Address>();
      for (BuddyGroup group : buddyGroupsIParticipateIn.values())
      {
         owners.add(group.getDataOwner());
      }
      return owners;
   }

   // -------------- static util methods ------------------

   // -------------- methods to be called by the BaseRPCINterceptor --------------------

   /**
    * Returns a list of buddies for which this instance is Data Owner.
    * List excludes self.  Used by the BaseRPCInterceptor when deciding
    * who to replicate to.
    */
   public List<Address> getBuddyAddresses()
   {
      return buddyGroup.getBuddies();
   }

   /**
    * Created as an optimisation for JGroups, which uses vectors.
    *
    * @since 2.2.0
    */
   public Vector<Address> getBuddyAddressesAsVector()
   {
      return buddyGroup.getBuddiesAsVector();
   }

   public List<Address> getMembersOutsideBuddyGroup()
   {
      List<Address> members = new ArrayList<Address>(rpcManager.getMembers());
      members.remove(rpcManager.getLocalAddress());
      members.removeAll(getBuddyAddresses());
      return members;
   }


   /**
    * @see Fqn2BuddyFqnVisitor
    */
   public VisitableCommand transformFqns(VisitableCommand call)
   {
      try
      {
         VisitableCommand transformed = (VisitableCommand) call.acceptVisitor(null, fqnVisitorFqn2);
         if (trace) log.trace("Transformed " + call + " to " + transformed);
         return transformed;
      }
      catch (Throwable throwable)
      {
         log.error("error while transforming an call", throwable);
         throw new CacheException(throwable);
      }
   }

   public ReplicateCommand transformReplicateCommand(ReplicateCommand rc)
   {
      ReplicateCommand clone = rc.copy();
      if (rc.isSingleCommand())
      {
         clone.setSingleModification(transformFqns((VisitableCommand) rc.getSingleModification()));
      }
      else
      {
         List<ReplicableCommand> transformed = new ArrayList<ReplicableCommand>(rc.getModifications().size());
         for (ReplicableCommand cmd : rc.getModifications())
         {
            transformed.add(transformFqns((VisitableCommand) cmd));
         }
         clone.setModifications(transformed);
      }

      return clone;
   }

   // -------------- internal helpers methods --------------------

   private void removeFromGroup(List<Address> buddies)
   {
      if (log.isDebugEnabled())
      {
         log.debug("Removing obsolete buddies from buddy group [" + buddyGroup.getGroupName() + "].  Obsolete buddies are " + buddies);
      }
      buddyGroup.removeBuddies(buddies);
      // now broadcast a message to the removed buddies.
      RemoveFromBuddyGroupCommand command = commandsFactory.buildRemoveFromBuddyGroupCommand(buddyGroup.getGroupName());

      int attemptsLeft = UNINIT_BUDDIES_RETRIES;
      int currentAttempt = 0;

      while (attemptsLeft-- > 0)
      {
         try
         {
            makeRemoteCall(buddies, command);
            break;
         }
         catch (Exception e)
         {
            if (e instanceof BuddyNotInitException || e.getCause() instanceof BuddyNotInitException)
            {
               if (attemptsLeft > 0)
               {
                  log.info("One of the buddies have not been initialised.  Will retry after a short nap.");
                  try
                  {
                     Thread.sleep(UNINIT_BUDDIES_RETRY_NAPTIME[currentAttempt++]);
                  }
                  catch (InterruptedException e1)
                  {
                     // what do we do?
                     log.trace("Thread interrupted while sleeping/waiting for a retry", e1);
                  }
               }
               else
               {
                  throw new BuddyNotInitException("Unable to contact buddy after " + UNINIT_BUDDIES_RETRIES + " retries");
               }
            }
            else
            {
               log.error("Unable to communicate with Buddy for some reason", e);
            }
         }
      }
      log.trace("removeFromGroup notification complete");
   }

   @SuppressWarnings("deprecation")
   private void addBuddies(List<Address> buddies) throws CacheException
   {
      if (log.isDebugEnabled())
      {
         log.debug("Assigning new buddies to buddy group [" + buddyGroup.getGroupName() + "].  New buddies are " + buddies);
      }
      BuddyGroup toBe = new BuddyGroup(buddyGroup.getGroupName(), buddyGroup.getDataOwner());
      toBe.addBuddies(buddies);
      // Create the state transfer map
      Map<Fqn, byte[]> stateMap = new HashMap<Fqn, byte[]>();
      if (configuredToFetchState())
      {
         byte[] state;
         if (configuration.isUseRegionBasedMarshalling())
         {
            Collection<Region> regions = regionManager.getAllRegions(Region.Type.MARSHALLING);
            if (regions.size() > 0)
            {
               for (Region r : regions)
               {
                  Fqn f = r.getFqn();
                  state = acquireState(f);
                  if (state != null) stateMap.put(f, state);
               }
            }
            else if (!configuration.isInactiveOnStartup())
            {
               // No regions defined; try the root
               state = acquireState(Fqn.ROOT);
               if (state != null)
               {
                  stateMap.put(Fqn.ROOT, state);
               }
            }
         }
         else
         {
            state = acquireState(Fqn.ROOT);
            if (state != null)
            {
               stateMap.put(Fqn.ROOT, state);
            }
         }
      }
      else
      {
         if (trace) log.trace("Not configured to provide state!");
      }

      // now broadcast a message to the newly assigned buddies.
      AssignToBuddyGroupCommand membershipCall = commandsFactory.buildAssignToBuddyGroupCommand(toBe, stateMap);

      int attemptsLeft = UNINIT_BUDDIES_RETRIES;
      int currentAttempt = 0;

      while (attemptsLeft-- > 0)
      {
         try
         {
            if (log.isTraceEnabled()) log.trace("Executing assignment call " + membershipCall);
            makeRemoteCall(buddies, membershipCall);
            break;
         }
         catch (Exception e)
         {
            if (e instanceof BuddyNotInitException || e.getCause() instanceof BuddyNotInitException)
            {
               if (attemptsLeft > 0)
               {
                  log.info("One of the buddies have not been initialised.  Will retry after a short nap.");
                  try
                  {
                     Thread.sleep(UNINIT_BUDDIES_RETRY_NAPTIME[currentAttempt++]);
                  }
                  catch (InterruptedException e1)
                  {
                     // what do we do?
                     log.trace("Thread interrupted while sleeping/waiting for a retry", e1);
                  }

               }
               else
               {
                  throw new BuddyNotInitException("Unable to contact buddy after " + UNINIT_BUDDIES_RETRIES + " retries");
               }
            }
            else
            {
               if (attemptsLeft > 0)
               {
                  log.error("Unable to communicate with Buddy for some reason", e);
               }
               else
               {
                  throw new BuddyNotInitException("Unable to contact buddy after " + UNINIT_BUDDIES_RETRIES + " retries");
               }
            }
         }
      }
      buddyGroup.addBuddies(buddies);
      log.trace("addToGroup notification complete");
   }

   private boolean configuredToFetchState()
   {
      return configuration.isFetchInMemoryState() || (cache.getCacheLoaderManager() != null && cache.getCacheLoaderManager().isFetchPersistentState());
   }

   private byte[] acquireState(Fqn fqn) throws CacheException
   {
      // Call _getState with progressively longer timeouts until we
      // get state or it doesn't throw a TimeoutException
      long[] timeouts = {400, 800, 1600, configuration.getStateRetrievalTimeout()};
      TimeoutException timeoutException = null;

      for (int i = 0; i < timeouts.length; i++)
      {
         boolean force = (i == timeouts.length - 1);

         try
         {
            byte[] state = generateState(fqn, timeouts[i], force);
            if (log.isDebugEnabled())
            {
               if (state == null)
                  log.debug("acquireState(): Got null state.  Region is probably empty.");
               else
                  log.debug("acquireState(): Got state");
            }
            return state;
         }
         catch (TimeoutException t)
         {
            timeoutException = t;
            if (trace)
            {
               log.trace("acquireState(): got a TimeoutException");
            }
         }
         catch (Exception e)
         {
            throw new CacheException("Error acquiring state", e);
         }
         catch (Throwable t)
         {
            throw new RuntimeException(t);
         }
      }

      // If we got a timeout exception on the final try,
      // this is a failure condition
      if (timeoutException != null)
      {
         throw new CacheException("acquireState(): Failed getting state due to timeout",
               timeoutException);
      }

      if (log.isDebugEnabled())
      {
         log.debug("acquireState(): Unable to give state");
      }

      return null;
   }

   /**
    * Returns the state for the portion of the cache named by <code>fqn</code>.
    * <p/>
    * State returned is a serialized byte[][], element 0 is the transient state
    * (or null), and element 1 is the persistent state (or null).
    *
    * @param fqn     Fqn indicating the uppermost node in the
    *                portion of the cache whose state should be returned.
    * @param timeout max number of ms this method should wait to acquire
    *                a read lock on the nodes being transferred
    * @param force   if a read lock cannot be acquired after
    *                <code>timeout</code> ms, should the lock acquisition
    *                be forced, and any existing transactions holding locks
    *                on the nodes be rolled back? <strong>NOTE:</strong>
    *                In release 1.2.4, this parameter has no effect.
    * @return a serialized byte[][], element 0 is the transient state
    *         (or null), and element 1 is the persistent state (or null).
    */
   private byte[] generateState(Fqn fqn, long timeout, boolean force) throws Throwable
   {

      MarshalledValueOutputStream out = null;
      byte[] result = null;
      try
      {
         ExposedByteArrayOutputStream baos = new ExposedByteArrayOutputStream(16 * 1024);
         out = new MarshalledValueOutputStream(baos);
         try
         {
            stateTransferManager.getState(out, fqn, timeout, force, false);
         }
         catch (RegionEmptyException ree)
         {
            return null;
         }
         result = baos.getRawBuffer();
      }
      finally
      {
         Util.close(out);
      }

      return result;
   }

   /**
    * Called by the BuddyGroupMembershipMonitor every time a view change occurs.
    */
   private void broadcastBuddyPoolMembership()
   {
      broadcastBuddyPoolMembership(null);
   }

   private void broadcastBuddyPoolMembership(List<Address> recipients)
   {
      // broadcast to other caches
      if (log.isDebugEnabled())
      {
         log.debug("Instance " + buddyGroup.getDataOwner() + " broadcasting membership in buddy pool " + config.getBuddyPoolName() + " to recipients " + recipients);
      }

      AnnounceBuddyPoolNameCommand command = commandsFactory.buildAnnounceBuddyPoolNameCommand(buddyGroup.getDataOwner(), config.getBuddyPoolName());

      try
      {
         makeRemoteCall(recipients, command);
      }
      catch (Exception e)
      {
         log.error("Problems broadcasting buddy pool membership info to cluster", e);
      }
   }

   private void makeRemoteCall(List<Address> recipients, ReplicableCommand call) throws Exception
   {
      // remove non-members from dest list
      if (recipients != null)
      {
         Iterator<Address> recipientsIt = recipients.iterator();
         List<Address> members = cache.getMembers();
         while (recipientsIt.hasNext())
         {
            if (!members.contains(recipientsIt.next()))
            {
               recipientsIt.remove();

            }
         }
      }

      rpcManager.callRemoteMethods(recipients == null ? null : new Vector<Address>(recipients), call, true, config.getBuddyCommunicationTimeout(), false);
   }


   private void migrateDefunctData(NodeSPI backupRoot, Address dataOwner)
   {
      Fqn defunctBackupRootFqn = getDefunctBackupRootFqn(dataOwner);

      if (trace) log.trace("Migrating defunct data.  Backup root is " + backupRoot);
      if (trace) log.trace("Children of backup root are " + backupRoot.getChildren());

      for (Object child : backupRoot.getChildren())
      {
         Fqn childFqn = ((Node) child).getFqn();
         cache.getInvocationContext().getOptionOverrides().setCacheModeLocal(true);
         cache.move(childFqn, defunctBackupRootFqn);
      }

      cache.getInvocationContext().getOptionOverrides().setCacheModeLocal(true);
      backupRoot.getParentDirect().removeChild(backupRoot.getFqn().getLastElement());
   }

   private Fqn getDefunctBackupRootFqn(Address dataOwner)
   {
      // the defunct Fqn should be: /_BUDDY_BACKUP_/dataOwnerAddess:DEAD/N
      // where N is a number.
      Fqn defunctRoot = buddyFqnTransformer.getDeadBackupRoot(dataOwner);
      cache.getInvocationContext().getOptionOverrides().setCacheModeLocal(true);
      cache.getInvocationContext().getOptionOverrides().setSkipCacheStatusCheck(true);
      Node<?, ?> root = cache.getRoot();
      cache.getInvocationContext().getOptionOverrides().setCacheModeLocal(true);
      cache.getInvocationContext().getOptionOverrides().setSkipCacheStatusCheck(true);
      Node<?, ?> defunctRootNode = root.addChild(defunctRoot);
      SortedSet<Object> childrenNames = new TreeSet<Object>(defunctRootNode.getChildrenNames()); // will be naturally sorted.
      Integer childName = 1;

      if (!childrenNames.isEmpty())
      {
         Integer lastChild = (Integer) childrenNames.last();
         childName = lastChild + 1;
      }

      cache.getInvocationContext().getOptionOverrides().setCacheModeLocal(true);
      defunctRootNode.addChild(Fqn.fromElements(childName));
      return Fqn.fromRelativeElements(defunctRoot, childName);
   }


   /**
    * Asynchronous thread that deals with handling view changes placed on a queue
    */
   private class AsyncViewChangeHandlerThread implements Runnable
   {
      private Thread t;
      private boolean isRunning = true;

      public void start()
      {
         if (t == null || !t.isAlive())
         {
            t = new Thread(this);
            t.setName("AsyncViewChangeHandlerThread," + cache.getLocalAddress());
            t.setDaemon(true);
            t.start();
         }
      }

      public void run()
      {
         log.trace("Started");
         while (!Thread.interrupted() && isRunning)
         {
            try
            {
               handleEnqueuedViewChange();
            }
            catch (InterruptedException e)
            {
               break;
            }
            catch (Throwable t)
            {
               // Don't let the thread die
               log.error("Caught exception handling view change", t);
            }
         }
         log.trace("Exiting run()");
      }

      private void handleEnqueuedViewChange() throws Exception
      {
         if (trace) log.trace("Waiting for enqueued view change events");
         MembershipChange members = queue.take();
         if (trace) log.trace("Processing membership change: " + members);
         if (members == STOP_NOTIFIER)
         {
            log.trace("Caught stop notifier, time to go home.");
            // time to go home
            isRunning = false;
            return;
         }

         // there is a strange case where JGroups issues view changes and just includes self in new views, and then
         // quickly corrects it.  Happens intermittently on some unit tests.  If this is such a case, please ignore.
         if (members.newMembers.size() == 1 && members.newMembers.get(0).equals(cache.getLocalAddress()))
         {
            log.info("Ignoring membership change event since it only contains self.");
            return;
         }

         broadcastPoolMembership(members);

         boolean rebroadcast = false;

         // make sure new buddies have broadcast their pool memberships.
         while (!buddyPoolInfoAvailable(members.newMembers))
         {
            rebroadcast = true;
            synchronized (poolInfoNotifierLock)
            {
               log.trace("Not received necessary buddy pool info for all new members yet; waiting on poolInfoNotifierLock.");
               while (!receivedBuddyInfo)
                  poolInfoNotifierLock.wait();
               log.trace("Notified!!");
               receivedBuddyInfo = false;
            }
         }

         if (rebroadcast) broadcastPoolMembership(members);

         // always refresh buddy list.
         reassignBuddies(members.newMembers);

         // look for missing data owners.
         // if the view change involves the removal of a data owner of a group in which we participate in, we should
         // rename the backup the region accordingly, and remove the group from the list in which the current instance participates.
         Set<Address> toRemove = members.getDroppedNodes(buddyGroupsIParticipateIn.keySet());
         if (log.isTraceEnabled()) log.trace("removed members are: " + toRemove);

         for (Address a : toRemove)
         {
            if (log.isTraceEnabled()) log.trace("handleEnqueuedViewChange is removing: " + a);
            BuddyGroup bg = buddyGroupsIParticipateIn.remove(a);
            Fqn backupRootFqn = buddyFqnTransformer.getBackupRoot(bg.getDataOwner());
            NodeSPI backupRoot = cache.getNode(backupRootFqn);
            if (backupRoot != null)
            {
               // could be a race condition where the backup region has been removed because we have been removed
               // from the buddy group, but the buddyGroupsIParticipateIn map hasn't been updated.
               migrateDefunctData(backupRoot, bg.getDataOwner());
            }
         }
      }

      private void broadcastPoolMembership(MembershipChange members)
      {
         log.trace("Broadcasting pool membership details, triggered by view change.");
         if (members.oldMembers == null)
         {
            broadcastBuddyPoolMembership();
         }
         else
         {
            List<Address> delta = new ArrayList<Address>();
            delta.addAll(members.newMembers);
            delta.removeAll(members.oldMembers);
            broadcastBuddyPoolMembership(delta);
         }
      }

      private boolean buddyPoolInfoAvailable(List<Address> newMembers)
      {
         boolean infoReceived = true;
         for (Address address : newMembers)
         {
            // make sure no one is concurrently writing to nullBuddyPool.
            synchronized (nullBuddyPool)
            {
               infoReceived = infoReceived && (address.equals(cache.getLocalAddress()) || buddyPool.keySet().contains(address) || nullBuddyPool.contains(address));
            }
         }

         if (trace)
         {
            log.trace(buddyGroup.getDataOwner() + " received buddy pool info for new members " + newMembers + "?  " + infoReceived);
         }

         return infoReceived;
      }

      public void stop()
      {
         if (t != null) t.interrupt();
      }
   }

   @CacheListener
   public class ViewChangeListener
   {
      private Vector<Address> oldMembers;

      @ViewChanged
      public void handleViewChange(ViewChangedEvent event)
      {
         View newView = event.getNewView();
         if (trace)
            log.trace("BuddyManager CacheListener - got view change with new view " + newView);
         Vector<Address> newMembers = newView.getMembers();

         // the whole 'oldMembers' concept is only used for buddy pool announcements.
         MembershipChange mc = new MembershipChange(oldMembers == null ? null : new Vector<Address>(oldMembers), new Vector<Address>(newMembers));
         enqueueViewChange(mc);
         if (oldMembers == null) oldMembers = new Vector<Address>();
         oldMembers.clear();
         oldMembers.addAll(newMembers);
      }
   }

   @ManagedAttribute(description = "A String representation of the cache's buddy group")
   public String getBuddyGroup()
   {
      return buddyGroup.toString();
   }

   @ManagedAttribute(description = "A String representation of buddy groups the cache participates in")
   public String getBuddyGroupsIParticipateIn()
   {
      return buddyGroupsIParticipateIn.toString();
   }
}