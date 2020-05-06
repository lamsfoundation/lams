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

import org.jboss.security.identity.Role;
import org.jboss.security.identity.RoleType;


/**
 *  Simple Role
 *  @author Anil.Saldhana@redhat.com
 *  @since  Nov 16, 2007 
 *  @version $Revision$
 */
public class SimpleRole implements Role, Cloneable
{
   private static final long serialVersionUID = 1L;

   private final String roleName;

   private final Role parent;

   public static final String ANYBODY = "<ANYBODY>";

   public static final Role ANYBODY_ROLE = new SimpleRole(ANYBODY);

   public SimpleRole(String roleName)
   {
      this(roleName, null);
   }

   public SimpleRole(String roleName, Role parent)
   {
      this.roleName = roleName;
      this.parent = parent;
   }

   public String getRoleName()
   {
      return this.roleName;
   }

   public RoleType getType()
   {
      return RoleType.simple;
   }

   /**
    * @see Role#contains(Role)
    */
   public boolean containsAll(Role anotherRole)
   {
      if (anotherRole.getType() == RoleType.simple)
      {
         if (ANYBODY.equals(roleName))
            return true;
         return roleName.equals(anotherRole.getRoleName());
      }
      return false;
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.identity.Role#getParent()
    */
   public Role getParent()
   {
      return this.parent;
   }

   @Override
   protected Object clone() throws CloneNotSupportedException
   {
      // TODO Auto-generated method stub
      return super.clone();
   }

   @Override
   public String toString()
   {
      return roleName;
   }
   
   @Override
   public int hashCode()
   {
      int hashCode = roleName.hashCode();
      if (parent != null)
         hashCode += parent.hashCode();
      return hashCode;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
      {
         return true;
      }
      if (obj instanceof SimpleRole)
      {
         SimpleRole other = (SimpleRole) obj;
         return parent != null ? (roleName.equals(other.roleName) && parent.equals(other.parent)) :
                 (roleName.equals(other.roleName) && other.parent == null);
      }
      return false;
   }
}