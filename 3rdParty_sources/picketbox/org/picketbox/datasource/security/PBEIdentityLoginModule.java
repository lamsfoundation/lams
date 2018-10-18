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

import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.jboss.security.Base64Utils;
import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.Util;

/** An example of how one could encrypt the database password for a jca
  connection factory. The corresponding login config entry illustrates
  the usage:
 
   <application-policy name = "testPBEIdentityLoginModule">
      <authentication>
         <login-module code = "org.jboss.resource.security.PBEIdentityLoginModule"
            flag = "required">
            <module-option name = "principal">sa</module-option>
            <module-option name = "userName">sa</module-option>
            <!--
            output from:
               org.jboss.resource.security.PBEIdentityLoginModule
               thesecret testPBEIdentityLoginModule abcdefgh 19 PBEWithMD5AndDES 
            -->
            <module-option name = "password">3fp7R/7TMjyTTxhmePdJVk</module-option>
            <module-option name = "ignoreMissigingMCF">true</module-option>
            <module-option name = "pbealgo">PBEWithMD5AndDES</module-option>
            <module-option name = "pbepass">testPBEIdentityLoginModule</module-option>
            <module-option name = "salt">abcdefgh</module-option>
            <module-option name = "iterationCount">19</module-option>
            <module-option name = "managedConnectionFactoryName">jboss.jca:service=LocalTxCM,name=DefaultDS</module-option>
         </login-module>
      </authentication>
   </application-policy>

 This uses password based encryption (PBE) with algorithm parameters dervived
 from pbealgo, pbepass, salt, iterationCount options:
 + pbealgo - the PBE algorithm to use. Defaults to PBEwithMD5andDES.
 + pbepass - the PBE password to use. Can use the JaasSecurityDomain {CLASS}
 and {EXT} syntax to obtain the password from outside of the configuration.
 Defaults to "jaas is the way".
 + salt - the PBE salt as a string. Defaults to {1, 7, 2, 9, 3, 11, 4, 13}.
 + iterationCount - the PBE iterationCount. Defaults to 37.

 * @author Scott.Stark@jboss.org
 * @author <a href="mailto:noel.rocher@jboss.org">Noel Rocher</a> 29, june 2004 username & userName issue
 * @version $Revision: 57189 $
 */
public class PBEIdentityLoginModule
   extends AbstractPasswordCredentialLoginModule
{
   private String username;
   private String password;
   /** The Blowfish key material */
   private char[] pbepass = "jaas is the way".toCharArray();
   private String pbealgo = "PBEwithMD5andDES";
   private byte[] salt = {1, 7, 2, 9, 3, 11, 4, 13};
   private int iterationCount = 37;
   private PBEParameterSpec cipherSpec;

   public PBEIdentityLoginModule()
   {
   }
   PBEIdentityLoginModule(String algo, char[] pass, byte[] pbesalt, int iter)
   {
      if (pass != null)
         pbepass = pass;
      if (algo != null)
         pbealgo = algo;
      if (pbesalt != null)
         salt = pbesalt;
      if (iter > 0)
         iterationCount = iter;
   }

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
      // Look for the cipher password and algo parameters
      String tmp = (String) options.get("pbepass");
      if (tmp != null)
      {
         try
         {
            pbepass = Util.loadPassword(tmp);
         }
         catch(Exception e)
         {
            throw new IllegalStateException(e);
         }
      }
      tmp = (String) options.get("pbealgo");
      if (tmp != null)
         pbealgo = tmp;
      tmp = (String) options.get("salt");
      if (tmp != null)
         salt = tmp.substring(0, 8).getBytes();
      tmp = (String) options.get("iterationCount");
      if (tmp != null)
         iterationCount = Integer.parseInt(tmp);
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

   @SuppressWarnings("unchecked")
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

   private String encode(String secret)
      throws Exception
   {
      // Create the PBE secret key
      cipherSpec = new PBEParameterSpec(salt, iterationCount);
      PBEKeySpec keySpec = new PBEKeySpec(pbepass);
      SecretKeyFactory factory = SecretKeyFactory.getInstance(pbealgo);
      SecretKey cipherKey = factory.generateSecret(keySpec);

      // Decode the secret
      Cipher cipher = Cipher.getInstance(pbealgo);
      cipher.init(Cipher.ENCRYPT_MODE, cipherKey, cipherSpec);
      byte[] encoding = cipher.doFinal(secret.getBytes());
      return Base64Utils.tob64(encoding);
   }

   private char[] decode(String secret)
      throws Exception
   {
      // Create the PBE secret key
      cipherSpec = new PBEParameterSpec(salt, iterationCount);
      PBEKeySpec keySpec = new PBEKeySpec(pbepass);
      SecretKeyFactory factory = SecretKeyFactory.getInstance(pbealgo);
      SecretKey cipherKey = factory.generateSecret(keySpec);
      // Decode the secret
      byte[] encoding;
      try {
         encoding = Base64Utils.fromb64(secret);
      }
      catch (IllegalArgumentException e) {
         // fallback when original string is was created with faulty version of Base64 
         encoding = Base64Utils.fromb64("0" + secret);
         PicketBoxLogger.LOGGER.wrongBase64StringUsed("0" + secret);
      }
      Cipher cipher = Cipher.getInstance(pbealgo);
      cipher.init(Cipher.DECRYPT_MODE, cipherKey, cipherSpec);
      byte[] decode = cipher.doFinal(encoding);
      return new String(decode).toCharArray();
   }

   /** Main entry point to encrypt a password using the hard-coded pass phrase 
    * 
    * @param args - [0] = the password to encode
    *    [1] = PBE password 
    *    [2] = PBE salt 
    *    [3] = PBE iterationCount 
    *    [4] = PBE algo 
    * @throws Exception
    */ 
   public static void main(String[] args) throws Exception
   {
      String algo = null;
      char[] pass = "jaas is the way".toCharArray();
      byte[] salt = null;
      int iter = -1;
      if (args.length >= 2)
         pass = args[1].toCharArray();
      if (args.length >= 3)
         salt = args[2].getBytes();
      if (args.length >= 4)
         iter = Integer.decode(args[3]).intValue();
      if (args.length >= 5)
         algo = args[4];

      PBEIdentityLoginModule pbe = new PBEIdentityLoginModule(algo, pass, salt, iter);
      String encode = pbe.encode(args[0]);
      System.out.println("Encoded password: " + encode);
   }
}