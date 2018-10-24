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
package org.jboss.security.auth.spi;

import java.security.Principal;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;

/**
 * The XMLLoginModule users/roles object representation.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */  
public class Users
{
   private HashMap<String,User> users = new HashMap<String,User>();

   public static class User implements Comparable<User>
   {
      private String name;
      private String password;
      private String encoding;
      private HashMap<String,Group> roleGroups = new HashMap<String,Group>();

      public User()
      {
      }
      public User(String name)
      {
         this.name = name;
      }
      public String getName()
      {
         return name;
      }
      public void setName(String name)
      {
         this.name = name;
      }
      public String getPassword()
      {
         return password;
      }
      public void setPassword(String password)
      {
         this.password = password;
      }

      public String getEncoding()
      {
         return encoding;
      }
      public void setEncoding(String encoding)
      {
         this.encoding = encoding;
      }
 
      public Group[] getRoleSets()
      {
         Group[] roleSets = new Group[roleGroups.size()];
         roleGroups.values().toArray(roleSets);
         return roleSets;
      }
      public String[] getRoleNames()
      {
         return getRoleNames("Roles");
      }
      public String[] getRoleNames(String roleGroup)
      {
         Group group = roleGroups.get(roleGroup);
         String[] names = {};
         if( group != null )
         {
            ArrayList<String> tmp = new ArrayList<String>();
            Enumeration<? extends Principal> iter = group.members();
            while( iter.hasMoreElements() )
            {
               Principal p = iter.nextElement();
               tmp.add(p.getName());
            }
            names = new String[tmp.size()];
            tmp.toArray(names);
         }
         return names;
      }
      public void addRole(String roleName, String roleGroup)
      {
         Group group = roleGroups.get(roleGroup);
         if( group == null )
         {
            group = new SimpleGroup(roleGroup);
            roleGroups.put(roleGroup, group);
         }
         SimplePrincipal role = new SimplePrincipal(roleName);
         group.addMember(role);
      }
      public int compareTo(User obj)
      {
         return name.compareTo(obj.name);
      }

      public String toString()
      {
         return "User{" +
            "name='" + name + "'" +
            ", password=*" + 
            ", encoding='" + encoding + "'" +
            ", roleGroups=" + roleGroups +
            "}";
      }
   }

   public void addUser(User user)
   {
      users.put(user.getName(), user);
   }
   public Iterator<User> getUsers()
   {
      return users.values().iterator();
   }
   public User getUser(String name)
   {
      User find = (User) users.get(name);
      return find;
   }

   public int size()
   {
      return users.size();
   }

   public String toString()
   {
      return "Users("+System.identityHashCode(this)+"){" +
         "users=" + users +
         "}";
   }
}
