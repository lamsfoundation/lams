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
package org.picketbox.datasource.security;

import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.acl.Group;
import java.util.Iterator;
import java.util.Set;

import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;

import org.jboss.security.SimpleGroup;

/**
 * Common package privileged actions.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision: 71545 $
 */

class SubjectActions
{
   interface AddRolesActions
   {
      AddRolesActions PRIVILEGED = new AddRolesActions()
      {
         public void addRoles(final Subject subject, final Set<Principal> roles)
         {
            AccessController.doPrivileged(new PrivilegedAction<Void>()
            {
               public Void run()
               {
                  addSubjectRoles(subject, roles);
                  return null;
               }
            });
         }
      };

      AddRolesActions NON_PRIVILEGED = new AddRolesActions()
      {
         public void addRoles(final Subject subject, final Set<Principal> roles)
         {
            addSubjectRoles(subject, roles);
         }
      };

      void addRoles(Subject subject, Set<Principal> roles);
   }

   static class AddCredentialsAction implements PrivilegedAction<Void>
   {
      Subject subject;
      PasswordCredential cred;
      AddCredentialsAction(Subject subject, PasswordCredential cred)
      {
         this.subject = subject;
         this.cred = cred;
      }
      public Void run()
      {
         subject.getPrivateCredentials().add(cred);
         return null;
      }
   }
   static class AddPrincipalsAction implements PrivilegedAction<Void>
   {
      Subject subject;
      Principal p;
      AddPrincipalsAction(Subject subject, Principal p)
      {
         this.subject = subject;
         this.p = p;
      }
      public Void run()
      {
         subject.getPrincipals().add(p);
         return null;
      }
   }
   static class RemoveCredentialsAction implements PrivilegedAction<Void>
   {
      Subject subject;
      RemoveCredentialsAction(Subject subject)
      {
         this.subject = subject;
      }
      public Void run()
      {
         Iterator<PasswordCredential> i = subject.getPrivateCredentials(PasswordCredential.class).iterator();
         while (i.hasNext())
         {
            i.remove();
         }
         return null;
      }
   }

   static void addCredentials(Subject subject, PasswordCredential cred)
   {
      AddCredentialsAction action = new AddCredentialsAction(subject, cred);
      AccessController.doPrivileged(action);
   }
   static void addPrincipals(Subject subject, Principal p)
   {
      AddPrincipalsAction action = new AddPrincipalsAction(subject, p);
      AccessController.doPrivileged(action);
   }
   static void removeCredentials(Subject subject)
   {
      RemoveCredentialsAction action = new RemoveCredentialsAction(subject);
      AccessController.doPrivileged(action);
   }

   static void addRoles(Subject subject, Set<Principal> runAsRoles)
   {
      if (System.getSecurityManager() != null)
      {
         AddRolesActions.PRIVILEGED.addRoles(subject, runAsRoles);
      }
      else
      {
         AddRolesActions.NON_PRIVILEGED.addRoles(subject, runAsRoles);         
      }
   }

   private static Group addSubjectRoles(Subject theSubject, Set<Principal> roles)
   {
      Set<Group> subjectGroups = theSubject.getPrincipals(Group.class);
      Iterator<Group> iter = subjectGroups.iterator();
      Group roleGrp = null;
      while (iter.hasNext())
      {
         Group grp = iter.next();
         String name = grp.getName();
         if (name.equals("Roles"))
            roleGrp = grp;
      }

      // Create the Roles group if it was not found
      if (roleGrp == null)
      {
         roleGrp = new SimpleGroup("Roles");
         theSubject.getPrincipals().add(roleGrp);
      }

      Iterator<Principal> iter2 = roles.iterator();
      while (iter2.hasNext())
      {
         Principal role = iter2.next();
         roleGrp.addMember(role);
      }
      return roleGrp;
   }

}
