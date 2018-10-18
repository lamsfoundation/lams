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
package org.jboss.security.authorization;

//$Id$

/**
 *  Defines the keys for the Context Map in the Resource
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jul 17, 2006 
 *  @version $Revision$
 */
public interface ResourceKeys
{
   String PARENT_RESOURCE = "parentResource";
   String CHILD_RESOURCES = "childResources";
   
   String HASROLE_PRINCIPAL = "hasRole.Principal";
   String PRINCIPAL_ROLES = "principal.roles";
   String RESOURCE_PERM_CHECK = "resourcePermissionCheck";
   String USERDATA_PERM_CHECK = "userDataPermissionCheck";
    
   //General Constants
   String POLICY_REGISTRATION = "policyRegistration";
   String ROLENAME = "roleName";
   String ROLEREF_PERM_CHECK = "roleRefPermissionCheck";
   String SECURITY_CONTEXT_ROLES = "securityContextRoles";
}
