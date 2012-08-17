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

import org.jboss.cache.CacheSPI;
import org.jboss.cache.Fqn;
import org.jboss.cache.config.EvictionPolicyConfig;

/**
 * Generic eviction policy interface.
 * <p/>
 * None of the Eviction classes are thread safe. It is assumed that an individual instance of an EvictionPolicy/
 * EvictionAlgorithm/EvictionQueue/EvictionConfiguration are only operated on by one thread at any given time.
 *
 * @author Ben Wang 2-2004
 * @author Daniel Huang - dhuang@jboss.org - 10/2005
 * @deprecated please use {@link EvictionActionPolicy} instead.
 */
@Deprecated
public interface EvictionPolicy
{
   /**
    * Evict a node form the underlying cache.
    *
    * @param fqn DataNode corresponds to this fqn.
    * @throws Exception
    */
   void evict(Fqn fqn) throws Exception;

   /**
    * @return the CacheSPI instance this eviction policy is configured to work on.
    */
   CacheSPI getCache();

   /**
    * Method called to set the cache in this implementation.
    *
    * @param cache the cache to set
    */
   void setCache(CacheSPI cache);

   /**
    * Get the associated EvictionAlgorithm used by the EvictionPolicy.
    * <p/>
    * This relationship should be 1-1.
    *
    * @return An EvictionAlgorithm implementation.
    */
   EvictionAlgorithm getEvictionAlgorithm();

   /**
    * The EvictionPolicyConfig implementation class used by this EvictionPolicy.
    *
    * @return EvictionPolicyConfig implementation class.
    */
   Class<? extends EvictionPolicyConfig> getEvictionConfigurationClass();

   /**
    * This method will be invoked prior to an event being processed for a node
    * with the specified Fqn.
    * <p/>
    * This method provides a way to optimize the performance of eviction by
    * signalling that the node associated with the specified Fqn should not be
    * subject to normal eviction processing.  It can also be used to filter
    * out certain {@link EvictionEventType event types} in which the particular
    * eviction algorithm has no interest.
    * </p>
    * <p/>
    * If this method returns false then the event is processed normally
    * and eviction processing for the node continues. As a result, the event
    * will be added to the {@link org.jboss.cache.Region eviction region's} event queue where
    * at some later point the particular algorithm of the eviction policy
    * can use it to decide whether to call {@link #evict(Fqn)}.
    * </p>
    * <p/>
    * If this method returns true, then the event is ignored and will not factor
    * in any subsequent eviction processing.
    * </p>
    *
    * @param fqn       The Fqn of the node associated with the event.
    * @param eventType the type of the event
    * @return <code>true</code> to ignore events of this type for this Fqn,
    *         <code>false</code> to process events normally.
    */
   boolean canIgnoreEvent(Fqn fqn, EvictionEventType eventType);
}
