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
package org.jboss.security.auth.spi.otp;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.acl.Group;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
import javax.servlet.http.HttpServletRequest;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SecurityConstants;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.otp.TimeBasedOTP;
import org.jboss.security.otp.TimeBasedOTPUtil;

/**
 * <p>
 * Login Module that can be configured to validate a Time based OTP.
 * </p>
 * 
 * <p>
 * Usage:
 * This login module needs to be configured along with one of the other JBoss login modules such
 * as {@code org.jboss.security.auth.spi.DatabaseServerLoginModule} or
 * {@code org.jboss.security.auth.spi.LdapLoginModule}
 * </p>
 * Example configuration:
 * <p>
 * <pre>
 * {@code
 * <application-policy name="otp">
    <authentication>
      <login-module code="org.jboss.security.auth.spi.UsersRolesLoginModule"
        flag="required">
        <module-option name="usersProperties">props/jmx-console-users.properties</module-option>
        <module-option name="rolesProperties">props/jmx-console-roles.properties</module-option>
      </login-module>
      <login-module code="org.jboss.security.auth.spi.otp.JBossTimeBasedOTPLoginModule" />
    </authentication>
  </application-policy>
 * }
 * </pre>
 * </p>
 * 
 * <p>
 * Configurable Options:
 * </p>
 * <p>
 * <ul>
 * <li>algorithm:  either "HmacSHA1", "HmacSHA256" or "HmacSHA512"   [Default: "HmacSHA1"]</li>
 * <li>numOfDigits:  Number of digits in the TOTP.  Default is 6.</li>
 * <li>additionalRoles: any additional roles that you want to add into the authenticated subject (on success). For multiple roles,
 * separate with a comma</li>
 * </ul>
 * </p>
 * 
 * <p>
 * This login module requires the presence of "otp-users.properties" on the class path with the format:
 * username=key
 * </p>
 * 
 * <p>
 * An example of otp-users.properties is:
 * </p>
 * <p>
 * <pre>
    admin=35cae61d6d51a7b3af
   </pre>
 * </p>
 * 
 * 
 * @author Anil.Saldhana@redhat.com
 * @since Sep 21, 2010
 */
public class JBossTimeBasedOTPLoginModule implements LoginModule
{  
   // see AbstractServerLoginModule
   private static final String PASSWORD_STACKING = "password-stacking";
   private static final String USE_FIRST_PASSWORD = "useFirstPass";
   private static final String NUM_OF_DIGITS_OPT = "numOfDigits";
   private static final String ALGORITHM = "algorithm";
   private static final String ADDITIONAL_ROLES = "additionalRoles";
   
   private static final String[] ALL_VALID_OPTIONS =
   {
	   PASSWORD_STACKING,USE_FIRST_PASSWORD,NUM_OF_DIGITS_OPT,ALGORITHM,ADDITIONAL_ROLES
   };
   
   public static final String TOTP = "totp";

   private Map<String,Object> lmSharedState = new HashMap<String,Object>();
   private Map<String, Object> lmOptions = new HashMap<String,Object>();
   private CallbackHandler callbackHandler;
   private boolean useFirstPass;

   //This is the number of digits in the totp
   private int NUMBER_OF_DIGITS = 6;
   
   private String additionalRoles = null;
   
   /**
    * Default algorithm is HMAC_SHA1
    */
   private String algorithm = TimeBasedOTP.HMAC_SHA1; //Default
   private Subject subject;

   public void initialize( Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
         Map<String, ?> options )
   {
      /* TODO: this module should really extend AbstractServerLoginModule where the options check is integrated.
	   * the code here has been intentionally kept identical
	   */
      HashSet<String> validOptions = new HashSet<String>(Arrays.asList(ALL_VALID_OPTIONS));
      for (String key : options.keySet())
      {
    	 if (!validOptions.contains(key))
             PicketBoxLogger.LOGGER.warnInvalidModuleOption(key);
      }
	  
      this.subject = subject;
      this.callbackHandler = callbackHandler;
      this.lmSharedState.putAll( sharedState );
      this.lmOptions.putAll( options );

      /* Check for password sharing options. Any non-null value for
      password_stacking sets useFirstPass as this module has no way to
      validate any shared password.
       */
      String passwordStacking = (String) options.get(PASSWORD_STACKING);
      if( passwordStacking != null && passwordStacking.equalsIgnoreCase(USE_FIRST_PASSWORD) )
         useFirstPass = true;
      
      //Option for number of digits
      String numDigitString = (String) options.get(NUM_OF_DIGITS_OPT);
      if( numDigitString != null && numDigitString.length() > 0 )
         NUMBER_OF_DIGITS = Integer.parseInt( numDigitString );
      
      //Algorithm
      String algorithmStr = (String) options.get(ALGORITHM);
      if( algorithmStr != null && !algorithmStr.isEmpty())
      {
         if( algorithmStr.equalsIgnoreCase( TimeBasedOTP.HMAC_SHA256) )
            algorithm = TimeBasedOTP.HMAC_SHA256;
         if( algorithmStr.equalsIgnoreCase( TimeBasedOTP.HMAC_SHA512 ))
            algorithm = TimeBasedOTP.HMAC_SHA512;
      }
      
      additionalRoles = (String) options.get(ADDITIONAL_ROLES); 
   }

