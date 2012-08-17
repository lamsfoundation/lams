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
import org.jboss.cache.commands.Visitor;
import org.jboss.cache.commands.WriteCommand;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jgroups.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * An optimistic version of {@link PrepareCommand}.
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
public class OptimisticPrepareCommand extends PrepareCommand
{
   public static final int METHOD_ID = 18;

   public OptimisticPrepareCommand(GlobalTransaction gtx, List<WriteCommand> modifications, Address address, boolean onePhaseCommit)
   {
      super(gtx, modifications, address, onePhaseCommit);
   }

   public OptimisticPrepareCommand()
   {
   }

   @Override
   public Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable
   {
      return visitor.visitOptimisticPrepareCommand(ctx, this);
   }

   @Override
   public int getCommandId()
   {
      return METHOD_ID;
   }

   @Override
   public Object[] getParameters()
   {
      // the null is needed for wire-level compat with pre-command versions
      return new Object[]{globalTransaction, modifications, null, localAddress, onePhaseCommit};
   }

   /**
    * A shallow copy of all fields except collections.
    *
    * @return a copy of this command
    */
   @Override
   public OptimisticPrepareCommand copy()
   {
      OptimisticPrepareCommand copy = new OptimisticPrepareCommand();
      copy.globalTransaction = globalTransaction;
      copy.localAddress = localAddress;
      copy.modifications = modifications == null ? null : new ArrayList<WriteCommand>(modifications);
      copy.onePhaseCommit = onePhaseCommit;
      return copy;
   }

   @Override
   @SuppressWarnings("unchecked")
   public void setParameters(int commandId, Object[] args)
   {
      globalTransaction = (GlobalTransaction) args[0];
      modifications = (List<WriteCommand>) args[1];
      //args[2] is probably null.
      localAddress = (Address) args[3];
      onePhaseCommit = (Boolean) args[4];
   }

   @Override
   public String toString()
   {
      return "OptimisticPrepareCommand{" +
            "modifications=" + modifications +
            ", localAddress=" + localAddress +
            ", onePhaseCommit=" + onePhaseCommit +
            ", globalTransaction = " + globalTransaction +
            '}';
   }
}
