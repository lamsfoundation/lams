/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.security;

import java.util.Set;

/**
 * An interface for managers that allow cached values.
 * 
 * @author <a href="mmoyses@redhat.com">Marcus Moyses</a>
 */
public interface CacheableManager <T, K>
{

   /**
    * Sets the cache.
    * 
    * @param cache New cache to use.
    */
   public void setCache(T cache);
   
   /**
    * Flushes all entries from the cache.
    *
    */
   public void flushCache();
   
   /**
    * Flushes one entry from the cache.
    * 
    * @param key Entry's key.
    */
   public void flushCache(K key);
   
   /**
    * Checks if an entry exists in the cache.
    * 
    * @param key Entry's key
    * @return true if there is an entry, false otherwise.
    */
   public boolean containsKey(K key);
   
   /**
    * Returns a set of keys stored in the cache.
    * 
    * @return keys stored in the cache.
    */
   public Set<K> getCachedKeys();
   
}
