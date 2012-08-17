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
package org.jboss.cache.eviction;

import org.jboss.cache.config.ConfigurationComponent;
import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.config.Dynamic;
import org.jboss.cache.config.EvictionPolicyConfig;

/**
 * Base implementation of {@link EvictionPolicyConfig}. Adds properties
 * for the most commonly used config elements.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani</a>
 * @deprecated See {@link org.jboss.cache.eviction.EvictionAlgorithmConfigBase}.
 */
@Deprecated
public abstract class EvictionPolicyConfigBase
      extends ConfigurationComponent
      implements EvictionPolicyConfig
{
   /**
    * The serialVersionUID
    */
   private static final long serialVersionUID = 4591691674370188932L;

   private String evictionPolicyClass;
   @Dynamic
   private int maxNodes = 0;

   @Dynamic
   private int minTimeToLiveSeconds = 0;

   /**
    * Can only be instantiated by a subclass.
    * <p/>
    * Calls {@link #setEvictionPolicyClassName()}.
    */
   protected EvictionPolicyConfigBase()
   {
      setEvictionPolicyClassName();
   }

   public String getEvictionPolicyClass()
   {
      return evictionPolicyClass;
   }

   public void setEvictionPolicyClass(String evictionPolicyClass)
   {
      testImmutability("evictionPolicyClass");
      this.evictionPolicyClass = evictionPolicyClass;
   }

   public int getMaxNodes()
   {
      return maxNodes;
   }

   public void setMaxNodes(int maxNodes)
   {
      testImmutability("maxNodes");
      this.maxNodes = maxNodes;
   }

   public int getMinTimeToLiveSeconds()
   {
      return this.minTimeToLiveSeconds;
   }

   public void setMinTimeToLiveSeconds(int minTimeToLiveSeconds)
   {
      this.minTimeToLiveSeconds = minTimeToLiveSeconds;
   }

   public void validate() throws ConfigurationException
   {
      // no-op
   }

   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (!(o instanceof EvictionPolicyConfigBase)) return false;

      EvictionPolicyConfigBase that = (EvictionPolicyConfigBase) o;

      if (maxNodes != that.maxNodes) return false;
      if (minTimeToLiveSeconds != that.minTimeToLiveSeconds) return false;
      if (evictionPolicyClass != null ? !evictionPolicyClass.equals(that.evictionPolicyClass) : that.evictionPolicyClass != null)
         return false;

      return true;
   }

   public int hashCode()
   {
      int result;
      result = (evictionPolicyClass != null ? evictionPolicyClass.hashCode() : 0);
      result = 31 * result + maxNodes;
      result = 31 * result + minTimeToLiveSeconds;
      result = 31 * result + (minTimeToLiveSeconds ^ (minTimeToLiveSeconds >>> 3));
      return result;
   }

   public void reset()
   {
      setEvictionPolicyClass(null);
      setMaxNodes(0);
      setMinTimeToLiveSeconds(0);
      setMinTimeToLiveSeconds(0);
      setEvictionPolicyClassName();
   }

   /**
    * This method should be overridden by subclass implementers to set the default
    * {@link #setEvictionPolicyClass(String) policy class name} for the subclass.
    * This will be called when the implementation is constructed, but is also
    * called in {@link #reset()}.
    */
   abstract protected void setEvictionPolicyClassName();
}
