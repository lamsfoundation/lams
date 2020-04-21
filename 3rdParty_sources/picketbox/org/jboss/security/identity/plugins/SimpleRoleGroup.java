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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectInputStream.GetField;
import java.io.ObjectOutputStream;
import java.io.ObjectOutputStream.PutField;
import java.io.ObjectStreamField;
import java.security.Principal;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.identity.Role;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.identity.RoleType;

//$Id$

/**
 *  Simple Role Group
 *  @author Anil.Saldhana@redhat.com
 *  @since  Nov 16, 2007 
 *  @version $Revision$
 */
public class SimpleRoleGroup extends SimpleRole implements RoleGroup
{
   private static final long serialVersionUID = 1L;

   // roles used to be ArrayList<Role>
   // we use do this so don't break the serialized form
   private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("roles", ArrayList.class)};
   private volatile HashSet<Role> roles = new HashSet<Role>();

   private static final String ROLES_IDENTIFIER = "Roles";

   public SimpleRoleGroup(String roleName)
   {
      super(roleName);
   }

   public SimpleRoleGroup(String roleName, Collection<Role> roles)
   {
      super(roleName);
      if (this.roles == null)
         this.roles = new HashSet<Role>();
      addAll(roles);
   }

   public SimpleRoleGroup(Group rolesGroup)
   {
      super(rolesGroup.getName());
      Enumeration<? extends Principal> principals = rolesGroup.members();
      while (principals.hasMoreElements())
      {
         SimpleRole role = new SimpleRole(principals.nextElement().getName());
         addRole(role);
      }
   }

   public SimpleRoleGroup(Set<Principal> rolesAsPrincipals)
   {
      super(ROLES_IDENTIFIER);
      for (Principal p : rolesAsPrincipals)
      {
         SimpleRole role = new SimpleRole(p.getName());
         addRole(role);
      }
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.identity.plugins.SimpleRole#getType()
    */
   @Override
   public RoleType getType()
   {
      return RoleType.group;
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.identity.RoleGroup#addRole(org.jboss.security.identity.Role)
    */
   public synchronized void addRole(Role role)
   {
      this.roles.add(role);
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.identity.RoleGroup#addAll(java.util.List)
    */
   public synchronized void addAll(Collection<Role> roles)
   {
      if (roles != null)
      {
         this.roles.addAll(roles);
      }
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.identity.RoleGroup#removeRole(org.jboss.security.identity.Role)
    */
   public synchronized void removeRole(Role role)
   {
      this.roles.remove(role);
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.identity.RoleGroup#clearRoles()
    */
   public synchronized void clearRoles()
   {
      this.roles.clear();
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.identity.RoleGroup#getRoles()
    */
   public Collection<Role> getRoles()
   {
      // unmodifiable view: clients must update the roles through the addRole and removeRole methods.
      return Collections.unmodifiableSet(this.roles);
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.identity.plugins.SimpleRole#clone()
    */
   @SuppressWarnings("unchecked")
   public synchronized Object clone() throws CloneNotSupportedException
   {
      SimpleRoleGroup clone = (SimpleRoleGroup) super.clone();
      if (clone != null)
         clone.roles = (HashSet<Role>) this.roles.clone();
      return clone;
   }


   private void writeObject(ObjectOutputStream out) throws IOException
   {
      PutField putFields = out.putFields();
      putFields.put("roles", new ArrayList<Role>(this.roles));
      out.writeFields();
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
   {
      GetField getField = in.readFields();
      Object fieldValue = (ArrayList<?>) getField.get("roles", null);
      if (fieldValue instanceof ArrayList) {
         ArrayList<?> stringList = (ArrayList<?>) fieldValue;
         this.roles = new HashSet<Role>((ArrayList<Role>) stringList);
      } else {
         this.roles = new HashSet<Role>();
      }
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.identity.plugins.SimpleRole#containsAll(org.jboss.security.identity.Role)
    */
   @Override
   public boolean containsAll(Role anotherRole)
   {
      boolean isContained = false;

      if (anotherRole.getType() == RoleType.simple)
      {
         // synchronize iteration to avoid concurrent modification exception.
         synchronized (this)
         {
            for (Role r : roles)
            {
               isContained = r.containsAll(anotherRole);
               if (isContained)
                  return true;
            }
         }
      }
      else
      {
         //Dealing with another roleGroup
         RoleGroup anotherRG = (RoleGroup) anotherRole;
         CopyOnWriteArrayList<Role> anotherRoles = new CopyOnWriteArrayList<Role>(anotherRG.getRoles());
         for (Role r : anotherRoles)
         {
            //if any of the roles are not there, no point checking further
            if (!this.containsAll(r))
               return false;
         }
         return true;
      }
      return false;
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.identity.RoleGroup#containsAtleastOneRole(org.jboss.security.identity.RoleGroup)
    */
   public boolean containsAtleastOneRole(RoleGroup anotherRole)
   {
      if (anotherRole == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("anotherRole");
      CopyOnWriteArrayList<Role> roleList = new CopyOnWriteArrayList<Role>(anotherRole.getRoles());
      for (Role r : roleList)
      {
         if (this.containsAll(r))
            return true;
      }
      return false;
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.identity.RoleGroup#containsRole(org.jboss.security.identity.Role)
    */
   public synchronized boolean containsRole(Role role)
   {
      // synchronize iteration to avoid concurrent modification exception.
      for (Role r : roles)
      {
         if (r.containsAll(role))
            return true;
      }
      return false;
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.identity.plugins.SimpleRole#toString()
    */
   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getRoleName());
      builder.append("(");
      synchronized (this)
      {
         for (Role role : roles)
         {
            builder.append(role.toString()).append(",");
         }
      }
      builder.append(")");
      return builder.toString();
   }
}
