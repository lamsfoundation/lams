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
package org.jboss.cache.commands.read;

import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.Visitor;

/**
 * Implements functionality defined by {@link org.jboss.cache.Cache#getKeys(org.jboss.cache.Fqn)}
 * <p/>
 * This is the equivalent of the old MethodCallDefinitions.getKeysMethodLocal method call from 2.1.x.
 * <p/>
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2.0
 */
public class GetKeysCommand extends AbstractDataCommand
{
   public static final int METHOD_ID = 25;

   public GetKeysCommand(Fqn fqn)
   {
      this.fqn = fqn;
   }

   public GetKeysCommand()
   {
   }

   /**
    * Retrieves Set of keys for all the data stored in a node referenced by the specified Fqn.
    *
    * @param ctx invocation context
    * @return a Set<K> of data keys contained in a node for a given Fqn, or null if the Fqn refers to a node that does not exist.
    */
   public Object perform(InvocationContext ctx)
   {
      NodeSPI n = ctx.lookUpNode(fqn);
      if (n == null || n.isDeleted()) return null;
      return n.getKeysDirect();
   }

   public Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable
   {
      return visitor.visitGetKeysCommand(ctx, this);
   }

   public int getCommandId()
   {
      return METHOD_ID;
   }
}
