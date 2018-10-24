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

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.jboss.logging.Logger;

/** A simple implementation of LoginModule for use by JBoss clients for
 the establishment of the caller identity and credentials. This simply sets
 the SecurityContext principal to the value of the NameCallback
 filled in by the CallbackHandler, and the SecurityContext credential
 to the value of the PasswordCallback filled in by the CallbackHandler.
 
 It has the following options:
 <ul>
 <li>multi-threaded=[true|false]
 When the multi-threaded option is set to true each login thread has its own principal and credential storage.
 <li>restore-login-identity=[true|false]
 When restore-login-identity is true, the SecurityContext principal
 and credential seen on entry to the login() method are saved and restored
 on either abort or logout. When false (the default), the abort and logout
 simply clears the SecurityContext. A restore-login-identity of true is
 needed if one need to change identities and then restore the original
 caller identity.
 <li>password-stacking=tryFirstPass|useFirstPass
 When password-stacking option is set, this module first looks for a shared
 username and password using "javax.security.auth.login.name" and
 "javax.security.auth.login.password" respectively. This allows a module configured
 prior to this one to establish a valid username and password that should be passed
 to JBoss.
 </ul>
 
 @author <a href="mailto:on@ibis.odessa.ua">Oleg Nitz</a>
 @author Scott.Stark@jboss.org
 @author Anil.Saldhana@redhat.com
 */
public class ClientLoginModule implements LoginModule
{
   private static final String MULTI_TREADED = "multi-threaded";
   private static final String RESTORE_LOGIN_IDENTITY = "restore-login-identity";
   private static final String PASSWORD_STACKING = "password-stacking";
   private static final String USE_FIRST_PASSWORD = "useFirstPass";
   private static final String PRINCIPAL_CLASS = "principalClass";

   private static final String[] ALL_VALID_OPTIONS =
   {
      MULTI_TREADED,RESTORE_LOGIN_IDENTITY,PASSWORD_STACKING,PRINCIPAL_CLASS,
      
      SecurityConstants.SECURITY_DOMAIN_OPTION
   };

   private Subject subject;
   private CallbackHandler callbackHandler;
   /** The principal set during login() */
   private Principal loginPrincipal;
   /** The credential set during login() */
   private Object loginCredential;
   /** Shared state between login modules */
   private Map<String,?> sharedState;
   /** Flag indicating if the shared password should be used */
   private boolean useFirstPass;
   /** Flag indicating if the SecurityAssociation existing at login should
    be restored on logout.
    */
   private boolean restoreLoginIdentity;

   /** To restore prelogin identity **/
   private SecurityContext cachedSecurityContext;

   /** Initialize this LoginModule. This checks for the options:
    multi-threaded
    restore-login-identity
    password-stacking
    */
   public void initialize(Subject subject, CallbackHandler callbackHandler,
                          Map<String,?> sharedState, Map<String,?> options)
   {
     /* TODO: this module should really extend AbstractServerLoginModule where the options check is integrated.
      * the code here has been intentionally kept identical
      */
      HashSet<String> validOptions = new HashSet<String>(Arrays.asList(ALL_VALID_OPTIONS));
      for (Object key : options.keySet())
      {
    	 if (!validOptions.contains((String)key))
         {
            PicketBoxLogger.LOGGER.warnInvalidModuleOption((String)key);
         }
      }

      this.subject = subject;
      this.callbackHandler = callbackHandler;
      this.sharedState = sharedState;

      //log securityDomain, if set.
      PicketBoxLogger.LOGGER.debugModuleOption(SecurityConstants.SECURITY_DOMAIN_OPTION,
              options.get(SecurityConstants.SECURITY_DOMAIN_OPTION));

      // Check for multi-threaded option
      String flag = (String) options.get(MULTI_TREADED);
      if (Boolean.valueOf(flag).booleanValue() == true)
      {
         /* Turn on the server mode which uses thread local storage for
            the principal information.
         */
         PicketBoxLogger.LOGGER.debugModuleOption(MULTI_TREADED, flag);
      }
      
      /**
       * SECURITY-415: when the multi-threaded value is explictly set
       * at false, then get into the client mode.
       */
      if(flag != null && flag.length() > 0 && "false".equalsIgnoreCase(flag))
      {
         SecurityAssociationActions.setClient();
      }

      flag = (String) options.get(RESTORE_LOGIN_IDENTITY);
      restoreLoginIdentity = Boolean.valueOf(flag).booleanValue();
      PicketBoxLogger.LOGGER.debugModuleOption(RESTORE_LOGIN_IDENTITY, flag);

      /* Check for password sharing options. Any non-null value for
          password_stacking sets useFirstPass as this module has no way to
          validate any shared password.
       */
      String passwordStacking = (String) options.get(PASSWORD_STACKING);
      if(passwordStacking != null && passwordStacking.equalsIgnoreCase(USE_FIRST_PASSWORD))
         useFirstPass = true;
      PicketBoxLogger.LOGGER.debugModuleOption(PASSWORD_STACKING, passwordStacking);

      //Cache the existing security context
      this.cachedSecurityContext = SecurityAssociationActions.getSecurityContext();
   }

