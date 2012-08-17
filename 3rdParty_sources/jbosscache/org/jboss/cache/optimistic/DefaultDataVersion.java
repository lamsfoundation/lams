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
package org.jboss.cache.optimistic;

import net.jcip.annotations.Immutable;

/**
 * The default implementation of a DataVersion, uses a <code>long</code> to
 * compare versions.
 * This class is immutable.
 * <p/>
 * Also note that this is meant to control implicit, internal versioning.  Do not attempt to instantiate or use instances
 * of this class explicitly, via the {@link org.jboss.cache.config.Option#setDataVersion(DataVersion)} API, as it WILL
 * break things.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @deprecated this is to support a deprecated locking scheme (Optimistic Locking).  Will be removed when Optimistic Locking support is removed.
 */
@Immutable
@Deprecated
public class DefaultDataVersion implements DataVersion
{
   private static final long serialVersionUID = -6896315742831861046L;
   /**
    * Version zero.
    * Assign this as the first version to your data.
    */
   public static final DataVersion ZERO = new DefaultDataVersion(0L);

   /**
    * Version one.
    */
   private static final DataVersion ONE = new DefaultDataVersion(1L);

   /**
    * Version two.
    */
   private static final DataVersion TWO = new DefaultDataVersion(2L);

   private long version;

   /**
    * Constructs with version 0.
    */
   public DefaultDataVersion()
   {
   }

   /**
    * Constructs with a version number.
    */
   public DefaultDataVersion(long version)
   {
      this.version = version;
   }

   /**
    * Returns a new DataVersion with a newer version number.
    */
   public DataVersion increment()
   {
      if (this == ZERO)
      {
         return ONE;
      }
      if (this == ONE)
      {
         return TWO;
      }
      return new DefaultDataVersion(version + 1);
   }

   public boolean newerThan(DataVersion other)
   {
      if (other instanceof DefaultDataVersion)
      {
         DefaultDataVersion dvOther = (DefaultDataVersion) other;
         return version > dvOther.version;
      }
      else
      {
         // now try and swap things around to see if the other implementation knows how to compare against DefaultDataVersion.
         // could cause a problem if 'this' and 'other' have the same value, in which case other.newerThan() will return false
         // and this will return true, which is not strictly true at all.  So we try calling other.equals() first to test
         // this, assuming that other will be able to effectively test equality if they are equal.

         return !other.equals(this) && !other.newerThan(this);
      }
   }

   @Override
   public String toString()
   {
      return "Ver=" + version;
   }

   @Override
   public boolean equals(Object other)
   {
      if (other instanceof DefaultDataVersion)
      {
         return version == ((DefaultDataVersion) other).version;
      }
      return false;
   }

   @Override
   public int hashCode()
   {
      return (int) version;
   }

   public long getRawVersion()
   {
      return version;
   }
}
