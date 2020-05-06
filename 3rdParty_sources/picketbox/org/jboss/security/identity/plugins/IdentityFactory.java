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

import java.lang.reflect.Constructor;
import java.security.Principal;
import java.security.acl.Group;

import org.jboss.security.identity.Identity;
import org.jboss.security.identity.Role;


/**
 *  Factory to create customized principal and group
 *  instances
 *  @author Anil.Saldhana@redhat.com
 *  @since  Nov 18, 2007 
 *  @version $Revision$
 */
public class IdentityFactory
{
   public static final String IDENTITY_CLASS = "org.jboss.security.identity.plugins.SimpleIdentity";

   public static final String PRINCIPAL_CLASS = "org.jboss.security.SimplePrincipal";

   public static final String GROUP_CLASS = "org.jboss.security.SimpleGroup";
   
   public static Principal createPrincipal(String name) throws Exception
   {
      return (Principal) loadClass(PRINCIPAL_CLASS, name);
   }

   public static Group createGroup(String name) throws Exception
   {
      return (Group) loadClass(GROUP_CLASS, name);
   }

   public static Identity createIdentity(String name) throws Exception
   {
      return (Identity) loadClass(IDENTITY_CLASS, name);
   }

   public static Identity createIdentity(String identityClass, String name) throws Exception
   {
      return (Identity) loadClass(identityClass, name);
   }

   public static Identity createIdentityWithRole(String name, String roleName) throws Exception
   {
      return (Identity) loadClass(IDENTITY_CLASS, name, roleName);
   }

   public static Identity createIdentityWithRole(String identityClass, String name, String roleName) throws Exception
   {
      return (Identity) loadClass(identityClass, name, roleName);
   }

   public static Identity createIdentityWithRole(String name, Role role) throws Exception
   {
      return (Identity) loadClass(IDENTITY_CLASS, name, role);
   }

   public static Identity createIdentityWithRole(String identityClass, String name, Role role) throws Exception
   {
      return (Identity) loadClass(identityClass, name, role);
   }

   private static Object loadClass(String className, String ctorArg) throws Exception
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(IdentityFactory.class.getName() + ".loadClass"));
      }
      Class<?> clazz = SecurityActions.getClass(className);
      Constructor<?> ctr = clazz.getConstructor(new Class[]
      {String.class});
      return ctr.newInstance(new Object[]
      {ctorArg});
   }

   private static Object loadClass(String className, String ctorArg1, String ctorArg2) throws Exception
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(IdentityFactory.class.getName() + ".loadClass"));
      }
      Class<?> clazz = SecurityActions.getClass(className);
      Constructor<?> ctr = clazz.getConstructor(new Class[]
      {String.class, String.class});
      return ctr.newInstance(new Object[]
      {ctorArg1, ctorArg2});
   }

   private static Object loadClass(String className, String ctorArg1, Role ctorArg2) throws Exception
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(IdentityFactory.class.getName() + ".loadClass"));
      }
      Class<?> clazz = SecurityActions.getClass(className);
      Constructor<?> ctr = clazz.getConstructor(new Class[]
      {String.class, Role.class});
      return ctr.newInstance(new Object[]
      {ctorArg1, ctorArg2});
   }
}