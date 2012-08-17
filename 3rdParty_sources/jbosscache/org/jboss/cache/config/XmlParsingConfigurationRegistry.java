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

import org.jboss.cache.config.parsing.CacheConfigsXmlParser;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * {@link ConfigurationRegistry} that obtains its initial set of configurations
 * by parsing an XML document.
 *
 * @author <a href="brian.stansberry@jboss.com">Brian Stansberry</a>
 * @version $Revision$
 */
public class XmlParsingConfigurationRegistry implements ConfigurationRegistry
{
   private final CacheConfigsXmlParser parser;
   private final String configResource;
   private final Map<String, Configuration> configs = new Hashtable<String, Configuration>();
   private boolean started;

   public XmlParsingConfigurationRegistry(String configResource)
   {
      parser = new CacheConfigsXmlParser();
      this.configResource = configResource;
   }

   public void start() throws Exception
   {
      if (!started)
      {
         if (configResource != null)
            configs.putAll(parser.parseConfigs(configResource));
         started = true;
      }
   }

   public void stop()
   {
      if (started)
      {
         synchronized (configs)
         {
            configs.clear();
         }
         started = false;
      }
   }

   public String getConfigResource()
   {
      return configResource;
   }

   public Set<String> getConfigurationNames()
   {
      return new HashSet<String>(configs.keySet());
   }

   public void registerConfiguration(String configName, Configuration config)
         throws CloneNotSupportedException
   {
      synchronized (configs)
      {
         if (configs.containsKey(configName))
            throw new IllegalStateException(configName + " already registered");
         configs.put(configName, config.clone());
      }
   }

   public void unregisterConfiguration(String configName)
   {
      synchronized (configs)
      {
         if (configs.remove(configName) == null)
            throw new IllegalStateException(configName + " not registered");
      }
   }

   public Configuration getConfiguration(String configName)
   {
      Configuration config;
      synchronized (configs)
      {
         config = configs.get(configName);
      }

      if (config == null)
         throw new IllegalArgumentException("unknown config " + configName);

      // Don't hand out a ref to our master copy
      try
      {
         return config.clone();
      }
      catch (CloneNotSupportedException e)
      {
         // This should not happen, as we already cloned the config
         throw new RuntimeException("Could not clone configuration " + configName, e);
      }
   }
}
