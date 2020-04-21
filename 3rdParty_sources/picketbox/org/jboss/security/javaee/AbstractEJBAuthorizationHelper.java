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

import java.lang.reflect.Method;
import java.security.CodeSource;
import java.security.Principal;
import java.util.Set;

import javax.security.auth.Subject;

import org.jboss.security.RunAs;
import org.jboss.security.authorization.Resource;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.javaee.exceptions.MissingArgumentsException;
import org.jboss.security.javaee.exceptions.WrongEEResourceException;

/**
 *  EJB Authorization Helper
 *  @author Anil.Saldhana@redhat.com
 *  @since  Apr 17, 2008 
 *  @version $Revision$
 */
public abstract class AbstractEJBAuthorizationHelper 
extends AbstractJavaEEHelper
{ 
   protected String version;
   
   /**
    * Get the EJB Version
    * @return
    */
   public abstract String getEJBVersion();
   
   /**
    * Set the EJB Version
    * @param ejbVersion
    */
   public abstract void setEJBVersion(String ejbVersion);
   
   /**
    * Authorize the EJB
    * @param resource 
    * @return
    * @throws {@code WrongEEResourceException} if the resource is not EJB resource
    */
   public abstract boolean authorize( Resource resource ) throws WrongEEResourceException;
    
   /**
    * Authorize the EJB Invocation
    * @param ejbName
    * @param ejbMethod
    * @param ejbPrincipal
    * @param invocationInterfaceString
    * @param ejbCS
    * @param callerSubject
    * @param callerRunAs
    * @param contextID
    * @param methodRoles
    * @return true - subject is authorized
    * @throws IllegalStateException Authorization Manager from SecurityContext is null
    * @throws IllegalArgumentException ejbName, ejbMethod, ejbCS or contextID is null
    * @deprecated
    * @see #authorize(Resource)
    */
   public abstract boolean authorize(String ejbName, 
         Method ejbMethod, 
         Principal ejbPrincipal,
         String invocationInterfaceString, 
         CodeSource ejbCS, 
         Subject callerSubject, 
         RunAs callerRunAs, 
         String contextID,
         RoleGroup methodRoles);
   
   /**
    * Check if the caller is in any of the roles
    * @param roleName
    * @param ejbName
    * @param ejbPrincipal
    * @param callerSubject
    * @param contextID
    * @param securityRoleRefs
    * @return true - caller is in the role
    * @throws WrongEEResourceException when resource is not EJB Resource
    * @throws MissingArgumentsException roleName, ejbName or contextID is null 
    */
   public abstract boolean isCallerInRole( Resource resource, String roleName )
   throws WrongEEResourceException, MissingArgumentsException;
   
   
   /**
    * Check if the caller is in any of the roles
    * @param roleName
    * @param ejbName
    * @param ejbPrincipal
    * @param callerSubject
    * @param contextID
    * @param securityRoleRefs
    * @return true - caller is in the role
    * @throws IllegalStateException Authorization Manager from SecurityContext is null
    * @throws IllegalArgumentException roleName, ejbName, contextID is null
    * @deprecated
    * @see #isCallerInRole(Resource, String)
    */
   public abstract boolean isCallerInRole(String roleName,
         String ejbName, 
         Principal ejbPrincipal,
         Subject callerSubject, 
         String contextID,
         Set<SecurityRoleRef> securityRoleRefs);
   

   /**
    * Enforce EJB 1.1 restrictions that the role being
    * checked has to be in the deployment descriptor
    * @param roleName
    * @param ejbName
    * @param ejbPrincipal
    * @param callerSubject
    * @param contextID
    * @param securityRoleRefs
    * @param enforceEJBRestrictions
    * @return true - caller is in the role
    * @throws IllegalStateException Authorization Manager from SecurityContext is null
    * @throws IllegalArgumentException roleName, ejbName, contextID is null
    * @deprecated 
    * @see #isCallerInRole(Resource, String)
    */
   public abstract boolean isCallerInRole(String roleName,
         String ejbName, 
         Principal ejbPrincipal,
         Subject callerSubject, 
         String contextID,
         Set<SecurityRoleRef> securityRoleRefs,
         boolean enforceEJBRestrictions);
}