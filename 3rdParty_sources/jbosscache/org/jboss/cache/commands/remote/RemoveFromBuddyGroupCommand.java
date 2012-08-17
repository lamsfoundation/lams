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
package org.jboss.cache.commands.remote;

import org.jboss.cache.InvocationContext;
import org.jboss.cache.buddyreplication.BuddyManager;
import org.jboss.cache.commands.ReplicableCommand;

/**
 * Removes a buddy from a group.  This is not a {@link org.jboss.cache.commands.VisitableCommand} and hence
 * not passed up the {@link org.jboss.cache.interceptors.base.CommandInterceptor} chain.
 * <p/>
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
public class RemoveFromBuddyGroupCommand implements ReplicableCommand
{
   public static final int METHOD_ID = 30;

   private BuddyManager buddyManager;

   private String groupName;

   public RemoveFromBuddyGroupCommand(String groupName)
   {
      this.groupName = groupName;
   }

   public RemoveFromBuddyGroupCommand()
   {
   }

   public void initialize(BuddyManager buddyManager)
   {
      this.buddyManager = buddyManager;
   }

   /**
    * This method calls the relevant handler on the buddy manager to deal with being removed from a buddy group
    *
    * @param ctx invocation context, ignored.
    * @return null
    */
   public Object perform(InvocationContext ctx)
   {
      if (buddyManager != null)
         buddyManager.handleRemoveFromBuddyGroup(groupName);
      return null;
   }

   public int getCommandId()
   {
      return METHOD_ID;
   }

   public String getGroupName()
   {
      return groupName;
   }

   public Object[] getParameters()
   {
      return new Object[]{groupName};
   }

   public void setParameters(int commandId, Object[] args)
   {
      groupName = (String) args[0];
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      RemoveFromBuddyGroupCommand that = (RemoveFromBuddyGroupCommand) o;

      if (groupName != null ? !groupName.equals(that.groupName) : that.groupName != null) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      return (groupName != null ? groupName.hashCode() : 0);
   }

   @Override
   public String toString()
   {
      return "RemoveFromBuddyGroupCommand{" +
            "buddyManager=" + buddyManager +
            ", groupName='" + groupName + '\'' +
            '}';
   }
}
