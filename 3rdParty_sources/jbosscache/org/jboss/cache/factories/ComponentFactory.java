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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.factories.annotations.Inject;

/**
 * Factory that creates components used internally within JBoss Cache, and also wires dependencies into the components.
 * <p/>
 * The {@link org.jboss.cache.DefaultCacheFactory} is a special subclass of this, which bootstraps the construction of
 * other components.  When this class is loaded, it maintains a static list of known default factories for known
 * components, which it then delegates to, when actually performing the construction.
 * <p/>
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @see Inject
 * @see ComponentRegistry
 * @since 2.1.0
 */
public abstract class ComponentFactory
{
   protected final Log log = LogFactory.getLog(getClass());
   protected ComponentRegistry componentRegistry;
   protected Configuration configuration;

   /**
    * Constructs a new ComponentFactory.
    */
   public ComponentFactory(ComponentRegistry componentRegistry, Configuration configuration)
   {
      this.componentRegistry = componentRegistry;
      this.configuration = configuration;
   }

   /**
    * Constructs a new ComponentFactory.
    */
   public ComponentFactory()
   {
   }

   @Inject
   void injectDependencies(Configuration configuration, ComponentRegistry componentRegistry)
   {
      this.configuration = configuration;
      this.componentRegistry = componentRegistry;
   }

   /**
    * Constructs a component.
    *
    * @param componentType type of component
    * @return a component
    */
   protected abstract <T> T construct(Class<T> componentType);

   protected void assertTypeConstructable(Class requestedType, Class... ableToConstruct)
   {
      boolean canConstruct = false;
      for (Class c : ableToConstruct)
      {
         canConstruct = canConstruct || requestedType.isAssignableFrom(c);
      }
      if (!canConstruct) throw new ConfigurationException("Don't know how to construct " + requestedType);
   }

}
