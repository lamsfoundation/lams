package org.lamsfoundation.lams.admin.security;

import java.security.Principal;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

public abstract class UsernamePasswordLoginModule extends AbstractServerLoginModule
{
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

   /** Override the superclass method to look for the following options after
	first invoking the super version.
	@param options :
	@option hashAlgorithm: the message digest algorithm used to hash passwords.
	If null then plain passwords will be used.
	@option hashCharset: the name of the charset/encoding to use when converting
	the password String to a byte array. Default is the platform's default
	encoding.
	@option hashEncoding: the string encoding format to use. Defaults to base64.
	@option ignorePasswordCase: A flag indicating if the password comparison
	should ignore case
	*/
   public void initialize(Subject subject, CallbackHandler callbackHandler,
	  Map sharedState, Map options)
   {
	  super.initialize(subject, callbackHandler, sharedState, options);

	  // Check to see if password hashing has been enabled.
	  // If an algorithm is set, check for a format and charset.
	  hashAlgorithm = (String) options.get("hashAlgorithm");
	  if( hashAlgorithm != null )
	  {
		hashEncoding = (String) options.get("hashEncoding");
		if( hashEncoding == null )
			hashEncoding = "BASE64"; //Util.BASE64_ENCODING;
		hashCharset = (String) options.get("hashCharset");
		log.debug("Passworg hashing activated: algorithm = " + hashAlgorithm +
			   ", encoding = " + hashEncoding+ (hashCharset == null ? "" : "charset = " + hashCharset));
	  }
	  String flag = (String) options.get("ignorePasswordCase");
	  ignorePasswordCase = Boolean.valueOf(flag).booleanValue();
   }

   /** Perform the authentication of the username and password.
	*/
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
			   log.debug("Failed to create principal", e);
			   throw new LoginException("Failed to create principal: "+ e.getMessage());
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
	  if( username == null && password == null )
	  {
		 identity = unauthenticatedIdentity;
		 super.log.info("Authenticating as unauthenticatedIdentity="+identity);
	  }

	  if( identity == null )
	  {
		 try
		 {
			identity = createIdentity(username);
		 }
		 catch(Exception e)
		 {
			log.debug("Failed to create principal", e);
			throw new LoginException("Failed to create principal: "+ e.getMessage());
		 }

		 // Hash the user entered password if password hashing is in use
		 if( hashAlgorithm != null )
			password = createPasswordHash(username, password);
		 // Validate the password supplied by the subclass
		 String expectedPassword = getUsersPassword();
		 if( validatePassword(password, expectedPassword) == false )
		 {
			super.log.debug("Bad password for username="+username);
			throw new FailedLoginException("Password Incorrect/Password Required");
		 }
	  }

	  if( getUseFirstPass() == true )
	  {    // Add the username and password to the shared state map
		 sharedState.put("javax.security.auth.login.name", username);
		 sharedState.put("javax.security.auth.login.password", credential);
	  }
	  super.loginOk = true;
	  super.log.info("User '" + identity + "' authenticated, loginOk="+loginOk);
	  return true;
   }

   protected Principal getIdentity()
   {
	  return identity;
   }
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
		 throw new LoginException("Error: no CallbackHandler available " +
		 "to collect authentication information");
	  }
	  NameCallback nc = new NameCallback("User name: ", "guest");
	  PasswordCallback pc = new PasswordCallback("Password: ", false);
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
	  catch(java.io.IOException ioe)
	  {
		 throw new LoginException(ioe.toString());
	  }
	  catch(UnsupportedCallbackException uce)
	  {
		 throw new LoginException("CallbackHandler does not support: " + uce.getCallback());
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
   * for example by adding user-specific information or salting.
   * <p>
   * The default version calculates the hash based on the following options:
   * <ul>
   * <li><em>hashAlgorithm</em>: The digest algorithm to use.
   * <li><em>hashEncoding</em>: The format used to store the hashes (base64 or hex)
   * <li><em>hashCharset</em>: The encoding used to convert the password to bytes
   * for hashing.
   * </ul>
   * It will return null if the hash fails for any reason, which will in turn
   * cause <code>validatePassword()</code> to fail.
   * 
   * @param username ignored in default version
   * @param password the password string to be hashed
   */
   protected String createPasswordHash(String username, String password)
   {
	  String passwordHash = createPasswordHash(hashAlgorithm, hashEncoding,
		 hashCharset, username, password);
	  return passwordHash;
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
		 valid = inputPassword.equals(expectedPassword);
	  return valid;
   }


   /** Get the expected password for the current username available via
	the getUsername() method. This is called from within the login()
	method after the CallbackHandler has returned the username and
	candidate password.
	@return the valid password String
	*/
   abstract protected String getUsersPassword() throws LoginException;

   public static String createPasswordHash(String hashAlgorithm, String hashEncoding,
	  String hashCharset, String username, String password)
   {
	//currently no implementation for password hashing.
	return password;
	/*
	  byte[] passBytes;
	  String passwordHash = null;

	  // convert password to byte data
	  try
	  {
		 if(hashCharset == null)
			passBytes = password.getBytes();
		 else
			passBytes = password.getBytes(hashCharset);
	  }
	  catch(Exception uee)
	  {
		 Logger log = Logger.getLogger(UsernamePasswordLoginModule.class);
		 log.error("charset " + hashCharset + " not found. Using platform default.", uee);
		 passBytes = password.getBytes();
	  }

	  // calculate the hash and apply the encoding.
	  try
	  {
		 byte[] hash = MessageDigest.getInstance(hashAlgorithm).digest(passBytes);
		 if(hashEncoding.equalsIgnoreCase("BASE64"))
		 {
			passwordHash = Util.encodeBase64(hash);
		 }
		 else if(hashEncoding.equalsIgnoreCase("HEX"))
		 {
			passwordHash = Util.encodeBase16(hash);
		 }
		 else
		 {
			Logger log = Logger.getLogger(UsernamePasswordLoginModule.class);
			log.error("Unsupported hash encoding format " + hashEncoding);
		 }
	  }
	  catch(Exception e)
	  {
		 Logger log = Logger.getLogger(UsernamePasswordLoginModule.class);
		 log.error("Password hash calculation failed ", e);
	  }
	  return passwordHash;
	  */
	  
   }
   
}
