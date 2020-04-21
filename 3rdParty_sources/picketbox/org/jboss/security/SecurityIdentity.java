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
package org.jboss.security;

import java.security.Principal;

import javax.security.auth.Subject;

import org.jboss.security.identity.extensions.CredentialIdentity;
 

/**
 *  Represents an Identity of an agent interacting with the
 *  security service. It can be an user or a process. It
 *  consists of a subject and various run-as
 *  @author Anil.Saldhana@redhat.com
 *  @since  Apr 22, 2007 
 *  @version $Revision$
 */
public class SecurityIdentity
{  
   SubjectInfo theSubjectInfo= null;
   RunAs runAs = null;
   RunAs callerRunAs = null;
   
   /**
    * Create a SecurityIdentity Instance
    * @param subject The SubjectInfo
    * @param outgoingRunAs RunAs that is propagating out
    * @param incomingRunAs RunAs that is propagating in
    */
   public SecurityIdentity(SubjectInfo subject, RunAs outgoingRunAs, RunAs incomingRunAs)
   {
      this.theSubjectInfo = subject;
      this.runAs = outgoingRunAs;
      this.callerRunAs = incomingRunAs;
   }

   public Principal getPrincipal()
   {
      if(theSubjectInfo != null)
      {
         CredentialIdentity<?> identity = theSubjectInfo.getIdentity(CredentialIdentity.class);
         if(identity != null)
           return identity.asPrincipal();
      }
      return null;
   }
   
   public Object getCredential()
   {
      if(theSubjectInfo != null)
      {
         CredentialIdentity<?> identity = theSubjectInfo.getIdentity(CredentialIdentity.class);
         if(identity != null)
           return identity.getCredential();
      }
      return null;
   }
   
   public Subject getSubject()
   {
      return theSubjectInfo != null ? theSubjectInfo.getAuthenticatedSubject() : null;
   }

   public RunAs getOutgoingRunAs()
   {
      return runAs;
   }

   public RunAs getIncomingRunAs()
   {
      return callerRunAs;
   } 
}