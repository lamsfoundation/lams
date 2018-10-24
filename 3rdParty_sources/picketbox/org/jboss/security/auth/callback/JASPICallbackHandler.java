/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.security.auth.callback;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.callback.PasswordValidationCallback;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SecurityConstants;
import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextUtil;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.identity.Identity;
import org.jboss.security.identity.IdentityFactory;
import org.jboss.security.identity.Role;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.identity.plugins.SimpleRole;
import org.jboss.security.identity.plugins.SimpleRoleGroup;


/**
 * {@code CallbackHandler} with the JASPI callbacks
 * @author Anil.Saldhana@redhat.com
 * @since May 12, 2010
 */
public class JASPICallbackHandler extends JBossCallbackHandler
{ 
   private static final long serialVersionUID = 1L;   
   
   public JASPICallbackHandler()
   {
      super(); 
   }


   public JASPICallbackHandler(Principal principal, Object credential)
   {
      super(principal, credential); 
   }


   @Override
   protected void handleCallBack(Callback callback) throws UnsupportedCallbackException
   { 
      if( callback instanceof GroupPrincipalCallback )
      {
         GroupPrincipalCallback groupPrincipalCallback = (GroupPrincipalCallback) callback; 
         SecurityContext currentSC = SecurityActions.getCurrentSecurityContext();
         if( currentSC == null )
            throw PicketBoxMessages.MESSAGES.invalidNullSecurityContext();

         String[] rolesArray = groupPrincipalCallback.getGroups();
         int sizeOfRoles = rolesArray != null ? rolesArray.length : 0;
          
         if( sizeOfRoles > 0 )
         {
           List<Role> rolesList = new ArrayList<Role>();
           for( int i = 0; i < sizeOfRoles ; i++ )
           {
              Role role = new SimpleRole( rolesArray[ i ] );
              rolesList.add( role );
           }
           RoleGroup roles = new SimpleRoleGroup( SecurityConstants.ROLES_IDENTIFIER, rolesList );

           // if the current security context already has roles, we merge them with the incoming roles.
           RoleGroup currentRoles = currentSC.getUtil().getRoles();
           if (currentRoles != null) {
               currentRoles.addAll(roles.getRoles());
           }
           else {
               currentSC.getUtil().setRoles( roles );
           }
         } 
         
         Subject subject = groupPrincipalCallback.getSubject();
         if( subject != null )
         {
            // if the current security context already has an associated subject, we merge it with the incoming subject.
            Subject currentSubject = currentSC.getSubjectInfo().getAuthenticatedSubject();
            if (currentSubject != null) {
                subject.getPrincipals().addAll(currentSubject.getPrincipals());
                subject.getPublicCredentials().addAll(currentSubject.getPublicCredentials());
                subject.getPrivateCredentials().addAll(currentSubject.getPrivateCredentials());
            }
            currentSC.getSubjectInfo().setAuthenticatedSubject(subject);
         }
      }
      else if( callback instanceof CallerPrincipalCallback )
      {
         CallerPrincipalCallback callerPrincipalCallback = (CallerPrincipalCallback) callback; 

         SecurityContext currentSC = SecurityActions.getCurrentSecurityContext();
         
         Subject subject = callerPrincipalCallback.getSubject();
         if( currentSC == null )
            throw PicketBoxMessages.MESSAGES.invalidNullSecurityContext();
         
         if( subject != null )
         {
             // if the current security context already has an associated subject, we merge it with the incoming subject.
             Subject currentSubject = currentSC.getSubjectInfo().getAuthenticatedSubject();
             if (currentSubject != null) {
                 subject.getPrincipals().addAll(currentSubject.getPrincipals());
                 subject.getPublicCredentials().addAll(currentSubject.getPublicCredentials());
                 subject.getPrivateCredentials().addAll(currentSubject.getPrivateCredentials());
             }
             currentSC.getSubjectInfo().setAuthenticatedSubject(subject);
         }
         
         Principal callerPrincipal = callerPrincipalCallback.getPrincipal();
         if (callerPrincipal == null && callerPrincipalCallback.getName() != null)
            callerPrincipal = new SimplePrincipal(callerPrincipalCallback.getName());
         
         if( callerPrincipal != null )
         {
            Subject currentSubject = currentSC.getSubjectInfo().getAuthenticatedSubject();
            if (currentSubject != null)
                currentSubject.getPrincipals().add(callerPrincipal);
            Identity principalBasedIdentity = IdentityFactory.getIdentity( callerPrincipal, null );
            currentSC.getSubjectInfo().addIdentity( principalBasedIdentity ); 
         }
      }
      else if( callback instanceof PasswordValidationCallback )
      {
         PasswordValidationCallback passwordValidationCallback = ( PasswordValidationCallback ) callback;

         SecurityContext currentSC = SecurityActions.getCurrentSecurityContext();
         if( currentSC == null )
            throw PicketBoxMessages.MESSAGES.invalidNullSecurityContext();
         String userName = passwordValidationCallback.getUsername();
         char[] password = passwordValidationCallback.getPassword();
         Subject subject = passwordValidationCallback.getSubject();
         
//         SecurityContextUtil util = currentSC.getUtil();
         if( subject != null )
         {
             // if the current security context already has an associated subject, we merge it with the incoming subject.
             Subject currentSubject = currentSC.getSubjectInfo().getAuthenticatedSubject();
             if (currentSubject != null) {
                 subject.getPrincipals().addAll(currentSubject.getPrincipals());
                 subject.getPublicCredentials().addAll(currentSubject.getPublicCredentials());
                 subject.getPrivateCredentials().addAll(currentSubject.getPrivateCredentials());
             }
             currentSC.getSubjectInfo().setAuthenticatedSubject(subject);

             // add the identity formed by username/pw to the security context.
             Identity identity = IdentityFactory.getIdentity(new SimplePrincipal(userName), password);
             currentSC.getSubjectInfo().addIdentity(identity);
//             util.createSubjectInfo( new SimplePrincipal( userName ), password, subject);
         }  
      }
      else super.handleCallBack(callback);
   }
}