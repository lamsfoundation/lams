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
package org.jboss.cache.commands.write;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.Visitor;
import org.jboss.cache.commands.read.AbstractDataCommand;
import org.jboss.cache.notifications.Notifier;

/**
 * Removes a node's content from memory - never removes the node.
 * It also clenups data for resident nodes - which are not being touched by eviction.
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
public class InvalidateCommand extends AbstractDataCommand
{
   public static final int METHOD_ID = 47;
   private static final Log log = LogFactory.getLog(InvalidateCommand.class);
   private static final boolean trace = log.isTraceEnabled();

   /* dependencies*/
   protected CacheSPI spi;
   protected Notifier notifier;

   public InvalidateCommand(Fqn fqn)
   {
      this.fqn = fqn;
   }

   public InvalidateCommand()
   {
   }

   public void initialize(CacheSPI cacheSpi, DataContainer dataContainer, Notifier notifier)
   {
      this.spi = cacheSpi;
      this.dataContainer = dataContainer;
      this.notifier = notifier;
   }

   /**
    * Performs an invalidation on a specified node
    *
    * @param ctx invocation context
    * @return null
    */
   public Object perform(InvocationContext ctx)
   {
      NodeSPI node = enforceNodeLoading();
      if (trace) log.trace("Invalidating fqn:" + fqn);
      if (node == null)
      {
         return null;
      }
      evictNode(fqn, ctx);
      invalidateNode(node);
      return null;
   }

   boolean evictNode(Fqn fqn, InvocationContext ctx)
   {
      notifier.notifyNodeInvalidated(fqn, true, ctx);
      try
      {
         return dataContainer.evict(fqn);
      }
      finally
      {
         notifier.notifyNodeInvalidated(fqn, false, ctx);
      }
   }


   /**
    * //TODO: 2.2.0: rather than using CacheSPI this should use peek().  The other interceptors should obtain locks and load nodes if necessary for this InvalidateCommand.
    * //Even better - this can be handles in the interceptors before call interceptor
    */
   protected NodeSPI enforceNodeLoading()
   {
      return spi.getNode(fqn);
   }


   /**
    * mark the node to be removed (and all children) as invalid so anyone holding a direct reference to it will
    * be aware that it is no longer valid.
    */
   protected void invalidateNode(NodeSPI node)
   {
      node.setValid(false, true);
      // root nodes can never be invalid
      if (fqn.isRoot()) node.setValid(true, false); // non-recursive.
   }


   public Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable
   {
      return visitor.visitInvalidateCommand(ctx, this);
   }

   public int getCommandId()
   {
      return METHOD_ID;
   }

   @Override
   public String toString()
   {
      return "InvalidateCommand{" +
            "fqn=" + fqn +
            '}';
   }

   @Override
   public Object[] getParameters()
   {
      return new Object[]{fqn};
   }

   @Override
   public void setParameters(int commandId, Object[] args)
   {
      fqn = (Fqn) args[0];
   }

   void setFqn(Fqn newFqn)
   {
      this.fqn = newFqn;
   }
}