   /**
    * @see {@code LoginModule#login()}
    */
   public boolean login() throws LoginException
   {
      String username;
       

      if(useFirstPass)
      {
         username = (String) lmSharedState.get("javax.security.auth.login.name");  
      }
      else
      { 
         NameCallback nc = new NameCallback(PicketBoxMessages.MESSAGES.enterUsernameMessage(), "guest");
         Callback[] callbacks = { nc };
         try
         {
            callbackHandler.handle(callbacks);
         }
         catch ( Exception e )
         {
            LoginException le = new LoginException();
            le.initCause(e);
            throw le;
         } 

         username = nc.getName();
      }
      
      //Load the otp-users.properties file
      ClassLoader tcl = SecurityActions.getContextClassLoader();
      InputStream is = null;
      
      Properties otp = new Properties();
      try
      {
    	 is = tcl.getResourceAsStream( "otp-users.properties" );
         otp.load(is);
      }
      catch (IOException e)
      {
         LoginException le = new LoginException();
         le.initCause(e);
         throw le;
      }
      finally
      {
    	  safeClose(is);
      }
      
      String seed = otp.getProperty( username );

      String submittedTOTP = this.getTimeBasedOTPFromRequest();
      if( submittedTOTP == null || submittedTOTP.length() == 0 )
      {
         throw new LoginException();
      }
  
      try
      {
         boolean result =  false;
         
         if( algorithm.equals( TimeBasedOTP.HMAC_SHA1 ))
         {
            result =  TimeBasedOTPUtil.validate( submittedTOTP, seed.getBytes() , NUMBER_OF_DIGITS ); 
         }
         else if( algorithm.equals( TimeBasedOTP.HMAC_SHA256 ))
         {
            result =  TimeBasedOTPUtil.validate256( submittedTOTP, seed.getBytes() , NUMBER_OF_DIGITS ); 
         }
         else if( algorithm.equals( TimeBasedOTP.HMAC_SHA512 ))
         {
            result =  TimeBasedOTPUtil.validate512( submittedTOTP, seed.getBytes() , NUMBER_OF_DIGITS ); 
         }
         
         if(!result)
            throw new LoginException();
         
         //add in roles if needed
         Set<Group> groupPrincipals  = subject.getPrincipals( Group.class );
         if( groupPrincipals != null && groupPrincipals.size() > 0 )
         {
            appendRoles( groupPrincipals.iterator().next() );
         }
         
         return result; 
      }
      catch (GeneralSecurityException e)
      {
         LoginException le = new LoginException();
         le.initCause(e);
         throw le;
      } 
   }

   /**
    * @see {@code LoginModule#commit()}
    */
   public boolean commit() throws LoginException
   { 
      return true;
   }

   /**
    * @see {@code LoginModule#abort()}
    */
   public boolean abort() throws LoginException
   { 
      return true;
   }

   /**
    * @see {@code LoginModule#logout()}
    */
   public boolean logout() throws LoginException
   { 
      return true;
   } 

   private String getTimeBasedOTPFromRequest()
   {
      String totp = null;

      //This is JBoss AS specific mechanism 
      String WEB_REQUEST_KEY = "javax.servlet.http.HttpServletRequest";

      try
      {
         HttpServletRequest request = (HttpServletRequest) PolicyContext.getContext(WEB_REQUEST_KEY);
         totp = request.getParameter( TOTP );
      }
      catch (PolicyContextException e)
      {
         PicketBoxLogger.LOGGER.debugErrorGettingRequestFromPolicyContext(e);
      }
      return totp; 
   }
   
   private void appendRoles( Group group )
   {
      if( ! group.getName().equals( SecurityConstants.ROLES_IDENTIFIER ) )
        return;
        
      if(additionalRoles != null && !additionalRoles.isEmpty())
      {   
         StringTokenizer st = new StringTokenizer( additionalRoles , "," );
         while(st.hasMoreTokens())
         {
            group.addMember( new SimplePrincipal( st.nextToken().trim() ) ); 
         }
      }
   }
   private void safeClose(InputStream fis)
   {
      try
      {
         if(fis != null)
         {
            fis.close();
         }
      }
      catch(Exception ignored)
      {}
   }
}