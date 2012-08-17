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

/**
 * Configuration for {@link ElementSizeAlgorithm}.
 * <p/>
 * Requires a positive "maxElementsPerNode" value otherwise a ConfigurationException is thrown.
 *
 * @author Manik Surtani
 * @since 3.0
 */
public class ElementSizeAlgorithmConfig extends EvictionAlgorithmConfigBase
{
   /**
    * The serialVersionUID
    */
   private static final long serialVersionUID = 2510593544656833758L;

   @Dynamic
   private int maxElementsPerNode = -1;

   public ElementSizeAlgorithmConfig()
   {
      evictionAlgorithmClassName = ElementSizeAlgorithm.class.getName();
      // Force configuration of maxElementsPerNode
      setMaxElementsPerNode(-1);
   }

   public ElementSizeAlgorithmConfig(int maxNodes, int maxElementsPerNode)
   {
      this();
      setMaxNodes(maxNodes);
      setMaxElementsPerNode(maxElementsPerNode);
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
      super.validate();
      if (maxElementsPerNode < -1) maxElementsPerNode = -1;
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
      if (obj instanceof ElementSizeAlgorithmConfig && super.equals(obj))
      {
         return this.maxElementsPerNode == ((ElementSizeAlgorithmConfig) obj).maxElementsPerNode;
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
      evictionAlgorithmClassName = ElementSizeAlgorithm.class.getName();
   }

   @Override
   public ElementSizeAlgorithmConfig clone() throws CloneNotSupportedException
   {
      return (ElementSizeAlgorithmConfig) super.clone();
   }
}