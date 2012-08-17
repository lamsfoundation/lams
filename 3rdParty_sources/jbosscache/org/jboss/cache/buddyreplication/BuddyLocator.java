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

import org.jboss.cache.config.BuddyReplicationConfig;
import org.jboss.cache.config.BuddyReplicationConfig.BuddyLocatorConfig;
import org.jgroups.Address;

import java.util.List;
import java.util.Map;

/**
 * Buddy Locators help the {@link org.jboss.cache.buddyreplication.BuddyManager} select buddies for its buddy group.
 * <p/>
 * Implementations of this class must declare a public no-arguments constructor.
 * </p>
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @since 1.4.0
 */
public interface BuddyLocator
{
   /**
    * Gets the configuration for this BuddyLocator.
    *
    * @return object encapsulating this object's configuration.
    *         Should not return <code>null</code>.  If {@link #init(org.jboss.cache.config.BuddyReplicationConfig.BuddyLocatorConfig)}
    *         has not been called or <code>null</code> was passed to it, the
    *         returned value should be the default config for the
    *         given BuddyLocator implementation.
    */
   BuddyLocatorConfig getConfig();

   /**
    * Initialize this <code>BuddyLocator</code>.
    *
    * @param config configuration for this <code>BuddyLocator</code>. May be
    *               <code>null</code>, in which case the implementation should
    *               use its default configuration.
    */
   void init(BuddyReplicationConfig.BuddyLocatorConfig config);

   /**
    * Choose a set of buddies for the given node.  Invoked when a change in
    * cluster membership is detected.
    *
    * @param buddyPoolMap      Map<Address, String> mapping nodes in the cluster to
    *                          the "buddy pool" they have identified themselves as
    *                          belonging too.  A BuddyLocator implementation can use
    *                          this information to preferentially assign buddies from
    *                          the same buddy pool as <code>dataOwner</code>.  May be
    *                          <code>null</code> if buddy pools aren't configured.
    * @param currentMembership List<Address> of the current cluster members
    * @param dataOwner         Address of the node for which buddies should be selected
    * @return List<Address> of the nodes that should serve as buddies for
    *         <code>dataOwner</code>. Will not be <code>null</code>, may
    *         be empty.
    */
   List<Address> locateBuddies(Map<Address, String> buddyPoolMap, List<Address> currentMembership, Address dataOwner);
}
