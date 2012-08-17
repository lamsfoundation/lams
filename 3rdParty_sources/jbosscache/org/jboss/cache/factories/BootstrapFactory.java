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
package org.jboss.cache.factories;

import org.jboss.cache.CacheException;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.factories.annotations.DefaultFactoryFor;
import org.jboss.cache.factories.annotations.NonVolatile;

/**
 * Factory for setting up bootstrap components
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.2.0
 */
@DefaultFactoryFor(classes = {CacheSPI.class, Configuration.class, ComponentRegistry.class})
@NonVolatile
public class BootstrapFactory extends ComponentFactory
{
   CacheSPI cacheSPI;

   public BootstrapFactory(CacheSPI cacheSPI, Configuration configuration, ComponentRegistry componentRegistry)
   {
      super(componentRegistry, configuration);
      this.cacheSPI = cacheSPI;
   }

   @Override
   protected <T> T construct(Class<T> componentType)
   {
      if (componentType.isAssignableFrom(CacheSPI.class) ||
            componentType.isAssignableFrom(Configuration.class) ||
            componentType.isAssignableFrom(ComponentRegistry.class))
      {
         return componentType.cast(cacheSPI);
      }

      throw new CacheException("Don't know how to handle type " + componentType);
   }
}
