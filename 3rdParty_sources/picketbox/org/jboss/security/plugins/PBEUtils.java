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
package org.jboss.security.plugins;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.jboss.security.Base64Utils;
import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;

/** Ecrypt a password using the JaasSecurityDomain password
 Usage: PBEUtils salt count domain-password password
 salt : the Salt attribute from the JaasSecurityDomain
 count : the IterationCount attribute from the JaasSecurityDomain
 domain-password : the plaintext password that maps to the KeyStorePass
   attribute from the JaasSecurityDomain
 password : the plaintext password that should be encrypted with the
   JaasSecurityDomain password
 
 @author Scott.Stark@jboss.org
 @version $Revison:$
 */
public class PBEUtils
{
   public static byte[] encode(byte[] secret, String cipherAlgorithm,
      SecretKey cipherKey, PBEParameterSpec cipherSpec)
      throws Exception
   {
      Cipher cipher = Cipher.getInstance(cipherAlgorithm);
      cipher.init(Cipher.ENCRYPT_MODE, cipherKey, cipherSpec);
      byte[] encoding = cipher.doFinal(secret);
      return encoding;
   }

   public static String encode64(byte[] secret, String cipherAlgorithm,
      SecretKey cipherKey, PBEParameterSpec cipherSpec)
      throws Exception
   {
      byte[] encoding = encode(secret, cipherAlgorithm, cipherKey, cipherSpec);
      String b64 = Base64Utils.tob64(encoding);
      return b64;
   }

   public static byte[] decode(byte[] secret, String cipherAlgorithm,
      SecretKey cipherKey, PBEParameterSpec cipherSpec)
      throws Exception
   {
      Cipher cipher = Cipher.getInstance(cipherAlgorithm);
      cipher.init(Cipher.DECRYPT_MODE, cipherKey, cipherSpec);
      byte[] decode = cipher.doFinal(secret);
      return decode;
   }

   public static String decode64(String secret, String cipherAlgorithm,
      SecretKey cipherKey, PBEParameterSpec cipherSpec)
      throws Exception
   {
      byte [] encoding;
      try {
         encoding = Base64Utils.fromb64(secret);
      }
      catch (IllegalArgumentException e) {
         // fallback when original string is was created with faulty version of Base64 
         encoding = Base64Utils.fromb64("0" + secret);
         PicketBoxLogger.LOGGER.wrongBase64StringUsed("0" + secret);
      }
      byte[] decode = decode(encoding, cipherAlgorithm, cipherKey, cipherSpec);
      return new String(decode, "UTF-8");
   }

   public static void main(String[] args) throws Exception
   {
      if( args.length != 4 )
      {
         System.err.println(PicketBoxMessages.MESSAGES.pbeUtilsMessage());
      }

      byte[] salt = args[0].substring(0, 8).getBytes();
      int count = Integer.parseInt(args[1]);
      char[] password = args[2].toCharArray();
      byte[] passwordToEncode = args[3].getBytes("UTF-8");
      PBEParameterSpec cipherSpec = new PBEParameterSpec(salt, count);
      PBEKeySpec keySpec = new PBEKeySpec(password);
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEwithMD5andDES");
      SecretKey cipherKey = factory.generateSecret(keySpec);
      String encodedPassword = encode64(passwordToEncode, "PBEwithMD5andDES",
         cipherKey, cipherSpec);
      System.err.println("Encoded password: "+encodedPassword);
   }
}
