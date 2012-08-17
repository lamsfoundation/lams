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
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.transaction.GlobalTransaction;

/**
 * Base class for transaction boundary commands that deal with global transactions
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.2.0
 */
public abstract class AbstractTransactionCommand implements VisitableCommand
{
   protected GlobalTransaction globalTransaction;

   /**
    * Default implementation which is a no-op.  Transaction boundary commands always return a null.
    *
    * @return null
    */
   public Object perform(InvocationContext ctx)
   {
      return null;
   }

   public GlobalTransaction getGlobalTransaction()
   {
      return globalTransaction;
   }

   public void setGlobalTransaction(GlobalTransaction gtx)
   {
      this.globalTransaction = gtx;
   }

   public Object[] getParameters()
   {
      return new Object[]{globalTransaction};
   }

   public void setParameters(int commandId, Object[] args)
   {
      globalTransaction = (GlobalTransaction) args[0];
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      AbstractTransactionCommand that = (AbstractTransactionCommand) o;

      if (globalTransaction != null ? !globalTransaction.equals(that.globalTransaction) : that.globalTransaction != null)
         return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      return (globalTransaction != null ? globalTransaction.hashCode() : 0);
   }

   @Override
   public String toString()
   {
      return getClass().getSimpleName() + "{" +
            "gtx=" + globalTransaction +
            '}';
   }
}
