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

import org.jboss.cache.config.Dynamic;
import org.jboss.cache.config.EvictionAlgorithmConfig;

import java.util.concurrent.TimeUnit;

/**
 * Configuration implementation for {@link LFUPolicy}.
 * <p/>
 * If configured via XML, expects the following:
 * <p/>
 * <pre>
 * <region name="abc">
 *    <attribute name="minNodes">10</attribute>
 *    <attribute name="maxNodes">20</attribute>
 * </region>
 * </pre>
 *
 * @author Daniel Huang (dhuang@jboss.org)
 * @version $Revision$
 * @deprecated see {@link org.jboss.cache.eviction.LFUAlgorithmConfig}
 */
@Deprecated
public class LFUConfiguration extends EvictionPolicyConfigBase implements ModernizableConfig
{
   /**
    * The serialVersionUID
    */
   private static final long serialVersionUID = 1865801530398969179L;

   @Dynamic
   private int minNodes;

   @Override
   protected void setEvictionPolicyClassName()
   {
      setEvictionPolicyClass(LFUPolicy.class.getName());
   }

   public int getMinNodes()
   {
      return minNodes;
   }

   public void setMinNodes(int minNodes)
   {
      testImmutability("minNodes");
      this.minNodes = minNodes;
   }

   public EvictionAlgorithmConfig modernizeConfig()
   {
      LFUAlgorithmConfig modernCfg = new LFUAlgorithmConfig();
      modernCfg.setMaxNodes(getMaxNodes());
      modernCfg.setMinTimeToLive(getMinTimeToLiveSeconds(), TimeUnit.SECONDS);
      modernCfg.setMinNodes(getMinNodes());
      return modernCfg;
   }

   @Override
   public String toString()
   {
      StringBuilder ret = new StringBuilder();
      ret.append("LFUConfiguration: maxNodes = ").append(getMaxNodes()).append(" minNodes = ").append(getMinNodes());
      return ret.toString();
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj instanceof LFUConfiguration && super.equals(obj))
      {
         return (this.minNodes == ((LFUConfiguration) obj).minNodes);
      }
      return false;
   }

   @Override
   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + minNodes;
      return result;
   }

   @Override
   public LFUConfiguration clone() throws CloneNotSupportedException
   {
      return (LFUConfiguration) super.clone();
   }

}
