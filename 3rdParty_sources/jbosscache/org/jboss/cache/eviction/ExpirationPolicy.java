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

import org.jboss.cache.Fqn;

/**
 * Returns the {@link ExpirationAlgorithm} as the policy's algorithm.
 *
 * @author rosse
 * @deprecated see ExpirationAlgorithm
 */
@Deprecated
public class ExpirationPolicy extends BaseEvictionPolicy implements ModernizablePolicy
{

   private EvictionAlgorithm algorithm;

   public ExpirationPolicy()
   {
      algorithm = new ExpirationAlgorithm();
   }

   public EvictionAlgorithm getEvictionAlgorithm()
   {
      return algorithm;
   }

   public Class<ExpirationConfiguration> getEvictionConfigurationClass()
   {
      return ExpirationConfiguration.class;
   }

   /**
    * Returns true if it's a visit node event.
    */
   @Override
   public boolean canIgnoreEvent(Fqn fqn, EvictionEventType eventType)
   {
      return (eventType == EvictionEventType.VISIT_NODE_EVENT);
   }

   public Class<? extends EvictionAlgorithm> modernizePolicy()
   {
      return ExpirationAlgorithm.class;
   }
}
