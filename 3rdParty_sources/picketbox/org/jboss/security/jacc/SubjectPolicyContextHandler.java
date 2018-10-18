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
package org.jboss.security.jacc;

import static org.jboss.security.SecurityConstants.SUBJECT_CONTEXT_KEY;

import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.jacc.PolicyContextException;
import javax.security.jacc.PolicyContextHandler;

import org.jboss.security.RunAsIdentity;
import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextAssociation;
import org.jboss.security.SubjectInfo;

/** A PolicyContextHandler for the current authenticated Subject.
 * @author Scott.Stark@jboss.org
 * @author Anil.Saldhana@redhat.com
 * @version $Revison:$
 */
public class SubjectPolicyContextHandler implements PolicyContextHandler
{
   public static final HashSet<Object> EMPTY_SET = new HashSet<Object>();

   private static class GetSubjectAction implements PrivilegedAction<Subject>
   {
      static PrivilegedAction<Subject> ACTION = new GetSubjectAction(); 
      
      public Subject run()
      {
         Subject theSubject = null;
         SecurityContext sc = SecurityContextAssociation.getSecurityContext();
         if(sc != null)
         {
            SubjectInfo si = sc.getSubjectInfo(); 
            
            if(si != null)
            {
               Subject activeSubject = si.getAuthenticatedSubject();
               RunAsIdentity callerRunAsIdentity = (RunAsIdentity)sc.getIncomingRunAs();
               
               if( activeSubject != null )
               {
                  Set<Principal> principalsSet;
                  if( callerRunAsIdentity == null )
                  {
                     principalsSet = activeSubject.getPrincipals();
                  }
                  else
                  {
                     principalsSet = callerRunAsIdentity.getPrincipalsSet();
                  }

                  theSubject = new Subject(true, principalsSet,
                     activeSubject.getPublicCredentials(),
                     activeSubject.getPrivateCredentials());
               }
               else
               { 
                  if( callerRunAsIdentity != null )
                  {
                     Set<Principal> principalsSet = callerRunAsIdentity.getPrincipalsSet();
                     theSubject = new Subject(true, principalsSet, EMPTY_SET, EMPTY_SET);
                  }
               }
            } 
         } 
         return theSubject;
      } 
   }

   public Object getContext(String key, Object data)
      throws PolicyContextException
   {
      if(!key.equalsIgnoreCase(SUBJECT_CONTEXT_KEY))
         return null;

      Subject subject = AccessController.doPrivileged(GetSubjectAction.ACTION);
      return subject;
   }

   public String[] getKeys()
      throws PolicyContextException
   {
      String[] keys = {SUBJECT_CONTEXT_KEY};
      return keys;
   }

   public boolean supports(String key)
      throws PolicyContextException
   {
      return key.equalsIgnoreCase(SUBJECT_CONTEXT_KEY);
   }
}
