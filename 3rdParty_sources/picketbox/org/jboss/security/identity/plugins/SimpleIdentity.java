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
package org.jboss.security.identity.plugins;

import java.security.Principal;
import java.security.acl.Group;

import org.jboss.security.identity.Identity;
import org.jboss.security.identity.Role;


/**
 *  Simple Identity
 *  @author Anil.Saldhana@redhat.com
 *  @since  Nov 16, 2007 
 *  @version $Revision$
 */
public class SimpleIdentity implements Identity
{
   private static final long serialVersionUID = 1L;

   private final String name;

   private Role role;

   public SimpleIdentity(String name)
   {
      this.name = name;
   }

   public SimpleIdentity(String name, String roleName)
   {
      this.name = name;
      this.role = new SimpleRole(roleName);
   }

   public SimpleIdentity(String name, Role role)
   {
      this.name = name;
      this.role = role;
   }

   public Group asGroup()
   {
      try
      {
         Group gp = IdentityFactory.createGroup("Roles");
         gp.addMember(IdentityFactory.createPrincipal(role.getRoleName()));
         return gp;
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   public Principal asPrincipal()
   {
      try
      {
         return IdentityFactory.createPrincipal(name);
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   public String getName()
   {
      return this.name;
   }

   public Role getRole()
   {
      return this.role;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj instanceof SimpleIdentity)
      {
         SimpleIdentity other = (SimpleIdentity) obj;
         if (this.name != null)
            return this.name.equals(other.name);
         return (other.name == null);
      }
      return false;
   }

   @Override
   public int hashCode()
   {
      return this.name.hashCode();
   }
}