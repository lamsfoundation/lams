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
package org.jboss.cache.loader;

import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig.SingletonStoreConfig;
import org.jboss.cache.config.Dynamic;

import java.util.Properties;

/**
 * Default singleton store cache loader configuration implementation, which is provided with the default singleton store
 * cache loader implementation. It provides with the capability of defining whether to push the in memory state to cache
 * loader when becoming the coordinator within a time constraint.
 *
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 */
public class SingletonStoreDefaultConfig extends SingletonStoreConfig
{
   private static final long serialVersionUID = -5828927920142613537L;

   /**
    * Boolean indicating whether push state when coordinator has been configured.
    */
   @Dynamic
   private boolean pushStateWhenCoordinator;

   /**
    * Number of milliseconds configured defining the time constraint for the state push.
    */
   @Dynamic
   private int pushStateWhenCoordinatorTimeout;

   /**
    * Default constructor that sets default values for singleton store cache loader configuration taking in account
    * that this configuration belongs to the default singleton store cache loader implementation.
    */
   public SingletonStoreDefaultConfig()
   {
      /* pushStateWhenCoordinator enabled by default with 20 seconds as default timeout*/
      pushStateWhenCoordinator = true;
      pushStateWhenCoordinatorTimeout = 20000;

      /* if we got to this point, we know that singleton store must have been enabled */
      setSingletonStoreEnabled(true);
      /* and we also know that the configuration was created by SingletonStoreCacheLoader */
      setSingletonStoreClass(SingletonStoreCacheLoader.class.getName());
   }

   /**
    * Constructor that sets the assumed values for the default singleton store cache loader implementation and also
    * the properties, as per the properties section defined in the XML configuration.
    *
    * @param base contains properties set in XML configuration
    */
   public SingletonStoreDefaultConfig(SingletonStoreConfig base)
   {
      this();
      setSingletonStoreproperties(base.getSingletonStoreproperties());
   }

   @Override
   public boolean isSingletonStoreEnabled()
   {
      return true;
   }

   @Override
   public void setSingletonStoreEnabled(boolean singletonStoreEnabled)
   {
      /* ignore it */
   }

   @Override
   public String getSingletonStoreClass()
   {
      return SingletonStoreCacheLoader.class.getName();
   }

   @Override
   public void setSingletonStoreClass(String singletonStoreClass)
   {
      /* ignore it */
   }

   /**
    * Takes the properties defined and populates the individual instance fields of the default singleton store cache
    * loader configuration.
    *
    * @param props is an instance of Properties containing these values.
    */
   @Override
   public void setSingletonStoreproperties(Properties props)
   {
      super.setSingletonStoreproperties(props);
      String pushStateWhenCoordinatorStr = props.getProperty("pushStateWhenCoordinator");
      if (pushStateWhenCoordinatorStr != null)
      {
         /* if not null, we use the defined value, otherwise we leave it to the default value, true */
         /* note: default value for a null property is false, hence the check */
         setPushStateWhenCoordinator(Boolean.valueOf(pushStateWhenCoordinatorStr));
      }
      String pushStateWhenCoordinatorTimeoutStr = props.getProperty("pushStateWhenCoordinatorTimeout");
      if (pushStateWhenCoordinatorTimeoutStr != null)
      {
         setPushStateWhenCoordinatorTimeout(Integer.parseInt(pushStateWhenCoordinatorTimeoutStr));
      }
   }

   public boolean isPushStateWhenCoordinator()
   {
      return pushStateWhenCoordinator;
   }

   public void setPushStateWhenCoordinator(boolean pushStateWhenCoordinator)
   {
      testImmutability("pushStateWhenCoordinator");
      this.pushStateWhenCoordinator = pushStateWhenCoordinator;
   }

   public int getPushStateWhenCoordinatorTimeout()
   {
      return pushStateWhenCoordinatorTimeout;
   }

   public void setPushStateWhenCoordinatorTimeout(int pushStateWhenCoordinatorTimeout)
   {
      testImmutability("pushStateWhenCoordinatorTimeout");
      this.pushStateWhenCoordinatorTimeout = pushStateWhenCoordinatorTimeout;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;

      if (obj instanceof SingletonStoreDefaultConfig)
      {
         SingletonStoreDefaultConfig other = (SingletonStoreDefaultConfig) obj;
         return (other.pushStateWhenCoordinator == this.pushStateWhenCoordinator)
               && (other.pushStateWhenCoordinatorTimeout == this.pushStateWhenCoordinatorTimeout);
      }
      return false;
   }


   @Override
   public int hashCode()
   {
      int result = 13;
      result = 23 * result + (pushStateWhenCoordinator ? 0 : 1);
      result = 23 * result + pushStateWhenCoordinatorTimeout;
      return result;
   }
}
