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

/**
 * Configuration implementation for {@link LFUAlgorithm}.
 *
 * @author Manik Surtani
 * @since 3.0
 */
public class LFUAlgorithmConfig extends EvictionAlgorithmConfigBase
{
   /**
    * The serialVersionUID
    */
   private static final long serialVersionUID = 1865801530398969179L;

   @Dynamic
   private int minNodes = -1;

   public LFUAlgorithmConfig()
   {
      evictionAlgorithmClassName = LFUAlgorithm.class.getName();
   }

   public LFUAlgorithmConfig(int maxNodes, int minNodes)
   {
      this();
      setMaxNodes(maxNodes);
      setMinNodes(minNodes);
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

   @Override
   public String toString()
   {
      StringBuilder ret = new StringBuilder();
      ret.append("LFUAlgorithmConfig: maxNodes = ").append(getMaxNodes()).append(" minNodes = ").append(getMinNodes());
      return ret.toString();
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj instanceof LFUAlgorithmConfig && super.equals(obj))
      {
         return (this.minNodes == ((LFUAlgorithmConfig) obj).minNodes);
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
   public LFUAlgorithmConfig clone() throws CloneNotSupportedException
   {
      return (LFUAlgorithmConfig) super.clone();
   }

   @Override
   public void reset()
   {
      super.reset();
      minNodes = -1;
      evictionAlgorithmClassName = LFUAlgorithm.class.getName();
   }
}