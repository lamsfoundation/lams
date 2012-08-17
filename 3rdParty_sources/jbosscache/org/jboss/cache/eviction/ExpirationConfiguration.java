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
 * Configuration for indicating the Node key for setting a specific eviction time.
 *
 * @deprecated see {@link org.jboss.cache.eviction.ExpirationAlgorithmConfig}
 */
@Deprecated
public class ExpirationConfiguration extends EvictionPolicyConfigBase implements ModernizableConfig
{

   private static final long serialVersionUID = 47338798734219507L;

   /**
    * Default key name for indicating expiration time.
    */
   public static final String EXPIRATION_KEY = "expiration";

   /**
    * Node key name used to indicate the expiration of a node.
    */
   @Dynamic
   private String expirationKeyName = EXPIRATION_KEY;

   @Dynamic
   private boolean warnNoExpirationKey = true;

   @Dynamic
   private int timeToLiveSeconds = 0;

   @Override
   protected void setEvictionPolicyClassName()
   {
      setEvictionPolicyClass(ExpirationPolicy.class.getName());
   }

   public EvictionAlgorithmConfig modernizeConfig()
   {
      ExpirationAlgorithmConfig modernCfg = new ExpirationAlgorithmConfig();
      modernCfg.setExpirationKeyName(getExpirationKeyName());
      modernCfg.setTimeToLive(getTimeToLiveSeconds(), TimeUnit.SECONDS);
      modernCfg.setWarnNoExpirationKey(getWarnNoExpirationKey());
      modernCfg.setMaxNodes(getMaxNodes());
      modernCfg.setMinTimeToLive(getMinTimeToLiveSeconds(), TimeUnit.SECONDS);
      return modernCfg;
   }

   /**
    * Returns the expirationKeyName.
    * This key should point to a java.lang.Long value in the Node data.
    */
   public String getExpirationKeyName()
   {
      return expirationKeyName;
   }

   /**
    * Sets the expirationKeyName.
    */
   public void setExpirationKeyName(String expirationKeyName)
   {
      this.expirationKeyName = expirationKeyName;
   }

   /**
    * Returns true if the algorithm should warn if a expiration key is missing for a node.
    */
   public boolean getWarnNoExpirationKey()
   {
      return warnNoExpirationKey;
   }

   /**
    * Sets if the algorithm should warn if a expiration key is missing for a node.
    */
   public void setWarnNoExpirationKey(boolean warnNoExpirationKey)
   {
      this.warnNoExpirationKey = warnNoExpirationKey;
   }

   public int getTimeToLiveSeconds()
   {
      return timeToLiveSeconds;
   }

   public void setTimeToLiveSeconds(int timeToLiveSeconds)
   {
      this.timeToLiveSeconds = timeToLiveSeconds;
   }

   @Override
   public ExpirationConfiguration clone() throws CloneNotSupportedException
   {
      return (ExpirationConfiguration) super.clone();
   }
}
