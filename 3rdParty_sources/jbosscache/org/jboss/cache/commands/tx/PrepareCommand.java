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
package org.jboss.cache.commands.tx;

import org.jboss.cache.InvocationContext;
import org.jboss.cache.commands.ReplicableCommand;
import org.jboss.cache.commands.Visitor;
import org.jboss.cache.commands.WriteCommand;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jgroups.Address;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The prepare phase of a 2-phase commit, or the single prepare/commit phase of a single-phase commit.
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
public class PrepareCommand extends AbstractTransactionCommand
{
   public static final int METHOD_ID = 10;

   protected List<WriteCommand> modifications;
   protected Address localAddress;
   protected boolean onePhaseCommit;

   public PrepareCommand(GlobalTransaction gtx, List<WriteCommand> modifications, Address localAddress, boolean onePhaseCommit)
   {
      this.globalTransaction = gtx;
      this.modifications = modifications;
      this.localAddress = localAddress;
      this.onePhaseCommit = onePhaseCommit;
   }

   public void removeModifications(Collection<WriteCommand> modificationsToRemove)
   {
      if (modifications != null) modifications.removeAll(modificationsToRemove);
   }

   public PrepareCommand()
   {
   }

   public Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable
   {
      return visitor.visitPrepareCommand(ctx, this);
   }

   public List<WriteCommand> getModifications()
   {
      return modifications;
   }

   public Address getLocalAddress()
   {
      return localAddress;
   }

   public boolean isOnePhaseCommit()
   {
      return onePhaseCommit;
   }

   public boolean existModifications()
   {
      return modifications != null && modifications.size() > 0;
   }

   public int getModificationsCount()
   {
      return modifications != null ? modifications.size() : 0;
   }

   public int getCommandId()
   {
      return METHOD_ID;
   }

   @Override
   public Object[] getParameters()
   {
      return new Object[]{globalTransaction, modifications, localAddress, onePhaseCommit};
   }

   @Override
   @SuppressWarnings("unchecked")
   public void setParameters(int commandId, Object[] args)
   {
      globalTransaction = (GlobalTransaction) args[0];
      modifications = (List<WriteCommand>) args[1];
      localAddress = (Address) args[2];
      onePhaseCommit = (Boolean) args[3];
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      if (!super.equals(o)) return false;

      PrepareCommand that = (PrepareCommand) o;

      if (onePhaseCommit != that.onePhaseCommit) return false;
      if (localAddress != null ? !localAddress.equals(that.localAddress) : that.localAddress != null) return false;
      if (modifications != null ? !modifications.equals(that.modifications) : that.modifications != null) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (modifications != null ? modifications.hashCode() : 0);
      result = 31 * result + (localAddress != null ? localAddress.hashCode() : 0);
      result = 31 * result + (onePhaseCommit ? 1 : 0);
      return result;
   }

   public PrepareCommand copy()
   {
      PrepareCommand copy = new PrepareCommand();
      copy.globalTransaction = globalTransaction;
      copy.localAddress = localAddress;
      copy.modifications = modifications == null ? null : new ArrayList<WriteCommand>(modifications);
      copy.onePhaseCommit = onePhaseCommit;
      return copy;
   }

   @Override
   public String toString()
   {
      return "PrepareCommand{" +
            "globalTransaction=" + globalTransaction +
            ", modifications=" + modifications +
            ", localAddress=" + localAddress +
            ", onePhaseCommit=" + onePhaseCommit +
            '}';
   }

   public boolean containsModificationType(Class<? extends ReplicableCommand> replicableCommandClass)
   {
      for (ReplicableCommand mod : getModifications())
      {
         if (mod.getClass().equals(replicableCommandClass))
         {
            return true;
         }
      }
      return false;
   }
}
