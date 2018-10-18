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

import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Utility class associated with the {@code TimeBasedOTP} class
 * @author Anil.Saldhana@redhat.com
 * @since Sep 20, 2010
 */
public class TimeBasedOTPUtil
{   
   private static long TIME_INTERVAL = 30 * 1000; //30 secs
   
   /**
   * Validate a submitted OTP string
   * @param submittedOTP OTP string to validate
   * @param secret Shared secret 
   * @return 
   * @throws GeneralSecurityException
   */
  public static boolean validate( String submittedOTP, byte[] secret, int numDigits ) throws GeneralSecurityException
  {
     TimeZone utc = TimeZone.getTimeZone( "UTC" );
     Calendar currentDateTime = Calendar.getInstance( utc );
     
     String generatedTOTP = TimeBasedOTP.generateTOTP( new String( secret ) , numDigits );
     boolean result =  generatedTOTP.equals( submittedOTP );

     long timeInMilis = currentDateTime.getTimeInMillis();
     
     if( !result )
     {   
        timeInMilis -= TIME_INTERVAL;
        
        generatedTOTP = TimeBasedOTP.generateTOTP( new String( secret ) , "" + timeInMilis, numDigits );
        result =  generatedTOTP.equals( submittedOTP );
     }
     
     if( !result )
     {
        timeInMilis += TIME_INTERVAL;
        generatedTOTP = TimeBasedOTP.generateTOTP( new String( secret ) , "" + timeInMilis, numDigits );
        result =  generatedTOTP.equals( submittedOTP );
     }
     
     return result;
  }
  
  /**
   * Validate a submitted OTP string using HMAC_256
   * @param submittedOTP OTP string to validate
   * @param secret Shared secret 
   * @return 
   * @throws GeneralSecurityException
   */
  public static boolean validate256( String submittedOTP, byte[] secret, int numDigits ) throws GeneralSecurityException
  {
     TimeZone utc = TimeZone.getTimeZone( "UTC" );
     Calendar currentDateTime = Calendar.getInstance( utc );
     
     String generatedTOTP = TimeBasedOTP.generateTOTP256( new String( secret ) , numDigits );
     boolean result =  generatedTOTP.equals( submittedOTP );
     
     if( !result )
     {
        //Step back time interval
        long timeInMilis = currentDateTime.getTimeInMillis();
        timeInMilis -= TIME_INTERVAL;
        
        generatedTOTP = TimeBasedOTP.generateTOTP256( new String( secret ) , "" + timeInMilis, numDigits );
        result =  generatedTOTP.equals( submittedOTP );
     }
     
     if( !result )
     {
        //Step ahead time interval
        long timeInMilis = currentDateTime.getTimeInMillis();
        timeInMilis += TIME_INTERVAL;
        
        generatedTOTP = TimeBasedOTP.generateTOTP256( new String( secret ) , "" + timeInMilis, numDigits );
        result =  generatedTOTP.equals( submittedOTP );
     }
     
     return result;
  }
  
  /**
   * Validate a submitted OTP string using HMAC_512
   * @param submittedOTP OTP string to validate
   * @param secret Shared secret 
   * @return 
   * @throws GeneralSecurityException
   */
  public static boolean validate512( String submittedOTP, byte[] secret, int numDigits ) throws GeneralSecurityException
  {
     TimeZone utc = TimeZone.getTimeZone( "UTC" );
     Calendar currentDateTime = Calendar.getInstance( utc );
     
     String generatedTOTP = TimeBasedOTP.generateTOTP512( new String( secret ) , numDigits );
     boolean result =  generatedTOTP.equals( submittedOTP );
     
     if( !result )
     {
        //Step back time interval
        long timeInMilis = currentDateTime.getTimeInMillis();
        timeInMilis -= TIME_INTERVAL;
        
        generatedTOTP = TimeBasedOTP.generateTOTP512( new String( secret ) , "" + timeInMilis, numDigits );
        result =  generatedTOTP.equals( submittedOTP );
     }
     
     if( !result )
     {
        //Step ahead time interval
        long timeInMilis = currentDateTime.getTimeInMillis();
        timeInMilis += TIME_INTERVAL;
        
        generatedTOTP = TimeBasedOTP.generateTOTP512( new String( secret ) , "" + timeInMilis, numDigits );
        result =  generatedTOTP.equals( submittedOTP );
     }
     
     return result;
  }
}