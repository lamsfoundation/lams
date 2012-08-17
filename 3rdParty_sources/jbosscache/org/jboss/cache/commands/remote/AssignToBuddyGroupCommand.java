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

import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.buddyreplication.BuddyGroup;
import org.jboss.cache.buddyreplication.BuddyManager;
import org.jboss.cache.commands.ReplicableCommand;

import java.util.Arrays;
import java.util.Map;

/**
 * Assigns a buddy to a group.  This is not a {@link org.jboss.cache.commands.VisitableCommand} and hence
 * not passed up the {@link org.jboss.cache.interceptors.base.CommandInterceptor} chain.
 * <p/>
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2.0
 */
public class AssignToBuddyGroupCommand implements ReplicableCommand
{
   public static final int METHOD_ID = 29;

   /* dependencies */
   private BuddyManager buddyManager;

   /* parameters */
   private BuddyGroup group;
   private Map<Fqn, byte[]> state;

   public AssignToBuddyGroupCommand(BuddyGroup group, Map<Fqn, byte[]> state)
   {
      this.group = group;
      this.state = state;
   }

   public AssignToBuddyGroupCommand()
   {
   }

   public void initialize(BuddyManager manager)
   {
      this.buddyManager = manager;
   }

   /**
    * This method calls the relevant handler on the buddy manager to deal with being assigned to a buddy group
    *
    * @param ctx invocation context, ignored.
    * @return null
    * @throws Throwable in the event of problems
    */
   public Object perform(InvocationContext ctx) throws Throwable
   {
      if (buddyManager != null)
         buddyManager.handleAssignToBuddyGroup(group, state);
      return null;
   }

   public int getCommandId()
   {
      return METHOD_ID;
   }

   public BuddyGroup getGroup()
   {
      return group;
   }

   public Map<Fqn, byte[]> getState()
   {
      return state;
   }

   public Object[] getParameters()
   {
      return new Object[]{group, state};
   }

   @SuppressWarnings("unchecked")
   public void setParameters(int commandId, Object[] args)
   {
      group = (BuddyGroup) args[0];
      state = (Map<Fqn, byte[]>) args[1];
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      AssignToBuddyGroupCommand that = (AssignToBuddyGroupCommand) o;

      if (group != null ? !group.equals(that.group) : that.group != null) return false;
      if (state != null ? !state.equals(that.state) : that.state != null) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result;
      result = (group != null ? group.hashCode() : 0);
      result = 31 * result + (state != null ? state.hashCode() : 0);
      return result;
   }

   @Override
   public String toString()
   {
      return "AssignToBuddyGroupCommand{" +
            "buddyManager=" + buddyManager +
            ", group=" + group +
            ", state=" + (state == null ? null : Arrays.asList(state)) +
            '}';
   }
}
