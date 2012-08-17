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
import org.jboss.cache.config.EvictionAlgorithmConfig;

import java.util.concurrent.TimeUnit;

/**
 * Configuration for {@link MRUPolicy}.
 * <p/>
 * If configured via XML, expects the following:
 * <p/>
 * <pre>
 * <region name="abc">
 *   <attribute name="maxNodes">1000</attribute>
 * </region>
 * </pre>
 * <p/>
 * Requires a "maxNodes" attribute otherwise a ConfigurationException is thrown.
 *
 * @author Daniel Huang (dhuang@jboss.org)
 * @version $Revision$
 * @deprecated see {@link org.jboss.cache.eviction.MRUAlgorithmConfig}
 */
@Deprecated
public class MRUConfiguration extends EvictionPolicyConfigBase implements ModernizableConfig
{
   /**
    * The serialVersionUID
    */
   private static final long serialVersionUID = -8734577898966155218L;

   public MRUConfiguration()
   {
      super();
      // We require that maxNodes is set
      setMaxNodes(-1);
   }

   @Override
   protected void setEvictionPolicyClassName()
   {
      setEvictionPolicyClass(MRUPolicy.class.getName());
   }

   public EvictionAlgorithmConfig modernizeConfig()
   {
      MRUAlgorithmConfig modernCfg = new MRUAlgorithmConfig();
      modernCfg.setMaxNodes(getMaxNodes());
      modernCfg.setMinTimeToLive(getMinTimeToLiveSeconds(), TimeUnit.SECONDS);
      return modernCfg;
   }

   /**
    * Requires a positive maxNodes value or ConfigurationException
    * is thrown.
    */
   @Override
   public void validate() throws ConfigurationException
   {
      if (getMaxNodes() < 0)
         throw new ConfigurationException("maxNodes not configured");
   }

   @Override
   public String toString()
   {
      StringBuilder str = new StringBuilder();
      str.append("MRUConfiguration: ").
            append(" maxNodes =").append(getMaxNodes());
      return str.toString();
   }

   @Override
   public boolean equals(Object obj)
   {
      return (obj instanceof MRUConfiguration && super.equals(obj));
   }

   @Override
   public void reset()
   {
      setMaxNodes(-1);
   }

   @Override
   public MRUConfiguration clone() throws CloneNotSupportedException
   {
      return (MRUConfiguration) super.clone();
   }

}
