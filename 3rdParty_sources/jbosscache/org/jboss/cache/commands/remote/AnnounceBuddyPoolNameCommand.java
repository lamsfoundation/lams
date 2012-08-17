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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.buddyreplication.BuddyManager;
import org.jboss.cache.commands.ReplicableCommand;
import org.jgroups.Address;

/**
 * Announces a buddy pool name to the cluster.  This is not a {@link org.jboss.cache.commands.VisitableCommand} and hence
 * not passed up the {@link org.jboss.cache.interceptors.base.CommandInterceptor} chain.
 * <p/>
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2.0
 */
public class AnnounceBuddyPoolNameCommand implements ReplicableCommand
{
   public static final int METHOD_ID = 28;
   private static final Log log = LogFactory.getLog(AnnounceBuddyPoolNameCommand.class);

   /* dependencies*/
   private BuddyManager buddyManager;

   /*parameters */
   private Address address;
   private String buddyPoolName;

   public AnnounceBuddyPoolNameCommand()
   {
   }

   public AnnounceBuddyPoolNameCommand(Address address, String buddyPoolName)
   {
      this.address = address;
      this.buddyPoolName = buddyPoolName;
   }

   public void initialize(BuddyManager buddyManager)
   {
      this.buddyManager = buddyManager;
   }

   /**
    * This method calls the relevant handler on the buddy manager to deal with this pool broadcast.
    *
    * @param ctx invocation context, ignored.
    * @return null
    * @throws Throwable in the event of problems
    */
   public Object perform(InvocationContext ctx) throws Throwable
   {
      if (buddyManager != null)
         buddyManager.handlePoolNameBroadcast(address, buddyPoolName);
      else if (log.isWarnEnabled())
         log.warn("Received annouceBuddyPoolName call from [" + address + "] but buddy replication is not enabled on this node!");
      return null;
   }

   public int getCommandId()
   {
      return METHOD_ID;
   }

   public Address getAddress()
   {
      return address;
   }

   public String getBuddyPoolName()
   {
      return buddyPoolName;
   }

   public Object[] getParameters()
   {
      return new Object[]{address, buddyPoolName};
   }

   public void setParameters(int commandId, Object[] args)
   {
      address = (Address) args[0];
      buddyPoolName = (String) args[1];
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      AnnounceBuddyPoolNameCommand that = (AnnounceBuddyPoolNameCommand) o;

      if (address != null ? !address.equals(that.address) : that.address != null) return false;
      if (buddyPoolName != null ? !buddyPoolName.equals(that.buddyPoolName) : that.buddyPoolName != null) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result;
      result = (address != null ? address.hashCode() : 0);
      result = 31 * result + (buddyPoolName != null ? buddyPoolName.hashCode() : 0);
      return result;
   }

   @Override
   public String toString()
   {
      return "AnnounceBuddyPoolNameCommand{" +
            "buddyManager=" + buddyManager +
            ", address=" + address +
            ", buddyPoolName='" + buddyPoolName + '\'' +
            '}';
   }
}
