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
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.EvictionAlgorithmConfig;
import org.jboss.cache.eviction.EvictionEvent.Type;

import java.util.concurrent.BlockingQueue;

/**
 * Interface for all eviction algorithms.
 * <p/>
 * Note: None of the Eviction classes are thread safe. It is assumed that an individual instance of an EvictionPolicy/
 * EvictionAlgorithm/EvictionQueue/EvictionConfiguration are only operated on by one thread at any given time.
 *
 * @author Ben Wang 2-2004
 * @author Daniel Huang - dhuang@jboss.org - 10/2005
 * @version $Revision$
 */
public interface EvictionAlgorithm
{
   /**
    * Entry point for eviction algorithm.  Invoking this will cause the algorithm to process the queue of {@link org.jboss.cache.eviction.EvictionEvent}
    * passed in.
    *
    * @param queue to process
    */
   void process(BlockingQueue<EvictionEvent> queue) throws EvictionException;

   /**
    * Reset the whole eviction queue. The queue may need to be reset due to corrupted state, for example.
    */
   void resetEvictionQueue();

   /**
    * Get the EvictionQueue implementation used by this algorithm.
    *
    * @return the EvictionQueue implementation.
    */
   EvictionQueue getEvictionQueue();

   /**
    * Sets the eviction action policy, so the algorithm knows what to do when a node is to be evicted.
    *
    * @param evictionActionPolicy to set
    */
   void setEvictionActionPolicy(EvictionActionPolicy evictionActionPolicy);

   /**
    * Assigns the algorithm instance to a specific region.
    *
    * @param fqn                     of the region to be assigned to
    * @param cache                   cache reference
    * @param evictionAlgorithmConfig configuration for the current algorithm instance.
    * @param configuration           for the entire cache.
    */
   void assignToRegion(Fqn fqn, CacheSPI<?, ?> cache, EvictionAlgorithmConfig evictionAlgorithmConfig, Configuration configuration);

   /**
    * Tests whether the algorithm would ignore certain event types on certain Fqns.
    *
    * @param eventType event type to test for
    * @return true if the event representing the parameters would be ignored by this algorithm or not.
    */
   boolean canIgnoreEvent(Type eventType);

   /**
    * Invoked by the region manager when the enclosing region is initialized.
    */
   void initialize();

   /**
    * This is a helper so that the XML parser will be able to select and use the correct {@link org.jboss.cache.config.EvictionAlgorithmConfig} implementation
    * class corresponding to this EvictionAlgorithm.  E.g., the {@link FIFOAlgorithm} would return {@link org.jboss.cache.eviction.FIFOAlgorithmConfig}.class.
    *
    * @return a class that is used to configure this EvictionAlgorithm.
    */
   Class<? extends EvictionAlgorithmConfig> getConfigurationClass();
}
