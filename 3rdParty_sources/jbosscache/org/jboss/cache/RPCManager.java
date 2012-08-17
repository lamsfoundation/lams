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

import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jboss.cache.RPCManagerImpl.FlushTracker;
import org.jboss.cache.commands.ReplicableCommand;
import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.blocks.RspFilter;

/**
 * Provides a mechanism for communicating with other caches in the cluster.  For now this is based on JGroups as an underlying
 * transport, and in future more transport options may become available.
 * <p/>
 * Implementations have a simple lifecycle:
 * <ul>
 * <li>start() - starts the underlying channel based on configuration options injected, and connects the channel</li>
 * <li>disconnect() - disconnects the channel</li>
 * <li>stop() - stops the dispatcher and releases resources</li>
 * </ul>
 *
 * @author Manik Surtani
 * @since 2.1.0
 */
public interface RPCManager
{
   /**
    * Disconnects and closes the underlying JGroups channel.
    */
   void disconnect();

   /**
    * Stops the RPCDispatcher and frees resources.  Closes and disconnects the underlying JGroups channel if this is
    * still open/connected.
    */
   void stop();

   /**
    * Starts the RPCManager by connecting the underlying JGroups channel (if configured for replication).  Connecting
    * the channel may also involve state transfer (if configured) so the interceptor chain should be started and
    * available before this method is called.
    */
   void start();

   /**
    * Invokes an RPC call on other caches in the cluster.
    *
    * @param recipients          a list of Addresses to invoke the call on.  If this is null, the call is broadcast to the entire cluster.
    * @param cacheCommand        the cache command to invoke
    * @param mode                the group request mode to use.  See {@link org.jgroups.blocks.GroupRequest}.
    * @param timeout             a timeout after which to throw a replication exception.
    * @param responseFilter      a response filter with which to filter out failed/unwanted/invalid responses.
    * @param useOutOfBandMessage if true, the message is put on JGroups' OOB queue.  See JGroups docs for more info.
    * @return a list of responses from each member contacted.
    * @throws Exception in the event of problems.
    */
   List<Object> callRemoteMethods(Vector<Address> recipients, ReplicableCommand cacheCommand, int mode, long timeout, RspFilter responseFilter, boolean useOutOfBandMessage) throws Exception;

   /**
    * Invokes an RPC call on other caches in the cluster.
    *
    * @param recipients          a list of Addresses to invoke the call on.  If this is null, the call is broadcast to the entire cluster.
    * @param cacheCommand        the cache command to invoke
    * @param mode                the group request mode to use.  See {@link org.jgroups.blocks.GroupRequest}.
    * @param timeout             a timeout after which to throw a replication exception.
    * @param useOutOfBandMessage if true, the message is put on JGroups' OOB queue.  See JGroups docs for more info.
    * @return a list of responses from each member contacted.
    * @throws Exception in the event of problems.
    */
   List<Object> callRemoteMethods(Vector<Address> recipients, ReplicableCommand cacheCommand, int mode, long timeout, boolean useOutOfBandMessage) throws Exception;

   /**
    * Invokes an RPC call on other caches in the cluster.
    *
    * @param recipients          a list of Addresses to invoke the call on.  If this is null, the call is broadcast to the entire cluster.
    * @param cacheCommand        the cache command to invoke
    * @param synchronous         if true, sets group request mode to {@link org.jgroups.blocks.GroupRequest#GET_ALL}, and if false sets it to {@link org.jgroups.blocks.GroupRequest#GET_NONE}.
    * @param timeout             a timeout after which to throw a replication exception.
    * @param useOutOfBandMessage if true, the message is put on JGroups' OOB queue.  See JGroups docs for more info.
    * @return a list of responses from each member contacted.
    * @throws Exception in the event of problems.
    */
   List<Object> callRemoteMethods(Vector<Address> recipients, ReplicableCommand cacheCommand, boolean synchronous, long timeout, boolean useOutOfBandMessage) throws Exception;

   /**
    * @return true if the current Channel is the coordinator of the cluster.
    */
   boolean isCoordinator();

   /**
    * @return the Address of the current coordinator.
    */
   Address getCoordinator();

   /**
    * Retrieves the local JGroups channel's address
    *
    * @return an Address
    */
   Address getLocalAddress();

   /**
    * Returns a defensively copied list of  members in the current cluster view.
    */
   List<Address> getMembers();

   /**
    * Retrieves partial state from remote instances.
    *
    * @param sources           sources to consider for a state transfer
    * @param sourceTarget      Fqn on source to retrieve state for
    * @param integrationTarget integration point on local cache to apply state
    * @throws Exception in the event of problems
    */
   void fetchPartialState(List<Address> sources, Fqn sourceTarget, Fqn integrationTarget) throws Exception;

   /**
    * Retrieves partial state from remote instances.
    *
    * @param sources sources to consider for a state transfer
    * @param subtree Fqn subtree to retrieve.  Will be integrated at the same point.
    * @throws Exception in the event of problems
    */
   void fetchPartialState(List<Address> sources, Fqn subtree) throws Exception;

   /**
    * Retrieves the Channel
    *
    * @return a channel
    */
   Channel getChannel();

   /**
    * Returns the last state transfer source address.
    *
    * @return the last state transfer source address
    */
   public Address getLastStateTransferSource();

   /**
    * Returns the flush tracker associated with this manager.
    *
    * @return the current flush tracker
    */
   public FlushTracker getFlushTracker();
}
