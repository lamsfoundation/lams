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
package org.jboss.cache.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.CacheStatus;
import org.jboss.cache.factories.ComponentRegistry;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Base superclass of Cache configuration classes that expose some properties
 * that can be changed after the cache is started.
 *
 * @author <a href="brian.stansberry@jboss.com">Brian Stansberry</a>
 * @version $Revision$
 * @see #testImmutability(String)
 */
public abstract class ConfigurationComponent implements CloneableConfigurationComponent
{
   private static final long serialVersionUID = 4879873994727821938L;

   protected transient Log log = LogFactory.getLog(getClass());
   private transient CacheSPI cache; // back-reference to test whether the cache is running.
   private final Set<ConfigurationComponent> children = Collections.synchronizedSet(new HashSet<ConfigurationComponent>());
   private transient ComponentRegistry cr;
   // a workaround to get over immutability checks
   private boolean accessible;

   protected ConfigurationComponent()
   {
   }

   public void passCacheToChildConfig(ConfigurationComponent child)
   {
      if (child != null)
      {
         child.setCache(cache);
      }
   }

   protected void addChildConfig(ConfigurationComponent child)
   {
      if (child != null && children.add(child))
         child.setCache(cache);
   }

   protected void addChildConfigs(Collection<? extends ConfigurationComponent> toAdd)
   {
      if (toAdd != null)
      {
         for (ConfigurationComponent child : toAdd)
            addChildConfig(child);
      }
   }

   protected void removeChildConfig(ConfigurationComponent child)
   {
      children.remove(child);
   }

   protected void removeChildConfigs(Collection<? extends ConfigurationComponent> toRemove)
   {
      if (toRemove != null)
      {
         for (ConfigurationComponent child : toRemove)
            removeChildConfig(child);
      }
   }

   protected void replaceChildConfig(ConfigurationComponent oldConfig, ConfigurationComponent newConfig)
   {
      removeChildConfig(oldConfig);
      addChildConfig(newConfig);
   }

   protected void replaceChildConfigs(Collection<? extends ConfigurationComponent> oldConfigs,
                                      Collection<? extends ConfigurationComponent> newConfigs)
   {
      synchronized (children)
      {
         removeChildConfigs(oldConfigs);
         addChildConfigs(newConfigs);
      }
   }

   /**
    * Checks field modifications via setters
    *
    * @param fieldName
    */
   protected void testImmutability(String fieldName)
   {
      try
      {
         if (!accessible && cache != null && cache.getCacheStatus() != null && cache.getCacheStatus() == CacheStatus.STARTED && !getClass().getDeclaredField(fieldName).isAnnotationPresent(Dynamic.class))
         {
            throw new ConfigurationException("Attempted to modify a non-Dynamic configuration element [" + fieldName + "] after the cache has started!");
         }
      }
      catch (NoSuchFieldException e)
      {
         log.warn("Field " + fieldName + " not found!!");
      }
      finally
      {
         accessible = false;
      }
   }

   public void setCache(CacheSPI cache)
   {
      this.cache = cache;
      synchronized (children)
      {
         for (ConfigurationComponent child : children)
         {
            child.setCache(cache);
         }
      }
   }

   @Inject
   void injectDependencies(ComponentRegistry cr)
   {
      this.cr = cr;
   }

   @Start
   void start()
   {
      setCache(cr.getComponent(CacheSPI.class));
   }

   @Override
   public CloneableConfigurationComponent clone() throws CloneNotSupportedException
   {
      ConfigurationComponent c = (ConfigurationComponent) super.clone();
      c.setCache(null);
      return c;
   }
}
