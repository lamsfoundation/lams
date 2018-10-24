/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.security;

/**
 * Interface to cache passwords retrieved from external commands.
 * 
 * @author Peter Skopek <pskopek@redhat.com>
 * @version $Revision:$
 */
public interface PasswordCache {
   
   /**
    * Checks whether the cache already contains given key. Non zero timeOut will be checked to expire cache entry.  
    *   
    * @param key
    * @param timeOut
    * @return
    */
   public boolean contains(String key, long timeOut);
   
   /**
    * Get password from the cache.
    * Returns null if there is no such key in the cache.
    * 
    * @param key
    * @return
    */
   char[] getPassword(String key); 
   
   /**
    * Store password to the cache.
    * @param key
    * @param password
    */
   public void storePassword(String key, char[] password);

   /**
    * Reset the cache (clean whole cache and start all over again).
    */
   public void reset();
}
