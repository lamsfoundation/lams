/*
  * JBoss, Home of Professional Open Source
  * Copyright 2007, JBoss Inc., and individual contributors as indicated
  * by the @authors tag. See the copyright.txt in the distribution for a
  * full listing of individual contributors.
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
package org.jboss.security.cache;

import java.util.Map;

//$Id$

/**
 *  Generic Security Cache Interface for usage
 *  by the security integration layers like authentication,
 *  authorization etc.
 *  
 *  @author Anil.Saldhana@redhat.com
 *  @since  May 13, 2007 
 *  @version $Revision$
 */
public interface SecurityCache<T>
{
   /**
    * Add a cache entry
    * @param key
    * @param contextMap a contextual map
    * @throws SecurityCacheException
    */
   void addCacheEntry(T key, Map<String,Object> contextMap) throws SecurityCacheException;
   
   /**
    * Cache Entry present?
    * @param key Key for the cache entry
    * @return true- cache entry exists, false-otherwise
    */
   boolean cacheHit(T key);
   
   /**
    * Perform a cache operation
    * @param key Key for the cache entry
    * @param contextMap A contextual map
    * @throws SecurityCacheException
    */
   void cacheOperation(T key, Map<String,Object> contextMap) throws SecurityCacheException; 
   
   /**
    * Get Cache Entry
    * @param <Y>
    * @param T key
    * @return Cache Entry
    * @throws SecurityCacheException
    */
   <Y> Y get(T key) throws SecurityCacheException;
}
