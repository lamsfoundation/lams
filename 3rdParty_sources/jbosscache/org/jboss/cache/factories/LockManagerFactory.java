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

import org.jboss.cache.factories.annotations.DefaultFactoryFor;
import org.jboss.cache.lock.LockManager;
import org.jboss.cache.lock.MVCCLockManager;
import org.jboss.cache.lock.NodeBasedLockManager;
import org.jboss.cache.lock.PessimisticNodeBasedLockManager;

/**
 * Creates lock managers
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.2.0
 */
@DefaultFactoryFor(classes = LockManager.class)
public class LockManagerFactory extends EmptyConstructorFactory
{
   @Override
   @SuppressWarnings("deprecation")
   protected <T> T construct(Class<T> componentType)
   {
      if (log.isTraceEnabled()) log.trace("Cache configuration is " + configuration.getNodeLockingScheme());
      switch (configuration.getNodeLockingScheme())
      {
         case MVCC:
            return componentType.cast(super.construct(MVCCLockManager.class));
         case OPTIMISTIC:
            return componentType.cast(super.construct(NodeBasedLockManager.class));
         case PESSIMISTIC:
         default:
            return componentType.cast(super.construct(PessimisticNodeBasedLockManager.class));
      }
   }
}
