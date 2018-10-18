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

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SimplePrincipal;

/** An example of how one could encrypt the database password for a jca
  connection factory. The corresponding  
 
<application-policy name = "HsqlDbRealm">
   <authentication>
      <login-module code = "org.jboss.resource.security.SecureIdentityLoginMdule"
         flag = "required">
         <module-option name = "userName">sa</module-option>
         <module-option name = "password">-207a6df87216de44</module-option>
         <module-option name = "managedConnectionFactoryName">jboss.jca:servce=LocalTxCM,name=DefaultDS</module-option>
      </login-module>
   </authentication>
</application-policy>

 This uses a hard-coded cipher algo of Blowfish, and key derived from the
 phrase 'jaas is the way'. Adjust to your requirements.

 * @author Scott.Stark@jboss.org
 * @author <a href="mailto:noel.rocher@jboss.org">Noel Rocher</a> 29, june 2004 username & userName issue
 * @version $Revision: 71545 $
 */

@SuppressWarnings("unchecked")
public class SecureIdentityLoginModule
   extends AbstractPasswordCredentialLoginModule
{
   private String username;
   private String password;

   @Override
   public void initialize(Subject subject, CallbackHandler handler, Map<String, ?> sharedState, Map<String, ?> options)
   {
      super.initialize(subject, handler, sharedState, options);
      // NR : we keep this username for compatibility
      username = (String) options.get("username");
      if (username == null)
      {
      	// NR : try with userName
        username = (String) options.get("userName");      	
        if (username == null)
        {
         throw new IllegalArgumentException(PicketBoxMessages.MESSAGES.missingRequiredModuleOptionMessage("username"));
        }
     }
      password = (String) options.get("password");
      if (password == null)
      {
         throw new IllegalArgumentException(PicketBoxMessages.MESSAGES.missingRequiredModuleOptionMessage("password"));
      }
   }

   @Override
   public boolean login() throws LoginException
   {
      PicketBoxLogger.LOGGER.traceBeginLogin();
      if (super.login())
         return true;

      super.loginOk = true;
      return true;
   }

   @Override
   public boolean commit() throws LoginException
   {
      Principal principal = new SimplePrincipal(username);
      SubjectActions.addPrincipals(subject, principal);
      sharedState.put("javax.security.auth.login.name", username);
      // Decode the encrypted password
      try
      {
         char[] decodedPassword = decode(password);
         PasswordCredential cred = new PasswordCredential(username, decodedPassword);
         SubjectActions.addCredentials(subject, cred);
      }
      catch(Exception e)
      {
         LoginException le = new LoginException(e.getLocalizedMessage());
         le.initCause(e);
         throw le;
      }
      return true;
   }

   @Override
   public boolean abort()
   {
      username = null;
      password = null;
      return true;
   }

   protected Principal getIdentity()
   {
      PicketBoxLogger.LOGGER.traceBeginGetIdentity(username);
      Principal principal = new SimplePrincipal(username);
      return principal;
   }

   protected Group[] getRoleSets() throws LoginException
   {
      return new Group[] {};
   }

   private static String encode(String secret)
      throws NoSuchPaddingException, NoSuchAlgorithmException,
      InvalidKeyException, BadPaddingException, IllegalBlockSizeException
   {
      byte[] kbytes = "jaas is the way".getBytes();
      SecretKeySpec key = new SecretKeySpec(kbytes, "Blowfish");

      Cipher cipher = Cipher.getInstance("Blowfish");
      cipher.init(Cipher.ENCRYPT_MODE, key);
      byte[] encoding = cipher.doFinal(secret.getBytes());
      BigInteger n = new BigInteger(encoding);
      return n.toString(16);
   }

   private static char[] decode(String secret)
      throws NoSuchPaddingException, NoSuchAlgorithmException,
      InvalidKeyException, BadPaddingException, IllegalBlockSizeException
   {
      byte[] kbytes = "jaas is the way".getBytes();
      SecretKeySpec key = new SecretKeySpec(kbytes, "Blowfish");

      BigInteger n = new BigInteger(secret, 16);
      byte[] encoding = n.toByteArray();
      
      //SECURITY-344: fix leading zeros
      if (encoding.length % 8 != 0)
      {
         int length = encoding.length;
         int newLength = ((length / 8) + 1) * 8;
         int pad = newLength - length; //number of leading zeros
         byte[] old = encoding;
         encoding = new byte[newLength];
         for (int i = old.length - 1; i >= 0; i--)
         {
            encoding[i + pad] = old[i];
         }
         //SECURITY-563: handle negative numbers
         if (n.signum() == -1)
         {
            for (int i = 0; i < newLength - length; i++)
            {
               encoding[i] = (byte) -1;
            }
         }
      }
      
      Cipher cipher = Cipher.getInstance("Blowfish");
      cipher.init(Cipher.DECRYPT_MODE, key);
      byte[] decode = cipher.doFinal(encoding);
      return new String(decode).toCharArray();
   }

   /** Main entry point to encrypt a password using the hard-coded pass phrase 
    * 
    * @param args - [0] = the password to encode
    * @throws Exception
    */ 
   public static void main(String[] args) throws Exception
   {
      String encode = encode(args[0]);
      System.out.println("Encoded password: " + encode);
   }
}