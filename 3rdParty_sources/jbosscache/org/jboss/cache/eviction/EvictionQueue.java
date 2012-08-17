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
 * Eviction Queue interface defines a contract for the Eviction Queue implementations used by EvictionPolicies.
 * <p/>
 * Note: None of the Eviction classes are thread safe. It is assumed that an individual instance of an EvictionPolicy/
 * EvictionAlgorithm/EvictionQueue/EvictionConfiguration are only operated on by one thread at any given time.
 *
 * @author Daniel Huang (dhuang@jboss.org)
 * @version $Revision$
 */
public interface EvictionQueue extends Iterable<NodeEntry>
{
   /**
    * Get the first entry in the queue.
    * <p/>
    * If there are no entries in queue, this method will return null.
    * <p/>
    * The first node returned is expected to be the first node to evict.
    *
    * @return first NodeEntry in queue.
    */
   NodeEntry getFirstNodeEntry();

   /**
    * Retrieve a node entry by Fqn.
    * <p/>
    * This will return null if the entry is not found.
    *
    * @param fqn Fqn of the node entry to retrieve.
    * @return Node Entry object associated with given Fqn param.
    */
   NodeEntry getNodeEntry(Fqn fqn);

   NodeEntry getNodeEntry(String fqn);

   /**
    * Check if queue contains the given NodeEntry.
    *
    * @param entry NodeEntry to check for existence in queue.
    * @return true/false if NodeEntry exists in queue.
    */
   boolean containsNodeEntry(NodeEntry entry);

   /**
    * Remove a NodeEntry from queue.
    * <p/>
    * If the NodeEntry does not exist in the queue, this method will return normally.
    *
    * @param entry The NodeEntry to remove from queue.
    */
   void removeNodeEntry(NodeEntry entry);

   /**
    * Add a NodeEntry to the queue.
    *
    * @param entry The NodeEntry to add to queue.
    */
   void addNodeEntry(NodeEntry entry);

   /**
    * Get the number of nodes in the queue.
    *
    * @return The number of nodes in the queue.
    */
   int getNumberOfNodes();

   /**
    * Get the number of elements in the queue.
    *
    * @return The number of elements in the queue.
    */
   int getNumberOfElements();

   void modifyElementCount(int difference);

   /**
    * Clear the queue.
    */
   void clear();

}
