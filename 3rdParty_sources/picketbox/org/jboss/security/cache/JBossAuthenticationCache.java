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

import java.security.Principal;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.security.auth.Subject;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SecurityConstants;
 

/**
 *  Authentication Cache keyed in by Principal
 *  @author Anil.Saldhana@redhat.com
 *  @since  May 13, 2007 
 *  @version $Revision$
 */
public class JBossAuthenticationCache implements SecurityCache<Principal>
{ 
   /** Initial Capacity for the Hash Map **/
   private int initialCapacity = 16;
   
   /** Load Factor for the HashMap **/
   private float loadFactor = (float) 0.75;
   
   /** Concurrency Level hint to the concurrent hashmap **/
   private int concurrencyLevel = 16; 
   
   private ConcurrentMap<Principal,AuthCacheObject> cacheMap = null;
   
   public JBossAuthenticationCache()
   { 
      constructCache();
   }
   
   public JBossAuthenticationCache(int initCapacity, float loadFactor,int level)
   {
      this.concurrencyLevel = level;
      this.loadFactor = loadFactor;
      this.initialCapacity = initCapacity; 
      constructCache();
   }
   
   /**
    * @see SecurityCache#addCacheEntry(Object, Map)
    */
   public void addCacheEntry(Principal principal, Map<String, Object> map) 
   throws SecurityCacheException
   {
      try
      {
         AuthCacheObject ao = new AuthCacheObject(map.get(SecurityConstants.CREDENTIAL),
               (Subject) map.get(SecurityConstants.SUBJECT));
         cacheMap.put(principal, ao);
      }
      catch(Exception e)
      {
         throw new SecurityCacheException(e);
      }
   }
   
   /**
    * @see SecurityCache#cacheHit(Object)
    */
   public boolean cacheHit(Principal principal)
   {
      return cacheMap.containsKey(principal);
   }

   /**
    * @see SecurityCache#cacheOperation(Object, Map)
    */
   @SuppressWarnings({"unchecked", "rawtypes"})
   public void cacheOperation(Principal principal, Map<String,Object> map) 
   throws SecurityCacheException
   {
      boolean isValid = false;
      if(!cacheHit(principal))
         throw new SecurityCacheException(PicketBoxMessages.MESSAGES.cacheMissMessage());
      Object cred = map.get(SecurityConstants.CREDENTIAL);
      AuthCacheObject ao = cacheMap.get(principal);
      Object cacheCred = ao.credential;
      
      //Anonymous login
      if(cred == null || cacheCred == null)
      {
         if(cred == null && cacheCred == null)
            isValid = true; 
      }  
      // See if the credential is assignable to the cache value
      else if( cacheCred.getClass().isAssignableFrom(cred.getClass()) )
      {
        /* Validate the credential by trying Comparable, char[], byte[],
         Object[], and finally Object.equals()
         */
         if( cacheCred instanceof Comparable )
         {
            Comparable c = (Comparable) cacheCred;
            isValid = c.compareTo(cred) == 0;
         }
         else if( cacheCred instanceof char[] )
         {
            char[] a1 = (char[]) cacheCred;
            char[] a2 = (char[]) cred;
            isValid = Arrays.equals(a1, a2);
         }
         else if( cacheCred instanceof byte[] )
         {
            byte[] a1 = (byte[]) cacheCred;
            byte[] a2 = (byte[]) cred;
            isValid = Arrays.equals(a1, a2);
         }
         else if( cacheCred.getClass().isArray() )
         {
            Object[] a1 = (Object[]) cacheCred;
            Object[] a2 = (Object[]) cred;
            isValid = Arrays.equals(a1, a2);
         }
         else
         {
            isValid = cacheCred.equals(cred);
         }
      }
      else if( cacheCred instanceof char[] && cred instanceof String )
      {
         char[] a1 = (char[]) cacheCred;
         char[] a2 = ((String) cred).toCharArray();
         isValid = Arrays.equals(a1, a2);
      }
      else if( cacheCred instanceof String && cred instanceof char[] )
      {
         char[] a1 = ((String) cacheCred).toCharArray();
         char[] a2 = (char[]) cred;
         isValid = Arrays.equals(a1, a2);         
      }
      
      if(!isValid)
         throw new SecurityCacheException(PicketBoxMessages.MESSAGES.cacheValidationFailedMessage());
   }

   /**
    * @see SecurityCache#get(Object)
    */ 
   @SuppressWarnings("unchecked")
   public <Y> Y get(Principal key) throws SecurityCacheException
   {
      Subject subj = null;
      if(cacheHit(key))
      {
         AuthCacheObject aco = cacheMap.get(key);
         subj = aco.subject;
      }
      return (Y) subj;
   }
   
   private void constructCache()
   {
      cacheMap = 
         new ConcurrentHashMap<Principal,AuthCacheObject>(initialCapacity,
               loadFactor, concurrencyLevel);
   }
   
   private class AuthCacheObject
   {
      private Object credential;
      private Subject subject;
      
      public AuthCacheObject(Object credential, Subject subject)
      {
         super();
         this.credential = credential;
         this.subject = subject;
      } 
   }

}