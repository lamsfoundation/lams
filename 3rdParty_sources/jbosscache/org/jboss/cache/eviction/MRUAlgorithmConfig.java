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
 * Configuration for {@link MRUAlgorithm}.
 * <p/>
 * Requires a "maxNodes" attribute otherwise a ConfigurationException is thrown.
 *
 * @author Manik Surtani
 * @since 3.0
 */
public class MRUAlgorithmConfig extends EvictionAlgorithmConfigBase
{
   /**
    * The serialVersionUID
    */
   private static final long serialVersionUID = -8734577898966155218L;

   public MRUAlgorithmConfig()
   {
      evictionAlgorithmClassName = MRUAlgorithm.class.getName();
      // We require that maxNodes is set
      setMaxNodes(-1);
   }

   public MRUAlgorithmConfig(int maxNodes)
   {
      evictionAlgorithmClassName = MRUAlgorithm.class.getName();
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
      if (getMaxNodes() < -1) maxNodes = -1;
   }

   @Override
   public String toString()
   {
      StringBuilder str = new StringBuilder();
      str.append("MRUAlgorithmConfig: ").
            append(" maxNodes =").append(getMaxNodes());
      return str.toString();
   }

   @Override
   public boolean equals(Object obj)
   {
      return (obj instanceof MRUAlgorithmConfig && super.equals(obj));
   }

   @Override
   public void reset()
   {
      super.reset();
      setMaxNodes(-1);
      evictionAlgorithmClassName = MRUAlgorithm.class.getName();
   }

   @Override
   public MRUAlgorithmConfig clone() throws CloneNotSupportedException
   {
      return (MRUAlgorithmConfig) super.clone();
   }

}