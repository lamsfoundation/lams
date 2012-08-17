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

import org.jboss.cache.DataContainer;
import org.jboss.cache.RegionRegistry;
import org.jboss.cache.batch.BatchContainer;
import org.jboss.cache.buddyreplication.BuddyFqnTransformer;
import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.factories.annotations.DefaultFactoryFor;
import org.jboss.cache.invocation.CacheInvocationDelegate;
import org.jboss.cache.invocation.InvocationContextContainer;
import org.jboss.cache.loader.CacheLoaderManager;
import org.jboss.cache.lock.LockStrategyFactory;
import org.jboss.cache.marshall.Marshaller;
import org.jboss.cache.marshall.VersionAwareMarshaller;
import org.jboss.cache.mvcc.MVCCNodeHelper;
import org.jboss.cache.notifications.Notifier;
import org.jboss.cache.remoting.jgroups.ChannelMessageListener;
import org.jboss.cache.transaction.TransactionTable;

/**
 * Simple factory that just uses reflection and an empty constructor of the component type.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.1.0
 */
@DefaultFactoryFor(classes = {Notifier.class, MVCCNodeHelper.class, RegionRegistry.class,
      ChannelMessageListener.class, CacheLoaderManager.class, Marshaller.class, InvocationContextContainer.class,
      CacheInvocationDelegate.class, TransactionTable.class, DataContainer.class,
      LockStrategyFactory.class, BuddyFqnTransformer.class, BatchContainer.class})
public class EmptyConstructorFactory extends ComponentFactory
{
   @Override
   protected <T> T construct(Class<T> componentType)
   {
      try
      {
         if (componentType.isInterface())
         {
            Class componentImpl;
            if (componentType.equals(Marshaller.class))
            {
               componentImpl = VersionAwareMarshaller.class;
            }
            else
            {
               // add an "Impl" to the end of the class name and try again
               componentImpl = getClass().getClassLoader().loadClass(componentType.getName() + "Impl");
            }

            return componentType.cast(componentImpl.newInstance());
         }
         else
         {
            return componentType.newInstance();
         }
      }
      catch (Exception e)
      {
         throw new ConfigurationException("Unable to create component " + componentType, e);
      }
   }
}
