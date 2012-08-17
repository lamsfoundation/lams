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
package org.jboss.cache.buddyreplication;

import org.jboss.cache.config.BuddyReplicationConfig;
import org.jboss.cache.config.BuddyReplicationConfig.BuddyLocatorConfig;
import org.jboss.cache.config.Dynamic;

import java.util.Properties;

/**
 * Type-specific configuration object for NextMemberBuddyLocator.
 */
public class NextMemberBuddyLocatorConfig extends BuddyLocatorConfig
{
   private static final long serialVersionUID = 2443438867383733851L;

   @Dynamic
   private int numBuddies = 1;
   @Dynamic
   private boolean ignoreColocatedBuddies = true;

   /**
    * Default constructor.
    */
   public NextMemberBuddyLocatorConfig()
   {
      setBuddyLocatorClass(NextMemberBuddyLocator.class.getName());
   }

   /**
    * Constructor for use by {@link NextMemberBuddyLocator#init(BuddyLocatorConfig)}.
    *
    * @param base the config passed in to <code>init()</code>.
    */
   NextMemberBuddyLocatorConfig(BuddyReplicationConfig.BuddyLocatorConfig base)
   {
      this();
      setBuddyLocatorProperties(base.getBuddyLocatorProperties());
   }

   @Override
   public String getBuddyLocatorClass()
   {
      return NextMemberBuddyLocator.class.getName();
   }

   @Override
   public void setBuddyLocatorClass(String buddyLocatorClass)
   {
      // ignore it
   }

   public boolean isIgnoreColocatedBuddies()
   {
      return ignoreColocatedBuddies;
   }

   public void setIgnoreColocatedBuddies(boolean ignoreColocatedBuddies)
   {
      testImmutability("ignoreColocatedBuddies");
      this.ignoreColocatedBuddies = ignoreColocatedBuddies;
   }

   public int getNumBuddies()
   {
      return numBuddies;
   }

   public void setNumBuddies(int numBuddies)
   {
      testImmutability("numBuddies");
      this.numBuddies = numBuddies;
   }

   @Override
   public void setBuddyLocatorProperties(Properties props)
   {
      super.setBuddyLocatorProperties(props);
      if (props != null)
      {
         String numBuddiesStr = props.getProperty("numBuddies");
         String ignoreColocatedBuddiesStr = props.getProperty("ignoreColocatedBuddies");
         if (numBuddiesStr != null) numBuddies = Integer.parseInt(numBuddiesStr);
         if (ignoreColocatedBuddiesStr != null)
         {
            ignoreColocatedBuddies = Boolean.valueOf(ignoreColocatedBuddiesStr);
         }
      }
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;

      if (obj instanceof NextMemberBuddyLocatorConfig)
      {
         NextMemberBuddyLocatorConfig other = (NextMemberBuddyLocatorConfig) obj;
         return (other.ignoreColocatedBuddies == this.ignoreColocatedBuddies)
               && (other.numBuddies == this.numBuddies);
      }
      return false;
   }

   @Override
   public int hashCode()
   {
      int result = 13;
      result = 23 * result + (ignoreColocatedBuddies ? 0 : 1);
      result = 23 * result + numBuddies;
      return result;
   }

   @Override
   public NextMemberBuddyLocatorConfig clone() throws CloneNotSupportedException
   {
      return (NextMemberBuddyLocatorConfig) super.clone();
   }


}