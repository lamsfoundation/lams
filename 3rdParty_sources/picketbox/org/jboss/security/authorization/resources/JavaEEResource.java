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
package org.jboss.security.authorization.resources;

import java.security.CodeSource;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;

import org.jboss.security.RunAs;
import org.jboss.security.authorization.Resource;
import org.jboss.security.authorization.ResourceType;
import org.jboss.security.javaee.SecurityRoleRef;

//$Id$

/**
 *  Represents a Java EE Resource
 *  @author Anil.Saldhana@redhat.com
 *  @since  Nov 26, 2007 
 *  @version $Revision$
 */
public abstract class JavaEEResource implements Resource
{
   protected Map<String,Object> map = new HashMap<String,Object>(); 
   
   protected String policyContextID = null;
   
   protected Subject callerSubject = null;
   protected RunAs callerRunAsIdentity = null;

   protected CodeSource codeSource = null;
   
   protected Principal principal = null;
   
   protected Set<SecurityRoleRef> securityRoleReferences = null;  

   public abstract ResourceType getLayer(); 

   /**
    * Add a key value to the context map
    * @param key
    * @param value
    */
   public void add( String key, Object value )
   {
      map.put(key, value);
   }
   
   /**
    * @see Resource#getMap()
    */
   public Map<String, Object> getMap()
   { 
      return Collections.unmodifiableMap( map );
   }
   
   /**
    * Get the Caller Subject
    * @return
    */
   public Subject getCallerSubject()
   {
      return callerSubject;
   }

   /**
    * Set the Caller Subject
    * @param callerSubject
    */
   public void setCallerSubject(Subject callerSubject)
   {
      this.callerSubject = callerSubject;
   }

   /**
    * Get the Caller RunAsIdentity
    * @return
    */
   public RunAs getCallerRunAsIdentity()
   {
      return callerRunAsIdentity;
   }

   /**
    * Set the Caller RunAsIdentity
    * @param callerRunAsIdentity
    */
   public void setCallerRunAsIdentity(RunAs callerRunAsIdentity)
   {
      this.callerRunAsIdentity = callerRunAsIdentity;
   }
   
   /**
    * Get the CodeSource
    * @return
    */
   public CodeSource getCodeSource()
   {
      return codeSource;
   }

   /**
    * Set the CodeSource
    * @param codeSource
    */
   public void setCodeSource(CodeSource codeSource)
   {
      this.codeSource = codeSource;
   } 

   /**
    * Get the Policy Context ID
    * (Mainly to retrieve policy from policy configuration (JACC)
    * or PolicyRegistration (XACML))
    * @return
    */
   public String getPolicyContextID()
   {
      return policyContextID;
   }

   /**
    * Set the Policy Context ID
    * @param policyContextID
    */
   public void setPolicyContextID(String policyContextID)
   {
      this.policyContextID = policyContextID;
   }

   public Principal getPrincipal()
   {
      return principal;
   }

   public void setPrincipal(Principal principal)
   {
      this.principal = principal;
   }

   /**
    * Get the set of Security Role Reference objects
    * defined in the deployment descriptor
    * @return
    */
   public Set<SecurityRoleRef> getSecurityRoleReferences()
   {
      return securityRoleReferences;
   }

   /**
    * Set the security role references
    * @param securityRoleReferences
    */
   public void setSecurityRoleReferences(Set<SecurityRoleRef> securityRoleReferences)
   {
      this.securityRoleReferences = securityRoleReferences;
   }
}