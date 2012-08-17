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
import org.jboss.cache.util.Immutables;
import org.jgroups.Address;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Value object that represents a buddy group
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 */
@ThreadSafe
public class BuddyGroup implements Serializable
{
   /**
    * Serial version.
    */
   private static final long serialVersionUID = 5391883716108410301L;

   private String groupName;

   private Address dataOwner;

   private Date lastModified = new Date();

   /**
    * List<Address> - a list of JGroups addresses
    */
   private final Vector<Address> buddies = new Vector<Address>();

   public BuddyGroup()
   {
   }

   public BuddyGroup(String groupName, Address dataOwner)
   {
      this.groupName = groupName;
      this.dataOwner = dataOwner;
   }

   public String getGroupName()
   {
      return groupName;
   }

   protected void setGroupName(String groupName)
   {
      this.groupName = groupName;
      lastModified = new Date();
   }

   public Address getDataOwner()
   {
      return dataOwner;
   }

   protected void setDataOwner(Address dataOwner)
   {
      this.dataOwner = dataOwner;
      lastModified = new Date();
   }

   public List<Address> getBuddies()
   {
      // defensive copy and immutable.
      return Immutables.immutableListCopy(buddies);
   }

   protected void addBuddies(Collection<Address> buddies)
   {
      this.buddies.addAll(buddies);
      lastModified = new Date();
   }

   protected void removeBuddies(Collection<Address> buddies)
   {
      this.buddies.removeAll(buddies);
      lastModified = new Date();
   }

   public Date getLastModified()
   {
      return lastModified;
   }

   @Override
   public String toString()
   {
      StringBuilder b = new StringBuilder("BuddyGroup: (");
      b.append("dataOwner: ").append(dataOwner).append(", ");
      b.append("groupName: ").append(groupName).append(", ");
      b.append("buddies: ").append(buddies).append(",");
      b.append("lastModified: ").append(lastModified).append(")");
      return b.toString();
   }

   /**
    * Added in 2.2.0 as an optimisation for JGroups which internally uses vectors.
    *
    * @return a list of buddies
    * @since 2.2.0
    */
   public Vector<Address> getBuddiesAsVector()
   {
      return buddies;
   }
}
