/*
  * JBoss, Home of Professional Open Source.
  * Copyright 2006, Red Hat Middleware LLC, and individual contributors
  * as indicated by the @author tags. See the copyright.txt file in the
  * distribution for a full listing of individual contributors. 
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
package org.jboss.security.mapping.providers;

import java.lang.reflect.Constructor;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.StringTokenizer;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SimplePrincipal;
 
/**
 *  Utility class for Mapping Providers
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Oct 10, 2006 
 *  @version $Revision$
 */
public class MappingProviderUtil
{
   /**
    * Add principals passed via an enumeration into a group
    * @param grp
    * @param en
    * @return
    */
   public static Group addPrincipals(Group grp, Enumeration<? extends Principal> en)
   {
      while(en.hasMoreElements())
         grp.addMember(en.nextElement()); 
      return grp;
   }
   
   /**
    * Add the roles into the Group
    * @param roles Group of roles
    * @param addRoles
    * @return Group with the added roles
    */
   public static Group addRoles(Group roles, String[] addRoles)
   {  
      Class<?> pClass = getPrincipalClass(roles); 
      for(String str:addRoles)
      { 
         roles.addMember(instantiatePrincipal(pClass,str));
      }
      return roles;
   }
   
   
   /**
    * Given a comma-separated list of roles, return a string array
    * @param str
    * @return
    */
   public static String[] getRolesFromCommaSeparatedString(String str)
   {
      if(str == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("str");
      StringTokenizer st = new StringTokenizer(str,",");
      int numTokens = st != null ? st.countTokens() : 0;
      String[] tokens = new String[numTokens];
      for(int i = 0; i < numTokens; i++)
      {
         tokens[i] = st.nextToken();
      }
      return tokens;
   } 

   /**
    * Instantiate a Principal representing a principal
    * @param cls principal class
    * @param role Name of the role
    * @return
    */
   public static Principal instantiatePrincipal(Class<?> cls, String role)
   {
      Principal p = null;
      try
      {
         Constructor<?> ctr = cls.getConstructor(new Class[] {String.class});
         p = (Principal)ctr.newInstance(new Object[]{role});
      }
      catch (Exception e)
      {
         PicketBoxLogger.LOGGER.debugIgnoredException(e);
      }
      return p;
   }
   
   /**
    * Remove all the principals from the group
    * @param grp
    * @return
    */
   public static Group removePrincipals(Group grp)
   {
      HashSet<Principal> removeset = new HashSet<Principal>();
      Enumeration<? extends Principal> en = grp.members();
      while(en.hasMoreElements())
      {
         removeset.add(en.nextElement());
      }
      
      for(Principal p:removeset)
         grp.removeMember(p);
      return grp;
   }
   
   /**
    * Remove the roles from the Group
    * @param roles Group of roles
    * @param removeRoles
    * @return Group with roles removed
    */
   public static Group removeRoles(Group roles, String[] removeRoles)
   {  
      //Assume that the roles all belong to the same principal class
      Class<?> pClass = getPrincipalClass(roles); 
      for(String str:removeRoles)
      { 
         roles.removeMember(instantiatePrincipal(pClass,str));
      }
      return roles;
   } 
   
   /**
    * Replace the principals in first group with those in the second
    * @param fg
    * @param sg
    * @return
    */
   public static Group replacePrincipals(Group fg, Group sg)
   { 
      return addPrincipals( removePrincipals(fg),sg.members());
   }
   
   private static Class<?> getPrincipalClass(Group roles)
   {
      //Assume that the roles all belong to the same principal class 
      Class<?> principalClass = SimplePrincipal.class;
      Enumeration<? extends Principal> en = roles.members();
      if(en.hasMoreElements())
      {
         principalClass = roles.members().nextElement().getClass(); 
      }
      return principalClass;
   }
}