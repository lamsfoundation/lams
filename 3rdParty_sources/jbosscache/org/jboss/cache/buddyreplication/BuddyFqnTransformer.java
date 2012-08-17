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

import org.jboss.cache.CacheException;
import org.jboss.cache.Fqn;
import org.jgroups.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * Knows how to transform between fqn and buddy-formated fqns.
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
public class BuddyFqnTransformer
{
   public static final String BUDDY_BACKUP_SUBTREE = BuddyManager.BUDDY_BACKUP_SUBTREE;
   public static final Fqn BUDDY_BACKUP_SUBTREE_FQN = BuddyManager.BUDDY_BACKUP_SUBTREE_FQN;

   /**
    * Utility method that retrieves a buddy backup Fqn given the actual Fqn of some data and the data owner's Address.
    *
    * @param dataOwnerAddress the JGroups {@link org.jgroups.Address}  of the data owner
    * @param origFqn          the original Fqn
    * @return a backup Fqn
    */
   public Fqn getBackupFqn(Address dataOwnerAddress, Fqn origFqn)
   {
      return getBackupFqn(getGroupNameFromAddress(dataOwnerAddress), origFqn);
   }

   /**
    * Utility method that retrieves a buddy backup Fqn given the actual Fqn of some data and the buddy group name.
    *
    * @param buddyGroupName the buddy group name
    * @param origFqn        the original Fqn
    * @return a backup Fqn
    */
   public Fqn getBackupFqn(String buddyGroupName, Fqn origFqn)
   {
      if (isBackupFqn(origFqn))
         throw new CacheException("Cannot make a backup Fqn from a backup Fqn! Attempting to create a backup of " + origFqn);

      List<Object> elements = new ArrayList<Object>(origFqn.size() + 2);
      elements.add(BuddyManager.BUDDY_BACKUP_SUBTREE);
      elements.add(buddyGroupName);
      elements.addAll(origFqn.peekElements());

      return Fqn.fromList(elements, true);
   }

   /**
    * Utility method that retrieves a buddy backup Fqn given the actual Fqn of some data and the backup subtree for the
    * buddy group in question
    *
    * @param buddyGroupRoot the subtree under which data for a particular buddy is backed up
    * @param origFqn        the original Fqn
    * @return a backup Fqn
    */
   public Fqn getBackupFqn(Fqn buddyGroupRoot, Fqn origFqn)
   {
      if (isBackupFqn(origFqn))
         throw new CacheException("Cannot make a backup Fqn from a backup Fqn! Attempting to create a backup of " + origFqn);

      List<Object> elements = new ArrayList<Object>(origFqn.size() + 2);
      elements.add(BuddyManager.BUDDY_BACKUP_SUBTREE);
      elements.add(buddyGroupRoot.get(1));
      elements.addAll(origFqn.peekElements());

      return Fqn.fromList(elements, true);
   }

   public static boolean isBackupFqn(Fqn name)
   {
      return name != null && name.hasElement(BuddyManager.BUDDY_BACKUP_SUBTREE);
   }

   public Fqn getActualFqn(Fqn fqn)
   {
      if (!isBackupFqn(fqn)) return fqn;
      if (fqn.equals(BUDDY_BACKUP_SUBTREE_FQN)) return Fqn.ROOT;
      // remove the first 2 (or 3 in the case of a dead backup region) elements
      boolean isDead = isDeadBackupFqn(fqn);
      int fqnSz = fqn.size();
      if (isDead && fqnSz == 2) return Fqn.ROOT;
      return fqn.getSubFqn(isDead ? 3 : 2, fqnSz);
   }

   /**
    * Tests whether a given Fqn belongs to a dead backup region.
    *
    * @param name fqn to test
    * @return true if the fqn is a part of a dead backup region; false otherwise.
    */
   public boolean isDeadBackupFqn(Fqn name)
   {
      if (name == null || name.size() < 2) return false;
      Object elem1 = name.get(1);
      if (elem1 instanceof String)
      {
         String strElem1 = (String) elem1;
         return name.hasElement(BuddyManager.BUDDY_BACKUP_SUBTREE) && strElem1.endsWith(":DEAD");
      }
      else
      {
         return false;
      }
   }

   /**
    * @param dataOwner owner of a data set
    * @return a backup root for a given data owner
    */
   public Fqn getBackupRoot(Address dataOwner)
   {
      return Fqn.fromRelativeElements(BUDDY_BACKUP_SUBTREE_FQN, getGroupNameFromAddress(dataOwner));
   }

   /**
    * Returns the backup root of a dead data owner
    *
    * @param dataOwner owner of data
    * @return Fqn of dead data owner's root
    */
   public Fqn getDeadBackupRoot(Address dataOwner)
   {
      return Fqn.fromRelativeElements(BUDDY_BACKUP_SUBTREE_FQN, getGroupNameFromAddress(dataOwner) + ":DEAD");
   }

   public boolean isDeadBackupRoot(Fqn f)
   {
      return f.isDirectChildOf(BUDDY_BACKUP_SUBTREE_FQN) && f.getLastElementAsString().endsWith(":DEAD");
   }

   public String getGroupNameFromAddress(Address address)
   {
      return address.toString().replace(':', '_');
   }

   /**
    * Returns the buddy backp root portion of a given Fqn, provided it is a backup Fqn.  If it is not a backup Fqn, Fqn.ROOT is returned.
    *
    * @param fqn fqn
    * @return a backup root from an Fqn
    */
   public Fqn getBackupRootFromFqn(Fqn fqn)
   {
      if (isBackupFqn(fqn) && fqn.size() > 1)
      {
         return fqn.getSubFqn(0, isDeadBackupFqn(fqn) ? 3 : 2);
      }
      else
      {
         return Fqn.root();
      }
   }
}
