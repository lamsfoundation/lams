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

/**
 * Configuration for {@link FIFOAlgorithm}.
 * <p/>
 * Requires a "maxNodes" attribute otherwise a ConfigurationException is thrown.
 *
 * @author Manik Surtani
 * @since 3.0
 */
public class FIFOAlgorithmConfig extends EvictionAlgorithmConfigBase
{
   /**
    * The serialVersionUID
    */
   private static final long serialVersionUID = -7229715009546277313L;

   public FIFOAlgorithmConfig()
   {
      evictionAlgorithmClassName = FIFOAlgorithm.class.getName();
      // We require that maxNodes is set
      setMaxNodes(-1);
   }

   public FIFOAlgorithmConfig(int maxNodes)
   {
      evictionAlgorithmClassName = FIFOAlgorithm.class.getName();
      // We require that maxNodes is set
      setMaxNodes(maxNodes);
   }

   /**
    * Requires a positive maxNodes value or ConfigurationException
    * is thrown.
    */
   @Override
   public void validate() throws ConfigurationException
   {
      super.validate();
      if (maxNodes < -1) maxNodes = -1;
   }

   @Override
   public String toString()
   {
      StringBuilder ret = new StringBuilder();
      ret.append("FIFOAlgorithmConfig: maxNodes = ").append(getMaxNodes());
      return ret.toString();
   }

   @Override
   public boolean equals(Object obj)
   {
      return (obj instanceof FIFOAlgorithmConfig && super.equals(obj));
   }

   @Override
   public void reset()
   {
      super.reset();
      setMaxNodes(-1);
      evictionAlgorithmClassName = FIFOAlgorithm.class.getName();
   }

   @Override
   public FIFOAlgorithmConfig clone() throws CloneNotSupportedException
   {
      return (FIFOAlgorithmConfig) super.clone();
   }
}