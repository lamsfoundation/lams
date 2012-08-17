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

import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.commands.DataCommand;

/**
 * An abstract class providing basic functionality of all {@link DataCommand}s.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.2.0
 */
public abstract class AbstractDataCommand implements DataCommand
{
   protected Fqn fqn;
   protected DataContainer dataContainer;

   public void initialize(DataContainer dataContainer)
   {
      this.dataContainer = dataContainer;
   }

   public Fqn getFqn()
   {
      return fqn;
   }

   void setFqn(Fqn fqn)
   {
      this.fqn = fqn;
   }

   /**
    * Basic versions of these methods
    */
   public Object[] getParameters()
   {
      return new Object[]{fqn};
   }

   /**
    * Basic versions of these methods
    */
   public void setParameters(int commandId, Object[] args)
   {
      fqn = (Fqn) args[0];
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      AbstractDataCommand that = (AbstractDataCommand) o;

      return !(fqn != null ? !fqn.equals(that.fqn) : that.fqn != null);
   }

   @Override
   public int hashCode()
   {
      int result;
      result = (fqn != null ? fqn.hashCode() : 0);
      result = 31 * result + getClass().hashCode();
      return result;
   }

   @Override
   public String toString()
   {
      return getClass().getSimpleName() +
            "{" +
            "fqn=" + fqn +
            '}';
   }
}
