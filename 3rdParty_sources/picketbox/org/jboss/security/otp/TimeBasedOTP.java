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
package org.jboss.security.otp;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * TOTP: Time-based One-time Password Algorithm
 * Based on http://tools.ietf.org/html/draft-mraihi-totp-timebased-06
 * 
 * @author Anil.Saldhana@redhat.com
 * @since Sep 20, 2010
 */
public class TimeBasedOTP
{
   public static final String HMAC_SHA1 = "HmacSHA1";

   public static final String HMAC_SHA256 = "HmacSHA256";
   
   public static final String HMAC_SHA512 = "HmacSHA512";
   
   // 0 1  2   3    4     5      6       7        8
   private static final int[] DIGITS_POWER  = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000 }; 

   private static int TIME_SLICE_X = 30000;
   private static int TIME_ZERO = 0;
   
   
   /**
    * Generate a TOTP value using HMAC_SHA1
    * @param key
    * @param returnDigits
    * @return
    * @throws GeneralSecurityException
    */
   public static String generateTOTP( String key, int returnDigits ) throws GeneralSecurityException
   {
      TimeZone utc = TimeZone.getTimeZone( "UTC" );
      Calendar currentDateTime = Calendar.getInstance( utc );
      long timeInMilis = currentDateTime.getTimeInMillis();
       
      String steps = "0";
      long T = ( timeInMilis - TIME_ZERO ) /  TIME_SLICE_X ; 
      steps = Long.toHexString( T ).toUpperCase(Locale.ENGLISH);
      
      // Just get a 16 digit string
      while(steps.length() < 16) 
         steps = "0" + steps;
      return TimeBasedOTP.generateTOTP( key, steps, returnDigits); 
   }
   
   /**
    * Generate a TOTP value using HMAC_SHA256
    * @param key
    * @param returnDigits
    * @return
    * @throws GeneralSecurityException
    */
   public static String generateTOTP256( String key, int returnDigits ) throws GeneralSecurityException
   {
      TimeZone utc = TimeZone.getTimeZone( "UTC" );
      Calendar currentDateTime = Calendar.getInstance( utc );
      long timeInMilis = currentDateTime.getTimeInMillis();
       
      String steps = "0";
      long T = ( timeInMilis - TIME_ZERO ) /  TIME_SLICE_X ; 
      steps = Long.toHexString( T ).toUpperCase(Locale.ENGLISH);
      
      // Just get a 16 digit string
      while(steps.length() < 16) 
         steps = "0" + steps;
      return TimeBasedOTP.generateTOTP256( key, steps, returnDigits); 
   }
   
   /**
    * Generate a TOTP value using HMAC_SHA512
    * @param key
    * @param returnDigits
    * @return
    * @throws GeneralSecurityException
    */
   public static String generateTOTP512( String key, int returnDigits ) throws GeneralSecurityException
   {
      TimeZone utc = TimeZone.getTimeZone( "UTC" );
      Calendar currentDateTime = Calendar.getInstance( utc );
      long timeInMilis = currentDateTime.getTimeInMillis();
       
      String steps = "0";
      long T = ( timeInMilis - TIME_ZERO ) /  TIME_SLICE_X ; 
      steps = Long.toHexString( T ).toUpperCase(Locale.ENGLISH);
      
      // Just get a 16 digit string
      while(steps.length() < 16) 
         steps = "0" + steps;
      return TimeBasedOTP.generateTOTP512( key, steps, returnDigits); 
   }
   
   /**
    * This method generates an TOTP value for the given
    * set of parameters.
    *
    * @param key   the shared secret, HEX encoded
    * @param time     a value that reflects a time
    * @param returnDigits     number of digits to return
    *
    * @return      A numeric String in base 10 that includes
    *              {@link truncationDigits} digits
    * @throws GeneralSecurityException 
    */
   public static String generateTOTP( String key,   String time, int returnDigits ) throws GeneralSecurityException
   {
      return generateTOTP( key, time, returnDigits, HMAC_SHA1 );
   }


   /**
    * This method generates an TOTP value for the given
    * set of parameters.
    *
    * @param key   the shared secret, HEX encoded
    * @param time     a value that reflects a time
    * @param returnDigits     number of digits to return
    *
    * @return      A numeric String in base 10 that includes
    *              {@link truncationDigits} digits
    * @throws GeneralSecurityException 
    */
   public static String generateTOTP256(String key, String time, int returnDigits) throws GeneralSecurityException
   {
      return generateTOTP( key, time, returnDigits, HMAC_SHA256 );
   }


   /**
    * This method generates an TOTP value for the given
    * set of parameters.
    *
    * @param key   the shared secret, HEX encoded
    * @param time     a value that reflects a time
    * @param returnDigits     number of digits to return
    *
    * @return      A numeric String in base 10 that includes
    *              {@link truncationDigits} digits
    * @throws GeneralSecurityException 
    */
   public static String generateTOTP512(String key, String time, int returnDigits) throws GeneralSecurityException
   {
      return generateTOTP( key, time, returnDigits, HMAC_SHA512 );
   }
  
   /**
    * This method generates an TOTP value for the given
    * set of parameters.
    *
    * @param key   the shared secret, HEX encoded
    * @param time     a value that reflects a time
    * @param returnDigits     number of digits to return
    * @param crypto    the crypto function to use
    *
    * @return      A numeric String in base 10 that includes
    *              {@link truncationDigits} digits
    * @throws GeneralSecurityException 
    */
   public static String generateTOTP(String key, String time,  int returnDigits, String crypto) throws GeneralSecurityException
   { 
      String result = null;
      byte[] hash;

      // Using the counter
      // First 8 bytes are for the movingFactor
      // Complaint with base RFC 4226 (HOTP)
      while(time.length() < 16 )
         time = "0" + time;

      // Get the HEX in a Byte[]
      byte[] msg = hexStr2Bytes(time);

      // Adding one byte to get the right conversion
      byte[] k = hexStr2Bytes(key);

      hash = hmac_sha1(crypto, k, msg);

      // put selected bytes into result int
      int offset = hash[hash.length - 1] & 0xf;

      int binary =
         ((hash[offset] & 0x7f) << 24) |
         ((hash[offset + 1] & 0xff) << 16) |
         ((hash[offset + 2] & 0xff) << 8) |
         (hash[offset + 3] & 0xff);

      int otp = binary % DIGITS_POWER[ returnDigits ];

      result = Integer.toString(otp);
      while (result.length() < returnDigits ) {
         result = "0" + result;
      }
      return result;
   }
   
   /**
    * This method uses the JCE to provide the crypto
    * algorithm.
    * HMAC computes a Hashed Message Authentication Code with the
    * crypto hash algorithm as a parameter.
    *
    * @param crypto     the crypto algorithm (HmacSHA1, HmacSHA256,
    *                            HmacSHA512)
    * @param keyBytes   the bytes to use for the HMAC key
    * @param text       the message or text to be authenticated.
    * @throws NoSuchAlgorithmException 
    * @throws InvalidKeyException 
    */
   private static byte[] hmac_sha1(String crypto, byte[] keyBytes, byte[] text) throws GeneralSecurityException
   {
      Mac hmac;
      hmac = Mac.getInstance(crypto);
      SecretKeySpec macKey =
         new SecretKeySpec(keyBytes, "RAW");
      hmac.init(macKey);
      return hmac.doFinal(text);
   }


   /**
    * This method converts HEX string to Byte[]
    *
    * @param hex   the HEX string
    *
    * @return      A byte array
    */
   private static byte[] hexStr2Bytes(String hex)
   {
      // Adding one byte to get the right conversion
      // values starting with "0" can be converted
      byte[] bArray = new BigInteger("10" + hex,16).toByteArray();

      // Copy all the REAL bytes, not the "first"
      byte[] ret = new byte[bArray.length - 1];
      for (int i = 0; i < ret.length ; i++)
         ret[i] = bArray[i+1];
      return ret;
   }
}