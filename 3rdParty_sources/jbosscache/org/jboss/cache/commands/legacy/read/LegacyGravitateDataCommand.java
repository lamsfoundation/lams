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
package org.jboss.cache.commands.legacy.read;

import org.jboss.cache.Fqn;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.buddyreplication.BuddyManager;
import org.jboss.cache.commands.read.GravitateDataCommand;
import org.jgroups.Address;

import java.util.Set;

/**
 * Legacy version that uses old data container peeks
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class LegacyGravitateDataCommand extends GravitateDataCommand
{
   public LegacyGravitateDataCommand(Fqn fqn, boolean searchSubtrees, Address localAddress)
   {
      super(fqn, searchSubtrees, localAddress);
   }

   public LegacyGravitateDataCommand(Address localAddress)
   {
      super(localAddress);
   }

   /**
    * @return a Set of child node names that hang directly off the backup tree root, or null if the backup tree root doesn't exist.
    */
   @Override
   @SuppressWarnings("unchecked")
   protected Set<Object> getBackupRoots()
   {
      NodeSPI backupSubtree = dataContainer.peek(BuddyManager.BUDDY_BACKUP_SUBTREE_FQN);
      if (backupSubtree == null) return null;
      return backupSubtree.getChildrenNamesDirect();
   }

   void setSearchSubtrees(boolean searchSubtrees)
   {
      this.searchSubtrees = searchSubtrees;
   }
}
