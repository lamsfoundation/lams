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

import org.jboss.cache.CacheException;
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.legacy.ReversibleCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.transaction.GlobalTransaction;

/**
 * A version of {@link org.jboss.cache.commands.write.PutKeyValueCommand} which can be rolled back, for use with
 * pessimistic locking where changes are made directly on the data structures and may need to be reversed.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 * @deprecated will be removed along with optimistic and pessimistic locking.
 */
@Deprecated
public class PessPutKeyValueCommand extends PutKeyValueCommand implements ReversibleCommand
{
   protected Object oldValue;

   public PessPutKeyValueCommand(GlobalTransaction gtx, Fqn fqn, Object key, Object value)
   {
      super(gtx, fqn, key, value);
   }

   public PessPutKeyValueCommand()
   {
   }

   @Override
   public Object perform(InvocationContext ctx)
   {
      oldValue = super.perform(ctx);
      return oldValue;
   }

   public void rollback()
   {
      NodeSPI n = dataContainer.peek(fqn, false, false);
      if (n == null) throw new CacheException("node " + fqn + " not found for rollback!");
      if (oldValue == null)
      {
         n.removeDirect(key);
      }
      else
      {
         n.putDirect(key, oldValue);
      }
   }
}
