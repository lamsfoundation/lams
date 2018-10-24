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
package org.jboss.security;

import java.security.Principal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jboss.security.identity.RoleGroup;
import org.jboss.security.identity.plugins.SimpleRoleGroup;

//$Id$

/**
 * The RunAsIdentity is a Principal that associates the run-as principal
 * with his run-as role(s).
 *
 * @author Thomas.Diesler@jboss.org
 * @author Scott.Stark@jboss.org
 * @author Anil.Saldhana@jboss.org 
 * @version $Revision$
 */
public class RunAsIdentity extends SimplePrincipal implements Cloneable, RunAs
{
   /** @since 4.0.2 */
   private static final long serialVersionUID = -3236178735180485083L;

   /** The run-as role principals */
   private HashSet<Principal> runAsRoles = new HashSet<Principal>(); 
   private HashSet<Principal> principalsSet; 

   private static final String ANOYMOUS_PRINCIPAL = "anonymous";

   /**
    * Construct an inmutable instance of a RunAsIdentity
    */
   public RunAsIdentity(String roleName, String principalName)
   {
      // we don't support run-as credetials
      super(principalName != null ? principalName : ANOYMOUS_PRINCIPAL);

      if (roleName == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("roleName");

      runAsRoles.add(new SimplePrincipal(roleName));
   }

   /**
    * Construct an inmutable instance of a RunAsIdentity
    */
   public RunAsIdentity(String roleName, String principalName, Set<String> extraRoleNames)
   {
      this(roleName, principalName);

      // these come from the assembly-descriptor
      if (extraRoleNames != null)
      {
         Iterator<String> it = extraRoleNames.iterator();
         while (it.hasNext())
         {
            String extraRoleName = (String) it.next();
            runAsRoles.add(new SimplePrincipal(extraRoleName));
         }
      }
   }

   /**
    Return a set with the configured run-as role
    @return Set<Principal> for the run-as roles
    */
   public Set<Principal> getRunAsRoles()
   {
      return new HashSet<Principal>(runAsRoles);
   }
   
   /**
    * Return a RoleGroup of Run-As roles
    * @return
    */
   public RoleGroup getRunAsRolesAsRoleGroup()
   {
      return new SimpleRoleGroup(runAsRoles); 
   }
    
   /**
    Return a set with the configured run-as principal and a Group("Roles")
    with the run-as roles

    @return Set<Principal> for the run-as principal and roles
    */
   public synchronized Set<Principal> getPrincipalsSet()
   {
      if( principalsSet == null )
      {
         principalsSet = new HashSet<Principal>();
         principalsSet.add(this);
         SimpleGroup roles = new SimpleGroup("Roles");
         principalsSet.add(roles);
         Iterator<Principal> iter = runAsRoles.iterator();
         while( iter.hasNext() )
         {
            Principal role = (Principal) iter.next();
            roles.addMember(role);
         }
      }
      return principalsSet;
   }

   public boolean doesUserHaveRole(Principal role)
   {
      return runAsRoles.contains(role);
   }

   /**
    * True if the run-as principal has any of the method roles
    */
   public boolean doesUserHaveRole(Set<Principal> methodRoles)
   {
      Iterator<Principal> it = methodRoles.iterator();
      while (it.hasNext())
      {
         Principal role = (Principal) it.next();
         if (doesUserHaveRole(role))
            return true;
      }
      return false;
   }

   /**
    * Returns a string representation of the object.
    * @return a string representation of the object.
    */
   public String toString()
   {
      return "[roles=" + runAsRoles + ",principal=" + getName() + "]";
   }
   
   @SuppressWarnings("unchecked")
   public synchronized Object clone() throws CloneNotSupportedException    
   {  
      RunAsIdentity clone = (RunAsIdentity) super.clone(); 
      if(clone != null) 
      { 
         clone.principalsSet = principalsSet != null ?
                   (HashSet<Principal>)this.principalsSet.clone() : null; 
         clone.runAsRoles = (HashSet<Principal>)this.runAsRoles.clone(); 
      }  
      return clone; 
   }

   @SuppressWarnings("unchecked")
   public <T> T getIdentity()
   { 
      return (T) getName();
   }

   @SuppressWarnings("unchecked")
   public <T> T getProof()
   { 
      return (T) SecurityConstants.JAVAEE;
   } 
}