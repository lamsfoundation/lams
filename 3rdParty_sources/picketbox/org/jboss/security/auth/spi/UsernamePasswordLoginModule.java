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
package org.jboss.security.auth.spi;

import org.jboss.crypto.digest.DigestCallback;
import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.plugins.ClassLoaderLocatorFactory;
import org.jboss.security.vault.SecurityVaultException;
import org.jboss.security.vault.SecurityVaultUtil;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


/** An abstract subclass of AbstractServerLoginModule that imposes
 * an identity == String username, credentials == String password view on
 * the login process.
 * <p>
 * Subclasses override the <code>getUsersPassword()</code>
 * and <code>getRoleSets()</code> methods to return the expected password and roles
 * for the user.
 *
 * @see #getUsername()
 * @see #getUsersPassword()
 * @see #getRoleSets()
 * @see #createIdentity(String)
 
 @author Scott.Stark@jboss.org
 @version $Revision$
 */
public abstract class UsernamePasswordLoginModule extends AbstractServerLoginModule
{
    // see AbstractServerLoginModule
   private static final String HASH_ALGORITHM = "hashAlgorithm";
   private static final String HASH_ENCODING = "hashEncoding";
   private static final String HASH_CHARSET = "hashCharset";
   private static final String HASH_STORE_PASSWORD = "hashStorePassword";
   private static final String HASH_USER_PASSWORD = "hashUserPassword";
   private static final String DIGEST_CALLBACK = "digestCallback";
   private static final String STORE_DIGEST_CALLBACK = "storeDigestCallback";
   private static final String IGNORE_PASSWORD_CASE = "ignorePasswordCase";
   private static final String LEGACY_CREATE_PASSWORD_HASH = "legacyCreatePasswordHash";
   private static final String THROW_VALIDATE_ERROR = "throwValidateError";
   private static final String INPUT_VALIDATOR = "inputValidator";
   private static final String PASS_IS_A1_HASH = "passwordIsA1Hash";

   private static final String[] ALL_VALID_OPTIONS =
   {
     HASH_ALGORITHM,HASH_ENCODING,HASH_CHARSET,
     HASH_STORE_PASSWORD,HASH_USER_PASSWORD,
     DIGEST_CALLBACK,STORE_DIGEST_CALLBACK,
     IGNORE_PASSWORD_CASE,LEGACY_CREATE_PASSWORD_HASH,
     THROW_VALIDATE_ERROR,INPUT_VALIDATOR, PASS_IS_A1_HASH
   };
   
   /** The login identity */
   private Principal identity;
   /** The proof of login identity */
   private char[] credential;
   /** the message digest algorithm used to hash passwords. If null then
    plain passwords will be used. */
   private String hashAlgorithm = null;
  /** the name of the charset/encoding to use when converting the password
   String to a byte array. Default is the platform's default encoding.
   */
   private String hashCharset = null;
   /** the string encoding format to use. Defaults to base64. */
   private String hashEncoding = null;
   /** A flag indicating if the password comparison should ignore case */
   private boolean ignorePasswordCase;
   /** A flag indicating if the store password should be hashed using the hashAlgorithm  */
   private boolean hashStorePassword;

   /** A flag indicating if the user supplied password should be hashed using the hashAlgorithm */
   private boolean hashUserPassword = true;
   /** A flag that restores the ability to override the createPasswordHash(String,String) */
   private boolean legacyCreatePasswordHash;
   
   /** A flag that indicates whether validation errors should be exposed to clients or not */
   private boolean throwValidateError = false;
   /** A {@code Throwable} representing the validation error */
   private Throwable validateError; 

   /** The input validator instance used to validate the username and password supplied by the client. */
   private InputValidator inputValidator = null;
   
   /** Override the superclass method to look for the following options after
    first invoking the super version.
    @param options :
    option: hashAlgorithm - the message digest algorithm used to hash passwords.
    If null then plain passwords will be used.
    option: hashCharset - the name of the charset/encoding to use when converting
    the password String to a byte array. Default is the platform's default
    encoding.
    option: hashEncoding - the string encoding format to use. Defaults to base64.
    option: ignorePasswordCase: A flag indicating if the password comparison
      should ignore case.
    option: digestCallback - The class name of the DigestCallback {@link org.jboss.crypto.digest.DigestCallback}
      implementation that includes pre/post digest content like salts for hashing
      the input password. Only used if hashAlgorithm has been specified.
    option: hashStorePassword - A flag indicating if the store password returned
      from #getUsersPassword() should be hashed .
    option: hashUserPassword - A flag indicating if the user entered password should be hashed.
    option: storeDigestCallback - The class name of the DigestCallback {@link org.jboss.crypto.digest.DigestCallback}
      implementation that includes pre/post digest content like salts for hashing
      the store/expected password. Only used if hashStorePassword or hashUserPassword is true and
      hashAlgorithm has been specified.
    */
   @Override
   public void initialize(Subject subject, CallbackHandler callbackHandler,
      Map<String,?> sharedState, Map<String,?> options)
   {
      addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);

