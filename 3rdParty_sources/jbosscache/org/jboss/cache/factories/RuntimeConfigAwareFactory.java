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

import org.jboss.cache.RPCManager;
import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.config.RuntimeConfig;
import org.jboss.cache.factories.annotations.DefaultFactoryFor;
import org.jboss.cache.util.BeanUtils;

import java.lang.reflect.Method;

/**
 * An extension of the EmptyConstructorFactory that places a component in the {@link RuntimeConfig} after creating it.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.1.0
 */
@DefaultFactoryFor(classes = RPCManager.class)
public class RuntimeConfigAwareFactory extends EmptyConstructorFactory
{
   @Override
   protected <T> T construct(Class<T> componentType)
   {
      T component = super.construct(componentType);

      Method setter = BeanUtils.setterMethod(RuntimeConfig.class, componentType);
      if (setter != null)
      {
         try
         {
            setter.invoke(configuration.getRuntimeConfig(), component);
         }
         catch (Exception e)
         {
            throw new ConfigurationException("Unable to put newly constructed component of type " + componentType + " in the RuntimeConfig", e);
         }
      }
      return component;
   }
}
