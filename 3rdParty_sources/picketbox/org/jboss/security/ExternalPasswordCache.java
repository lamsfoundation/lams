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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * External command password cache.
 * Singleton password cache.
 * 
 * @author Peter Skopek <pskopek@redhat.com>
 * @version $Revision:$
 */
public class ExternalPasswordCache implements PasswordCache {

   private static final ExternalPasswordCache PASSWORD_CACHE = new ExternalPasswordCache(); 

   private Map<String, PasswordRecord> cache;
   private MessageDigest md5Digest = null;

   private ExternalPasswordCache() {
      cache = Collections.synchronizedMap(new HashMap<String, PasswordRecord>());
      try {
         md5Digest = MessageDigest.getInstance("MD5");
      }
      catch (NoSuchAlgorithmException e) {
         // Cannot get MD5 algorithm instance for hashing password commands. Using NULL.
         PicketBoxLogger.LOGGER.errorCannotGetMD5AlgorithmInstance();
      }
   }

   public static ExternalPasswordCache getExternalPasswordCacheInstance() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(ExternalPasswordCache.class.getName() + ".getExternalPasswordCacheInstance"));
      }
      return PASSWORD_CACHE;
   }
   
   /* (non-Javadoc)
    * @see org.jboss.security.PasswordCache#contains(java.lang.String)
    */
   @Override
   public boolean contains(String key, long timeOut) {
      String transformedKey = transformKey(key);
      PasswordRecord pr = cache.get(transformedKey);
      if (pr != null && (timeOut == 0 || System.currentTimeMillis() - pr.timeOut < timeOut)) {
         return true;
      }      
      return false;
   }

   /* (non-Javadoc)
    * @see org.jboss.security.PasswordCache#getPassword(java.lang.String)
    */
   @Override
   public char[] getPassword(String key) {
      String newKey = transformKey(key);
      PicketBoxLogger.LOGGER.traceRetrievingPasswordFromCache(newKey);
      PasswordRecord pr = cache.get(newKey);
      return pr.password;
   }

   /* (non-Javadoc)
    * @see org.jboss.security.PasswordCache#storePassword(java.lang.String, char[])
    */
   @Override
   public void storePassword(String key, char[] password) {
      String newKey = transformKey(key);
      PicketBoxLogger.LOGGER.traceStoringPasswordToCache(newKey);
      PasswordRecord pr = new PasswordRecord();
      pr.timeOut = System.currentTimeMillis();
      pr.password = password;
      cache.put(newKey, pr);
   }
   
   private String transformKey(String key) {
      String newKey = key;
      if (md5Digest != null) {
         md5Digest.reset();
         byte[] bt = key.getBytes();
         byte[] md5 = md5Digest.digest(bt);
         newKey = new String(Base64Utils.tob64(md5));
      }
      return newKey;
   }
   
   /**
    * Get number of cached passwords. 
    * Mainly for testing purpose.
    */
   public int getCachedPasswordsCount() {
      return cache.size();
   }

   /* (non-Javadoc)
    * @see org.jboss.security.PasswordCache#reset()
    */
   @Override
   public void reset() {
      PicketBoxLogger.LOGGER.traceResettingCache();
      cache.clear();
   }
   
   
}

class PasswordRecord {

   long timeOut;
   char[] password;
   
}
