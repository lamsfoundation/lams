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

/**
 * Most Recently Used Policy.
 * <p/>
 * This algorithm will evict the most recently used cache entries from cache.
 *
 * @author Daniel Huang (dhuang@jboss.org)
 * @version $Revision$
 * @deprecated see MRUAlgorithm
 */
@Deprecated
public class MRUPolicy extends BaseEvictionPolicy implements ModernizablePolicy
{
   private MRUAlgorithm algorithm;


   public MRUPolicy()
   {
      super();
      algorithm = new MRUAlgorithm();
   }

   public EvictionAlgorithm getEvictionAlgorithm()
   {
      return algorithm;
   }

   public Class<MRUConfiguration> getEvictionConfigurationClass()
   {
      return MRUConfiguration.class;
   }

   public Class<? extends EvictionAlgorithm> modernizePolicy()
   {
      return MRUAlgorithm.class;
   }
}
