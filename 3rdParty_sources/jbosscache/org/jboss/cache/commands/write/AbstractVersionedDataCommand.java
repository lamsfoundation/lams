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

import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.VersionedDataCommand;
import org.jboss.cache.commands.read.AbstractDataCommand;
import org.jboss.cache.notifications.Notifier;
import org.jboss.cache.optimistic.DataVersion;
import org.jboss.cache.optimistic.DataVersioningException;
import org.jboss.cache.transaction.GlobalTransaction;

/**
 * Base version of {@link org.jboss.cache.commands.DataCommand} which handles common behaviour
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.2.0
 */
public abstract class AbstractVersionedDataCommand extends AbstractDataCommand implements VersionedDataCommand
{
   protected Notifier notifier;

   protected DataVersion dataVersion;
   protected GlobalTransaction globalTransaction;

   public void initialize(Notifier notifier, DataContainer dataContainer)
   {
      this.notifier = notifier;
      this.dataContainer = dataContainer;
   }

   public DataVersion getDataVersion()
   {
      return dataVersion;
   }

   public void setDataVersion(DataVersion dataVersion)
   {
      this.dataVersion = dataVersion;
   }

   public GlobalTransaction getGlobalTransaction()
   {
      return globalTransaction;
   }

   public void setGlobalTransaction(GlobalTransaction globalTransaction)
   {
      this.globalTransaction = globalTransaction;
   }

   public boolean isVersioned()
   {
      return dataVersion != null;
   }

   // basic implementations
   @Override
   public Object[] getParameters()
   {
      if (isVersioned())
         return new Object[]{fqn, dataVersion};
      else
         return new Object[]{fqn};
   }

   // basic implementations
   @Override
   public void setParameters(int commandId, Object[] args)
   {
      fqn = (Fqn) args[0];
      if (isVersionedId(commandId)) dataVersion = (DataVersion) args[1];
   }

   protected abstract boolean isVersionedId(int id);

   @Override
   public boolean equals(Object o)
   {
      if (!super.equals(o)) return false;
      AbstractVersionedDataCommand that = (AbstractVersionedDataCommand) o;
      return !(fqn != null ? !fqn.equals(that.fqn) : that.fqn != null);
   }

   @Override
   public int hashCode()
   {
      return 31 * super.hashCode() + (dataVersion != null ? dataVersion.hashCode() : 0);
   }

   /**
    * Utility method to peek a node and throw an exception if the version isn't what is expected.
    *
    * @param ctx context to use
    * @return node peeked, null if nonexistent
    * @throws org.jboss.cache.optimistic.DataVersioningException
    *          if there is a version mismatch
    */
   protected NodeSPI peekVersioned(InvocationContext ctx)
   {
      NodeSPI n = ctx.lookUpNode(fqn);
      if (n != null && isVersioned() && n.getVersion().newerThan(dataVersion))
      {
         String errMsg = new StringBuilder("Node found, but version is not equal to or less than the expected [").append(dataVersion).append("].  Is [").append(n.getVersion()).append("] instead!").toString();
         throw new DataVersioningException(errMsg);
      }
      return n;
   }
}