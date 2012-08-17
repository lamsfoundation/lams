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

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.config.BuddyReplicationConfig.BuddyLocatorConfig;
import org.jgroups.Address;
import org.jgroups.stack.IpAddress;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * This buddy locator uses a next-in-line algorithm to select buddies for a buddy group.
 * This algorithm allows for the following properties, all of which are optional.
 * <p/>
 * <ul>
 * <li>More than one buddy per group - the <b>numBuddies</b> property, defaulting to 1 if ommitted.</li>
 * <li>Ability to skip buddies on the same host when selecting buddies - the <b>ignoreColocatedBuddies</b>
 * property, defaulting to true if ommitted.  Note that this is just a hint though, and if all nstances in
 * a cluster are colocated, the algorithm will be forced to pick a colocated instance even if this is property
 * set to true.</li>
 * </ul>
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 */
@ThreadSafe
public class NextMemberBuddyLocator implements BuddyLocator
{
   private final Log log = LogFactory.getLog(NextMemberBuddyLocator.class);

   private NextMemberBuddyLocatorConfig config = new NextMemberBuddyLocatorConfig();

   public BuddyLocatorConfig getConfig()
   {
      return config;
   }

   public void init(BuddyLocatorConfig buddyLocatorConfig)
   {
      if (buddyLocatorConfig instanceof NextMemberBuddyLocatorConfig)
      {
         this.config = (NextMemberBuddyLocatorConfig) buddyLocatorConfig;
      }
      else if (buddyLocatorConfig != null)
      {
         this.config = new NextMemberBuddyLocatorConfig(buddyLocatorConfig);
      }
      else
      {
         // We were passed null; just use a default config
         this.config = new NextMemberBuddyLocatorConfig();
      }
   }

   public List<Address> locateBuddies(Map<Address, String> buddyPoolMap, List<Address> currentMembership, Address dataOwner)
   {
      int numBuddiesToFind = Math.min(config.getNumBuddies(), currentMembership.size());
      List<Address> buddies = new ArrayList<Address>(numBuddiesToFind);

      // find where we are in the list.
      int dataOwnerSubscript = currentMembership.indexOf(dataOwner);
      int i = 0;
      boolean ignoreColocatedBuddiesForSession = config.isIgnoreColocatedBuddies();


      while (buddies.size() < numBuddiesToFind)
      {
         int subscript = i + dataOwnerSubscript + 1;
         // make sure we loop around the list
         if (subscript >= currentMembership.size()) subscript = subscript - currentMembership.size();

         // now if subscript is STILL greater than or equal to the current membership size, we've looped around
         // completely and still havent found any more suitable candidates.  Try with colocation hint disabled.
         if (subscript >= currentMembership.size() && ignoreColocatedBuddiesForSession)
         {
            ignoreColocatedBuddiesForSession = false;
            i = 0;
            if (log.isInfoEnabled())
            {
               log.info("Expected to look for " + numBuddiesToFind + " buddies but could only find " + buddies.size() + " suitable candidates - trying with colocated buddies as well.");
            }

            continue;
         }

         // now try disabling the buddy pool
         if (subscript >= currentMembership.size() && buddyPoolMap != null)
         {
            buddyPoolMap = null;
            ignoreColocatedBuddiesForSession = config.isIgnoreColocatedBuddies();// reset this flag
            i = 0;
            if (log.isInfoEnabled())
            {
               log.info("Expected to look for " + numBuddiesToFind + " buddies but could only find " + buddies.size() + " suitable candidates - trying again, ignoring buddy pool hints.");
            }
            continue;
         }

         // now if subscript is STILL greater than or equal to the current membership size, we've looped around
         // completely and still havent found any more suitable candidates.  Give up with however many we have.
         if (subscript >= currentMembership.size())
         {
            if (log.isInfoEnabled())
            {
               log.info("Expected to look for " + numBuddiesToFind + " buddies but could only find " + buddies.size() + " suitable candidates!");
            }
            break;
         }

         Address candidate = currentMembership.get(subscript);
         if (
               !candidate.equals(dataOwner) && // ignore self from selection as buddy
                     !buddies.contains(candidate) && // havent already considered this candidate
                     (!ignoreColocatedBuddiesForSession || !isColocated(candidate, dataOwner)) && // ignore colocated buddies
                     (isInSameBuddyPool(buddyPoolMap, candidate, dataOwner))// try and find buddies in the same buddy pool first
               )
         {
            buddies.add(candidate);
         }
         i++;
      }

      if (log.isTraceEnabled()) log.trace("Selected buddy group as " + buddies);
      return buddies;
   }

   private boolean isInSameBuddyPool(Map<Address, String> buddyPoolMap, Address candidate, Address dataOwner)
   {
      if (buddyPoolMap == null) return true;
      Object ownerPoolName = buddyPoolMap.get(dataOwner);
      Object candidatePoolName = buddyPoolMap.get(candidate);
      return !(ownerPoolName == null || candidatePoolName == null) && ownerPoolName.equals(candidatePoolName);
   }

   private boolean isColocated(Address candidate, Address dataOwner)
   {
      // assume they're both IpAddresses??
      InetAddress inetC = ((IpAddress) candidate).getIpAddress();
      InetAddress inetD = ((IpAddress) dataOwner).getIpAddress();

      if (inetC.equals(inetD)) return true;

      // now check other interfaces.
      try
      {
         for (Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces(); nics.hasMoreElements();)
         {
            NetworkInterface i = nics.nextElement();
            for (Enumeration<InetAddress> addrs = i.getInetAddresses(); addrs.hasMoreElements();)
            {
               InetAddress addr = addrs.nextElement();
               if (addr.equals(inetC)) return true;
            }
         }
      }
      catch (SocketException e)
      {
         if (log.isDebugEnabled()) log.debug("Unable to read NICs on host", e);
         if (log.isWarnEnabled())
         {
            log.warn("UNable to read all network interfaces on host " + inetD + " to determine colocation of " + inetC + ".  Assuming " + inetC + " is NOT colocated with " + inetD);
         }
      }

      return false;
   }
}