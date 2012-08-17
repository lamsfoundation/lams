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

import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.config.Dynamic;
import org.jboss.cache.config.EvictionAlgorithmConfig;

import java.util.concurrent.TimeUnit;

/**
 * Configuration for {@link ElementSizePolicy}.
 * <p/>
 * If configured via XML, expects the following:
 * <p/>
 * <pre>
 * <region name="/region/">
 *    <attribute name="maxElementsPerNode">100</attribute>
 *    <attribute name="maxNodes">10000</attribute>
 * </region>
 * </pre>
 * <p/>
 * Requires a positive "maxElementsPerNode" value otherwise a ConfigurationException is thrown.
 *
 * @author Daniel Huang
 * @author Brian Stansberry
 * @version $Revision$
 * @deprecated see {@link org.jboss.cache.eviction.ElementSizeAlgorithmConfig}
 */
@Deprecated
public class ElementSizeConfiguration extends EvictionPolicyConfigBase implements ModernizableConfig
{
   /**
    * The serialVersionUID
    */
   private static final long serialVersionUID = 2510593544656833758L;

   @Dynamic
   private int maxElementsPerNode;

   public ElementSizeConfiguration()
   {
      super();
      // Force configuration of maxElementsPerNode
      setMaxElementsPerNode(-1);
   }

   @Override
   protected void setEvictionPolicyClassName()
   {
      setEvictionPolicyClass(ElementSizePolicy.class.getName());
   }

   public EvictionAlgorithmConfig modernizeConfig()
   {
      ElementSizeAlgorithmConfig modernCfg = new ElementSizeAlgorithmConfig();
      modernCfg.setMaxElementsPerNode(getMaxElementsPerNode());
      modernCfg.setMaxNodes(getMaxNodes());
      modernCfg.setMinTimeToLive(getMinTimeToLiveSeconds(), TimeUnit.SECONDS);
      return modernCfg;
   }


   public int getMaxElementsPerNode()
   {
      return maxElementsPerNode;
   }

   public void setMaxElementsPerNode(int maxElementsPerNode)
   {
      testImmutability("maxElementsPerNode");
      this.maxElementsPerNode = maxElementsPerNode;
   }

   /**
    * Requires a positive maxElementsPerNode value or ConfigurationException
    * is thrown.
    */
   @Override
   public void validate() throws ConfigurationException
   {
      if (maxElementsPerNode < 0)
      {
         throw new ConfigurationException("maxElementsPerNode must be must be " +
               "configured to a value greater than or equal to 0");
      }
   }

   @Override
   public String toString()
   {
      StringBuilder str = new StringBuilder();
      str.append("ElementSizeConfiguration: maxElementsPerNode =");
      str.append(getMaxElementsPerNode()).append(" maxNodes =").append(getMaxNodes());
      return str.toString();
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj instanceof ElementSizeConfiguration && super.equals(obj))
      {
         return this.maxElementsPerNode == ((ElementSizeConfiguration) obj).maxElementsPerNode;
      }
      return false;
   }

   @Override
   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + maxElementsPerNode;
      return result;
   }

   @Override
   public void reset()
   {
      super.reset();
      setMaxElementsPerNode(-1);
   }

   @Override
   public ElementSizeConfiguration clone() throws CloneNotSupportedException
   {
      return (ElementSizeConfiguration) super.clone();
   }
}