      // Check to see if password hashing has been enabled.
      // If an algorithm is set, check for a format and charset.
      hashAlgorithm = (String) options.get(HASH_ALGORITHM);
      if( hashAlgorithm != null )
      {
         hashEncoding = (String) options.get(HASH_ENCODING);
         if( hashEncoding == null )
            hashEncoding = Util.BASE64_ENCODING;
         hashCharset = (String) options.get(HASH_CHARSET);

         PicketBoxLogger.LOGGER.debugPasswordHashing(hashAlgorithm, hashEncoding, hashCharset,
                 (String) options.get(DIGEST_CALLBACK), (String) options.get(STORE_DIGEST_CALLBACK));
      }
      String flag = (String) options.get(IGNORE_PASSWORD_CASE);
      ignorePasswordCase = Boolean.valueOf(flag).booleanValue();
      flag = (String) options.get(HASH_STORE_PASSWORD);
      hashStorePassword = Boolean.valueOf(flag).booleanValue();
      flag = (String) options.get(HASH_USER_PASSWORD);
      if( flag != null )
         hashUserPassword = Boolean.valueOf(flag).booleanValue();
      flag = (String) options.get(LEGACY_CREATE_PASSWORD_HASH);
      if( flag != null )
         legacyCreatePasswordHash = Boolean.valueOf(flag).booleanValue();
      flag = (String) options.get(THROW_VALIDATE_ERROR);
      if(flag != null)
         this.throwValidateError = Boolean.valueOf(flag).booleanValue();
      // instantiate the input validator class.
      flag = (String) options.get(INPUT_VALIDATOR);
      if(flag != null)
      {
         try
         {
            Class<?> validatorClass = SecurityActions.loadClass(flag, jbossModuleName);
            this.inputValidator = (InputValidator) validatorClass.newInstance();
         }
         catch(Exception e)
         {
            PicketBoxLogger.LOGGER.debugFailureToInstantiateClass(flag, e);
         }
      }
   }

   /** Perform the authentication of the username and password.
    */
   @Override
   @SuppressWarnings("unchecked")
   public boolean login() throws LoginException
   {
      // See if shared credentials exist
      if( super.login() == true )
      {
         // Setup our view of the user
         Object username = sharedState.get("javax.security.auth.login.name");
         if( username instanceof Principal )
            identity = (Principal) username;
         else
         {
            String name = username.toString();
            try
            {
               identity = createIdentity(name);
            }
            catch(Exception e)
            {
               LoginException le = PicketBoxMessages.MESSAGES.failedToCreatePrincipal(e.getLocalizedMessage());
               le.initCause(e);
               throw le;
            }
         }
         Object password = sharedState.get("javax.security.auth.login.password");
         if( password instanceof char[] )
            credential = (char[]) password;
         else if( password != null )
         {
            String tmp = password.toString();
            credential = tmp.toCharArray();
         }
         return true;
      }

      super.loginOk = false;
      String[] info = getUsernameAndPassword();
      String username = info[0];
      String password = info[1];
      
      // validate the retrieved username and password.
      if(this.inputValidator != null)
      {
         try
         {
            this.inputValidator.validateUsernameAndPassword(username, password);
         }
         catch(InputValidationException ive)
         {
            throw new FailedLoginException(ive.getLocalizedMessage());
         }
      }

      if( username == null && password == null )
      {
         identity = unauthenticatedIdentity;
         PicketBoxLogger.LOGGER.traceUsingUnauthIdentity(identity != null ? identity.getName() : null);
      }

      if( identity == null )
      {
         try
         {
            identity = createIdentity(username);
         }
         catch(Exception e)
         {
            LoginException le = PicketBoxMessages.MESSAGES.failedToCreatePrincipal(e.getLocalizedMessage());
            le.initCause(e);
            throw le;
         }

         // Hash the user entered password if password hashing is in use
         if( hashAlgorithm != null && hashUserPassword == true )
            password = createPasswordHash(username, password, DIGEST_CALLBACK);
         // Validate the password supplied by the subclass
         String expectedPassword = getUsersPassword();
         //Check if the password is vaultified
         if(SecurityVaultUtil.isVaultFormat(expectedPassword))
         {
        	 try 
        	 {
        		 expectedPassword = SecurityVaultUtil.getValueAsString(expectedPassword);
        	 } 
        	 catch (SecurityVaultException e) 
        	 {
        		 LoginException le = PicketBoxMessages.MESSAGES.unableToGetPasswordFromVault();
        		 le.initCause(e);
        		 throw le;
        	 }
         }
         // Allow the storeDigestCallback to hash the expected password
         if( hashAlgorithm != null && hashStorePassword == true )
            expectedPassword = createPasswordHash(username, expectedPassword, STORE_DIGEST_CALLBACK);
         if( validatePassword(password, expectedPassword) == false )
         {
            Throwable ex = getValidateError();
            FailedLoginException fle = PicketBoxMessages.MESSAGES.invalidPassword();
            PicketBoxLogger.LOGGER.debugBadPasswordForUsername(username);
            if( ex != null && this.throwValidateError)
               fle.initCause(ex);
            throw fle;
         }
      }

      if( getUseFirstPass() == true )
      {    // Add the principal and password to the shared state map
         sharedState.put("javax.security.auth.login.name", identity);
         sharedState.put("javax.security.auth.login.password", credential);
      }
      super.loginOk = true;
      PicketBoxLogger.LOGGER.traceEndLogin(super.loginOk);
      return true;
   }

   @Override
   protected Principal getIdentity()
   {
      return identity;
   }
   @Override
   protected Principal getUnauthenticatedIdentity()
   {
      return unauthenticatedIdentity;
   }

   protected Object getCredentials()
   {
      return credential;
   }
   protected String getUsername()
   {
      String username = null;
      if( getIdentity() != null )
         username = getIdentity().getName();
      return username;
   }

   /** Called by login() to acquire the username and password strings for
    authentication. This method does no validation of either.
    @return String[], [0] = username, [1] = password
    @exception LoginException thrown if CallbackHandler is not set or fails.
    */
   protected String[] getUsernameAndPassword() throws LoginException
   {
      String[] info = {null, null};
      // prompt for a username and password
      if( callbackHandler == null )
      {
         throw PicketBoxMessages.MESSAGES.noCallbackHandlerAvailable();
      }
      
      NameCallback nc = new NameCallback(PicketBoxMessages.MESSAGES.enterUsernameMessage(), "guest");
      PasswordCallback pc = new PasswordCallback(PicketBoxMessages.MESSAGES.enterPasswordMessage(), false);
      Callback[] callbacks = {nc, pc};
      String username = null;
      String password = null;
      try
      {
         callbackHandler.handle(callbacks);
         username = nc.getName();
         char[] tmpPassword = pc.getPassword();
         if( tmpPassword != null )
         {
            credential = new char[tmpPassword.length];
            System.arraycopy(tmpPassword, 0, credential, 0, tmpPassword.length);
            pc.clearPassword();
            password = new String(credential);
         }
      }
      catch(IOException e)
      {
         LoginException le = PicketBoxMessages.MESSAGES.failedToInvokeCallbackHandler();
         le.initCause(e);
         throw le;
      }
      catch(UnsupportedCallbackException e)
      {
         LoginException le = new LoginException();
         le.initCause(e);
         throw le;
      }
      info[0] = username;
      info[1] = password;
      return info;
   }

  /**
   * If hashing is enabled, this method is called from <code>login()</code>
   * prior to password validation.
   * <p>
   * Subclasses may override it to provide customized password hashing,
   * for example by adding user-specific information or salting. If the
   * legacyCreatePasswordHash option is set, this method tries to delegate
   * to the legacy createPasswordHash(String, String) method via reflection
   * and this is the value returned.
   * <p>
   * The default version calculates the hash based on the following options:
   * <ul>
   * <li><em>hashAlgorithm</em>: The digest algorithm to use.
   * <li><em>hashEncoding</em>: The format used to store the hashes (base64 or hex)
   * <li><em>hashCharset</em>: The encoding used to convert the password to bytes
   * for hashing.
   * <li><em>digestCallback</em>: The class name of the
   * org.jboss.security.auth.spi.DigestCallback implementation that includes
   * pre/post digest content like salts.
   * </ul>
   * It will return null if the hash fails for any reason, which will in turn
   * cause <code>validatePassword()</code> to fail.
   * 
   * @param username ignored in default version
   * @param password the password string to be hashed
   * @param digestOption - the login module option name of the DigestCallback
   * @throws SecurityException - thrown if there is a failure to load the
   *  digestOption DigestCallback
   */
   @SuppressWarnings("unchecked")
   protected String createPasswordHash(String username, String password, String digestOption) throws LoginException
   {
      DigestCallback callback = null;
      String callbackClassName = (String) options.get(digestOption);
      if( callbackClassName != null )
      {
         try
         {
            Class<?> callbackClass = SecurityActions.loadClass(callbackClassName, jbossModuleName);
            callback = (DigestCallback) callbackClass.newInstance();
            PicketBoxLogger.LOGGER.traceCreateDigestCallback(callbackClassName);
         }
         catch (Exception e)
         {
            LoginException le = new LoginException(PicketBoxMessages.MESSAGES.failedToInstantiateClassMessage(Callback.class));
            le.initCause(e);
            throw le;
         }
         Map<String,Object> tmp = new HashMap<String,Object>();
         tmp.putAll(options);
         tmp.put("javax.security.auth.login.name", username);
         tmp.put("javax.security.auth.login.password", password);

         callback.init(tmp);
         // Check for a callbacks
         Callback[] callbacks = (Callback[]) tmp.get("callbacks");
         if( callbacks != null )
         {
            try
            {
               callbackHandler.handle(callbacks);
            }
            catch(IOException e)
            {
               LoginException le = PicketBoxMessages.MESSAGES.failedToInvokeCallbackHandler();
               le.initCause(e);
               throw le;
            }
            catch(UnsupportedCallbackException e)
            {
                LoginException le = PicketBoxMessages.MESSAGES.failedToInvokeCallbackHandler();
                le.initCause(e);
               throw le;
            }
         }
      }
      String passwordHash = Util.createPasswordHash(hashAlgorithm, hashEncoding,
         hashCharset, username, password, callback);
      return passwordHash;
   }

   /**
    * Get the error associated with the validatePassword failure
    * @return the Throwable seen during validatePassword, null if no
    * error occurred.
    */
   protected Throwable getValidateError()
   {
      return validateError;
   }

   /**
    * Set the error associated with the validatePassword failure
    * @param validateError
    */
   protected void setValidateError(Throwable validateError)
   {
      PicketBoxLogger.LOGGER.passwordValidationFailed(validateError);
      this.validateError = validateError;
   }

   /** A hook that allows subclasses to change the validation of the input
    password against the expected password. This version checks that
    neither inputPassword or expectedPassword are null that that
    inputPassword.equals(expectedPassword) is true;
    @return true if the inputPassword is valid, false otherwise.
    */
   protected boolean validatePassword(String inputPassword, String expectedPassword)
   {
      if( inputPassword == null || expectedPassword == null )
         return false;
      boolean valid = false;
      if( ignorePasswordCase == true )
         valid = inputPassword.equalsIgnoreCase(expectedPassword);
      else
         valid = slowEquals(inputPassword, expectedPassword);
      return valid;
   }



   /**
    * Compares two strings in length-constant time. This comparison method
    * is used so that passwords or password hashes cannot be extracted
    * using a timing attack.
    *
    * @param stinga the first string
    * @param stringb the second string
    * @return {@code true} if both byte strings are the equal, {@code false} if not
    * @see java.security.MessageDigest#isEqual(byte[], byte[])
    */
   private static boolean slowEquals(String stinga, String stringb)
   {
       int aLength = stinga.length();
       int bLength = stringb.length();
       int diff = aLength ^ bLength;
       int lenght = Math.min(aLength, bLength);
       for(int i = 0; i < lenght; i++)
       {
           diff |= stinga.charAt(i) ^ stringb.charAt(i);
       }
       return diff == 0;
   }


   /** Get the expected password for the current username available via
    the getUsername() method. This is called from within the login()
    method after the CallbackHandler has returned the username and
    candidate password.
    @return the valid password String
    */
   protected abstract String getUsersPassword() throws LoginException;

   protected void safeClose(InputStream fis)
   {
      try
      {
         if(fis != null)
         {
            fis.close();
         }
      }
      catch(Exception e)
      {}
   }
}