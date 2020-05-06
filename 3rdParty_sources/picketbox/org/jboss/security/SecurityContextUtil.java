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

import org.jboss.security.identity.Identity;
import org.jboss.security.identity.RoleGroup;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 *  General Utility methods for dealing with the SecurityContext
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jan 5, 2007 
 *  @version $Revision$
 */
public abstract class SecurityContextUtil
{
   protected SecurityContext securityContext = null;
   
   public void setSecurityContext(SecurityContext sc)
   {
      this.securityContext = sc;
   }
   
   /**
    * Get the username from the security context
    * @return username
    */
   public abstract String getUserName();
   
   /**
    * Get the user principal the security context
    * @return user principal
    */
   public abstract Principal getUserPrincipal(); 
   
   /**
    * Get the credential
    * @return
    */
   public abstract Object getCredential();
   
   /**
    * Get the subject the security context
    * @return
    */
   public abstract Subject getSubject(); 
   
   /**
    * Get a holder of subject, runAs and caller RunAs
    * @return
    */
   public abstract SecurityIdentity getSecurityIdentity();
   
   /**
    * Inject subject, runAs and callerRunAs into the security context
    * Mainly used by integration code base to cache the security identity
    * and put back to the security context
    * @param si The SecurityIdentity Object 
    */
   public abstract void setSecurityIdentity(SecurityIdentity si);
   
   /**
    * Get the Roles associated with the user for the
    * current security context
    * @return
    */
   public abstract RoleGroup getRoles();
   
   /**
    * Set the roles for the user for the current security context
    * @param roles
    */
   public abstract void setRoles(RoleGroup roles);
   
   /**
    * Create SubjectInfo and set it in the current security context
    * @param principal
    * @param credential
    * @param subject
    */
   public void createSubjectInfo(Principal principal, Object credential,Subject subject)
   {
      SubjectInfo si = new SubjectInfo(principal, credential, subject);
      this.securityContext.setSubjectInfo(si);
   }
   
   /**
    * Create a SubjectInfo
    * @param identity
    * @param theSubject The AuthenticatedSubject(can be null)
    */
   public void createSubjectInfo(Identity identity, Subject theSubject)
   {
      this.securityContext.setSubjectInfo(new SubjectInfo(identity, theSubject));
   }
   
   /**
    * Add an Identity to the Security Context
    * @param id
    */
   public void addIdentity(Identity id)
   {
      this.securityContext.getSubjectInfo().addIdentity(id);
   }
   
   /**
    * Clear Identities of a particular type
    * @param clazz
    */
   public void clearIdentities(Class<?> clazz)
   {
      Set<Identity> ids  = this.securityContext.getSubjectInfo().getIdentities();
      if(ids != null)
      {
         Iterator<Identity> iter = ids.iterator();
         while(iter.hasNext())
         {
            Identity id = iter.next();
            if(clazz.isAssignableFrom(id.getClass()))
               this.securityContext.getSubjectInfo().removeIdentity(id);
         }
      }
   }
   
   /**
    * Get a set of identities of a particular type
    * @param clazz
    * @return
    */
   public Set<Identity> getIdentities(Class<?> clazz)
   {
      Set<Identity> resultSet = new HashSet<Identity>();
      
      Set<Identity> ids  = this.securityContext.getSubjectInfo().getIdentities();
      if(ids != null)
      {
         Iterator<Identity> iter = ids.iterator();
         while(iter.hasNext())
         {
            Identity id = iter.next();
            if(clazz.isAssignableFrom(id.getClass()))
               resultSet.add(id);
         }
      }
      return resultSet;
   }
   
   /**
    * Set the Identities into the Security Context
    * @param idSet
    */
   public void setIdentities(Set<Identity> idSet)
   {
      this.securityContext.getSubjectInfo().setIdentities(idSet);
   }
   
   /**
    * Set an object on the Security Context
    * The context implementation may place the object in its internal
    * data structures (like the Data Map)
    * @param <T> Generic Type
    * @param key Key representing the object being set
    * @param obj
    */
   public abstract <T> void set(String key, T obj);
   
   /**
    * Return an object from the Security Context
    * @param <T>
    * @param key key identifies the type of object we are requesting
    * @return
    */
   public abstract <T> T get(String key);   
   
   /**
    * Remove an object represented by the key from the security context
    * @param <T>
    * @param key key identifies the type of object we are requesting
    * @return the removed object
    */
   public abstract <T> T remove(String key);
}