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

import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.commands.Visitor;
import org.jboss.cache.transaction.GlobalTransaction;

/**
 * Represents the {@link org.jboss.cache.Cache#putForExternalRead(org.jboss.cache.Fqn, Object, Object)} method call.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.2.0
 */
public class PutForExternalReadCommand extends PutKeyValueCommand
{
   public static final int METHOD_ID = 45;
   public static final int VERSIONED_METHOD_ID = 46;

   public PutForExternalReadCommand(GlobalTransaction gtx, Fqn fqn, Object key, Object value)
   {
      super(gtx, fqn, key, value);
   }

   public PutForExternalReadCommand()
   {
      super();
   }

   @Override
   public int getCommandId()
   {
      if (isVersioned())
      {
         return VERSIONED_METHOD_ID;
      }
      else
      {
         return METHOD_ID;
      }
   }

   @Override
   protected boolean isVersionedId(int commandId)
   {
      return commandId == VERSIONED_METHOD_ID;
   }

   @Override
   public Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable
   {
      return visitor.visitPutForExternalReadCommand(ctx, this);
   }
}
