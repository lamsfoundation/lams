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
package org.jboss.security.identity;

import java.io.Serializable;
import java.security.Principal;
import java.security.acl.Group;

//$Id$

/**
 *  Identity of an entity (user, process etc)
 *  @author Anil.Saldhana@redhat.com
 *  @since  Nov 16, 2007 
 *  @version $Revision$
 */
public interface Identity extends Serializable
{  
   /**
    * Get the name of the identity
    * @return
    */
   public String getName();
   
   /**
    * Get the Role (Role or RoleGroup)
    * @return
    */
   public Role getRole(); 
   
   /**
    * Return a Group only if it has been set
    * @return
    */
   public Group asGroup(); 
   
   /**
    * Returns a Principal only if it set
    * @return
    */
   public Principal asPrincipal(); 
}