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
package org.jboss.security.javaee;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
 
/**
 *  Abstract Web Authorization Helper
 *  @author Anil.Saldhana@redhat.com
 *  @since  Apr 17, 2008 
 *  @version $Revision$
 */
public abstract class AbstractWebAuthorizationHelper 
extends AbstractJavaEEHelper
{
   protected boolean enableAudit = false; 
   
   public boolean isEnableAudit()
   {
      return enableAudit;
   }

   public void setEnableAudit(boolean enableAudit)
   {
      this.enableAudit = enableAudit;
   }

   /**
    * Validate that the caller has the permission to access a web resource
    * @param contextMap
    * @param request
    * @param response
    * @param callerSubject
    * @param contextID
    * @param canonicalRequestURI
    * @return true - permitted
    * @throws IllegalArgumentException request, response, callerSubject, contextID or canonicalRequestURI is null
    * @throws IllegalStateException Authorization Manager from Security Context is null
    */
   public abstract boolean checkResourcePermission(
         Map<String, Object> contextMap,
         ServletRequest request, 
         ServletResponse response,
         Subject callerSubject, 
         String contextID,
         String canonicalRequestURI);
   
   /**
    * Validate that the caller has the permission to access a web resource
    * @param contextMap
    * @param request
    * @param response
    * @param callerSubject
    * @param contextID
    * @param canonicalRequestURI
    * @param roles
    * @return true - permitted
    * @throws IllegalArgumentException request, response, callerSubject, contextID or canonicalRequestURI is null
    * @throws IllegalStateException Authorization Manager from Security Context is null
    */
   public abstract boolean checkResourcePermission(
         Map<String, Object> contextMap,
         ServletRequest request, 
         ServletResponse response,
         Subject callerSubject, 
         String contextID,
         String canonicalRequestURI,
         List<String> roles);

   /**
    * Validate that the caller has the required role to access a resource
    * @param roleName
    * @param principal
    * @param servletName
    * @param principalRoles
    * @param contextID
    * @param callerSubject
    * @return
    * @throws IllegalArgumentException roleName, contextID, callerSubject is null
    * @throws IllegalStateException Authorization Manager from Security Context is null
    */
   public abstract boolean hasRole(
         String roleName, 
         Principal principal, 
         String servletName, 
         Set<Principal> principalRoles,  
         String contextID,
         Subject callerSubject);
   
   /**
    * Validate that the caller has the required role to access a resource
    * @param roleName
    * @param principal
    * @param servletName
    * @param principalRoles
    * @param contextID
    * @param callerSubject
    * @param roles
    * @return
    * @throws IllegalArgumentException roleName, contextID, callerSubject is null
    * @throws IllegalStateException Authorization Manager from Security Context is null
    */
   public abstract boolean hasRole(
         String roleName, 
         Principal principal, 
         String servletName, 
         Set<Principal> principalRoles,  
         String contextID,
         Subject callerSubject,
         List<String> roles);
   
   /**
    * Validate whether the transport constraints are met by the caller
    * @param contextMap
    * @param request
    * @param response
    * @param contextID
    * @param callerSubject
    * @return
    * @throws IllegalArgumentException request, response, callerSubject or contextID is null
    * @throws IllegalStateException Authorization Manager from Security Context is null
    */
   public abstract boolean hasUserDataPermission(
         Map<String,Object> contextMap,
         ServletRequest request, 
         ServletResponse response, 
         String contextID,
         Subject callerSubject);
   
   /**
    * Validate whether the transport constraints are met by the caller
    * @param contextMap
    * @param request
    * @param response
    * @param contextID
    * @param callerSubject
    * @param roles
    * @return
    * @throws IllegalArgumentException request, response, callerSubject or contextID is null
    * @throws IllegalStateException Authorization Manager from Security Context is null
    */
   public abstract boolean hasUserDataPermission(
         Map<String,Object> contextMap,
         ServletRequest request, 
         ServletResponse response, 
         String contextID,
         Subject callerSubject,
         List<String> roles);
}