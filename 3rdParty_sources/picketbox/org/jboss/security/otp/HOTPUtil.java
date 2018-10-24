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
 * Utility class for {@code HOTP}
 * @author Anil.Saldhana@redhat.com
 * @since Sep 15, 2010
 */
public class HOTPUtil
{
   private static final int MILISECOND_BUFFER = 50; 
   
   /**
    * Validate a submitted OTP string
    * @param submittedOTP OTP string to validate
    * @param secret Shared secret
    * @param timeValueInMins How many mins back we need to check?
    * @return
    * @throws GeneralSecurityException 
    */
   public static boolean validate( String submittedOTP, byte[] secret, int timeValueInMins ) throws GeneralSecurityException
   {
      int codeDigits = 6;
      boolean addChecksum = false;
      int truncationOffset = 0;
      
      TimeZone utc = TimeZone.getTimeZone( "UTC" );
      Calendar currentDateTime = Calendar.getInstance( utc );
      
      long timeInMilis = currentDateTime.getTimeInMillis();
      long movingFactor = timeInMilis;
             
      String otp = HOTP.generateOTP( secret, movingFactor, codeDigits, addChecksum, truncationOffset );
      
      if( otp.equals( submittedOTP ) )
         return true;
      
      int endLimit = timeValueInMins * 60* 1000 + MILISECOND_BUFFER;
      
      for( int i = 1; i < endLimit ; i++ )
      {
         movingFactor --; 
         otp = HOTP.generateOTP( secret, movingFactor, codeDigits, addChecksum, truncationOffset );
         if( otp.equals( submittedOTP ) )
            return true;   
      }
      return false; 
   } 
}