   /**
    * Method to authenticate a Subject (phase 1).
    */
   public boolean login() throws LoginException
   {
      PicketBoxLogger.LOGGER.traceBeginLogin();
      // If useFirstPass is true, look for the shared password
      if (useFirstPass == true)
      {
         try
         {
            Object name = sharedState.get("javax.security.auth.login.name");
            if ((name instanceof Principal) == false)
            {
               String username = name != null ? name.toString() : "";
               loginPrincipal = new SimplePrincipal(username);
            } else
            {
               loginPrincipal = (Principal) name;
            }
            loginCredential = sharedState.get("javax.security.auth.login.password");
            return true;
         }
         catch (Exception e)
         {   // Dump the exception and continue
            PicketBoxLogger.LOGGER.debugIgnoredException(e);
         }
      }

      /* There is no password sharing or we are the first login module. Get
          the username and password from the callback hander.
       */
      if (callbackHandler == null)
         throw PicketBoxMessages.MESSAGES.noCallbackHandlerAvailable();

      PasswordCallback pc = new PasswordCallback(PicketBoxMessages.MESSAGES.enterPasswordMessage(), false);
      NameCallback nc = new NameCallback(PicketBoxMessages.MESSAGES.enterUsernameMessage(), "guest");
      Callback[] callbacks = {nc, pc};
      try
      {
         String username;
         char[] password = null;
         char[] tmpPassword;

         callbackHandler.handle(callbacks);
         username = nc.getName();
         loginPrincipal = new SimplePrincipal(username);
         tmpPassword = pc.getPassword();
         if (tmpPassword != null)
         {
            password = new char[tmpPassword.length];
            System.arraycopy(tmpPassword, 0, password, 0, tmpPassword.length);
            pc.clearPassword();
         }
         loginCredential = password;
         PicketBoxLogger.LOGGER.traceObtainedAuthInfoFromHandler(loginPrincipal,
                 loginCredential != null ? loginCredential.getClass() : null);
      }
      catch (IOException ioe)
      {
         LoginException ex = new LoginException(ioe.getLocalizedMessage());
         ex.initCause(ioe);
         throw ex;
      }
      catch (UnsupportedCallbackException uce)
      {
         LoginException ex = new LoginException(uce.getLocalizedMessage());
         ex.initCause(uce);
         throw ex;
      }
      PicketBoxLogger.LOGGER.traceEndLogin(true);
      return true;
   }

   /**
    * Method to commit the authentication process (phase 2).
    */
   public boolean commit() throws LoginException
   {
      PicketBoxLogger.LOGGER.traceBeginCommit(true);

      SecurityAssociationActions.setPrincipalInfo(loginPrincipal, loginCredential, subject);

      // Add the login principal to the subject if is not there
      Set<Principal> principals = subject.getPrincipals();
      if (principals.contains(loginPrincipal) == false)
         principals.add(loginPrincipal);
      return true;
   }

   /**
    * Method to abort the authentication process (phase 2).
    */
   public boolean abort() throws LoginException
   {
      PicketBoxLogger.LOGGER.traceBeginAbort(true);
      if( restoreLoginIdentity == true )
      {
         SecurityAssociationActions.setSecurityContext(this.cachedSecurityContext);
      }
      else
      {
         // Clear the entire security association stack
         SecurityAssociationActions.clear();
      }

      return true;
   }

   public boolean logout() throws LoginException
   {
      PicketBoxLogger.LOGGER.traceBeginLogout();
      if( restoreLoginIdentity == true )
      {
         SecurityAssociationActions.setSecurityContext(this.cachedSecurityContext);
      }
      else
      {
         // Clear the entire security association stack
         SecurityAssociationActions.clear();  
      }
      Set<Principal> principals = subject.getPrincipals();
      principals.remove(loginPrincipal);
      return true;
   }
}
