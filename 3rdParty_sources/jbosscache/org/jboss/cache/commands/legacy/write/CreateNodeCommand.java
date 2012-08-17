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
import org.jboss.cache.commands.Visitor;
import org.jboss.cache.commands.WriteCommand;
import org.jboss.cache.commands.legacy.ReversibleCommand;
import org.jboss.cache.commands.read.AbstractDataCommand;
import org.jboss.cache.transaction.GlobalTransaction;

import java.util.LinkedList;
import java.util.List;

/**
 * Command that creates a node.  Primarily to be used as an undo command for removing nodes.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.2.0
 * @deprecated will be removed when pessimistic locking is removed.
 */
@Deprecated
public class CreateNodeCommand extends AbstractDataCommand implements WriteCommand, ReversibleCommand
{
   public static final int METHOD_ID = 48;
   protected final List<Fqn> newlyCreated = new LinkedList<Fqn>();

   public CreateNodeCommand(Fqn fqn)
   {
      this.fqn = fqn;
      newlyCreated.add(fqn);
   }

   public CreateNodeCommand()
   {
   }

   public int getCommandId()
   {
      return METHOD_ID;
   }

   public void setGlobalTransaction(GlobalTransaction gtx)
   {
      // no op
   }

   public GlobalTransaction getGlobalTransaction()
   {
      return null;
   }

   /**
    * Creates a node in the cache, specified by the given Fqn.
    */
   public Object perform(InvocationContext ctx)
   {
      Object[] results = dataContainer.createNodes(fqn);
      List<NodeSPI> created = (List<NodeSPI>) results[0];

      boolean foundFqn = false;
      if (!created.isEmpty())
      {
         for (NodeSPI n : created)
         {
            if (fqn.equals(n.getFqn())) foundFqn = true;
            newlyCreated.add(n.getFqn());
         }
      }
      if (newlyCreated != null && !foundFqn) newlyCreated.remove(fqn);

      return results[1];
   }

   public void rollback()
   {
      if (newlyCreated != null)
      {
         for (Fqn f : newlyCreated) dataContainer.removeFromDataStructure(f, true);
      }
   }

   List<Fqn> getNewlyCreated()
   {
      return newlyCreated;
   }

   public Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable
   {
      return visitor.visitCreateNodeCommand(ctx, this);
   }

   @Override
   public String toString()
   {
      return "CreateNodeCommand{" +
            "fqn=" + fqn +
            ", newlyCreated=" + newlyCreated +
            '}';
   }
}
