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
package org.jboss.cache.commands.legacy.write;

import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.legacy.ReversibleCommand;
import org.jboss.cache.commands.write.ClearDataCommand;
import org.jboss.cache.transaction.GlobalTransaction;

import java.util.HashMap;

/**
 * A version of {@link org.jboss.cache.commands.write.ClearDataCommand} which can be rolled back, for use with
 * pessimistic locking where changes are made directly on the data structures and may need to be reversed.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class PessClearDataCommand extends ClearDataCommand implements ReversibleCommand
{
   private HashMap originalData;

   public PessClearDataCommand(GlobalTransaction gtx, Fqn fqn)
   {
      super(gtx, fqn);
   }

   public PessClearDataCommand()
   {
   }

   @Override
   public Object perform(InvocationContext ctx)
   {
      if (globalTransaction != null)
      {
         NodeSPI n = ctx.lookUpNode(fqn);
         originalData = n == null ? null : new HashMap(n.getDataDirect());
      }
      return super.perform(ctx);
   }

   public void rollback()
   {
      if (trace) log.trace("rollback(" + globalTransaction + ", \"" + fqn + "\", " + originalData + ")");
      if (originalData != null && !originalData.isEmpty())
      {
         NodeSPI nodeSpi = dataContainer.peek(fqn, false, true);
         if (nodeSpi == null)
         {
            if (trace)
               log.trace("Not rolling back node clearance for node: " + fqn + " as it does not exist in the cache. " +
                     "This might be the result of an NoOp clear operation");
            return;
         }
         nodeSpi.putAllDirect(originalData);
      }
   }
}
