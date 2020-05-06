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
package org.jboss.security.client;

import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextAssociation;
import org.jboss.security.SecurityContextFactory;
import org.jboss.security.SimplePrincipal;


/**
 *  Implementation of the SecurityClient contract <br/>
 *
 *  <b> Usage:<b>
 *  <pre>
 *  SecurityClient sc = SecurityClientFactory.getSecurityClient(JBossSecurityClient.class)
 *  sc.setUserName(somestring);
 *  etc...
 *  sc.login();
 *  </pre>
 *  @author Anil.Saldhana@redhat.com
 *  @since  May 1, 2007
 *  @version $Revision$
 */
public class JBossSecurityClient extends SecurityClient
{
   protected LoginContext lc = null;

   private SecurityContext previousSecurityContext = null;

   @Override
   protected void peformSASLLogin()
   {
     throw new UnsupportedOperationException();
   }

   @Override
   protected void performJAASLogin() throws LoginException
   {
      lc = new LoginContext(this.loginConfigName, this.callbackHandler);
      lc.login();
   }

   @Override
   protected void performSimpleLogin()
   {
      Principal up = null;
      if(userPrincipal instanceof String)
         up = new SimplePrincipal((String)userPrincipal);
      else
         up = (Principal) userPrincipal;

      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         previousSecurityContext = RuntimeActions.PRIVILEGED.getSecurityContext();
      }
      else {
         previousSecurityContext = RuntimeActions.NON_PRIVILEGED.getSecurityContext();
      }

      SecurityContext sc = null;
      try
      {
         if (sm != null) {
            sc = RuntimeActions.PRIVILEGED.createClientSecurityContext();
         }
         else {
            sc = RuntimeActions.NON_PRIVILEGED.createClientSecurityContext();
         }
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }

      if (sm != null) {
         RuntimeActions.PRIVILEGED.createSubjectInfo(sc, up, credential, null);
         RuntimeActions.PRIVILEGED.setSecurityContext(sc);
      }
      else {
         RuntimeActions.NON_PRIVILEGED.createSubjectInfo(sc, up, credential, null);
         RuntimeActions.NON_PRIVILEGED.setSecurityContext(sc);
      }
   }

   @Override
   protected void cleanUp()
   {

      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         RuntimeActions.PRIVILEGED.setSecurityContext(previousSecurityContext);
      }
      else {
         RuntimeActions.NON_PRIVILEGED.setSecurityContext(previousSecurityContext);
      }
      if(lc != null)
         try
         {
            lc.logout();
         }
         catch (LoginException e)
         {
            throw new RuntimeException(e);
         }
   }

   interface RuntimeActions {

      SecurityContext getSecurityContext();
      SecurityContext createClientSecurityContext() throws Exception;
      void setSecurityContext(SecurityContext securityContext);
      void createSubjectInfo(SecurityContext securityContext, Principal principal, Object credential, Subject subject);

      RuntimeActions NON_PRIVILEGED = new RuntimeActions() {

         @Override
         public SecurityContext getSecurityContext() {
            return SecurityContextAssociation.getSecurityContext();
         }

         @Override
         public SecurityContext createClientSecurityContext() throws Exception {
            return SecurityContextFactory.createSecurityContext("CLIENT");
         }

         @Override
         public void setSecurityContext(SecurityContext securityContext) {
            SecurityContextAssociation.setSecurityContext(securityContext);
         }

         @Override
         public void createSubjectInfo(SecurityContext securityContext, Principal principal, Object credential, Subject subject) {
            securityContext.getUtil().createSubjectInfo(principal, credential, subject);
         }
      };

      RuntimeActions PRIVILEGED = new RuntimeActions() {
         @Override
         public SecurityContext getSecurityContext() {
            return AccessController.doPrivileged(new PrivilegedAction<SecurityContext>() {
               @Override
               public SecurityContext run() {
                  return NON_PRIVILEGED.getSecurityContext();
               }
            });
         }

         @Override
         public SecurityContext createClientSecurityContext() throws Exception {
            try {
               return AccessController.doPrivileged(new PrivilegedExceptionAction<SecurityContext>() {
                  @Override
                  public SecurityContext run() throws Exception {
                     return NON_PRIVILEGED.createClientSecurityContext();
                  }
               });
            } catch (PrivilegedActionException pae) {
               throw pae.getException();
            }
         }

         @Override
         public void setSecurityContext(final SecurityContext securityContext) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
               @Override
               public Void run() {
                  NON_PRIVILEGED.setSecurityContext(securityContext);
                  return null;
               }
            });
         }

         @Override
         public void createSubjectInfo(final SecurityContext securityContext, final Principal principal, final Object credential, final Subject subject) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
               @Override
               public Void run() {
                  NON_PRIVILEGED.createSubjectInfo(securityContext, principal, credential, subject);
                  return null;
               }
            });
         }
      };
   }

}