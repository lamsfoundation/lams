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

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.jboss.security.PicketBoxLogger;

/** Read a password from a file specified via the ctor and then overwrite
 the file contents with garbage, and then remove it. This may be used as a
 password accessor in conjunction with the JaasSecurityDomain
 {CLASS}org.jboss.security.plugins.TmpFilePassword:password-file
 format of the KeyStorePass attribute.
 
 This class waits until the file exists if it does not when toCharArray()
 is called. It prints out to the console every 10 seconds the path to
 the file it is waiting on until the file is created.

 @author Scott.Stark@jboss.org
 @version $Revison:$
 */
public class TmpFilePassword
{
   private File passwordFile;

   public TmpFilePassword(String file)
   {
      passwordFile = new File(file);
   }

   public char[] toCharArray()
      throws IOException
   {
      while( passwordFile.exists() == false )
      {
         try
         {
            Thread.sleep(10*1000);
         }
         catch(InterruptedException e)
         {
            break;
         }
      }
      FileInputStream fis = null;
      CharArrayWriter writer = null;
      try
      {
         fis = new FileInputStream(passwordFile);
         writer = new CharArrayWriter();
         int b;
         while( (b = fis.read()) >= 0 )
         {
            if( b == '\r' || b == '\n' )
               continue;
            writer.write(b);
         }
      }
      finally
      {
         safeClose(fis);
      }
      try
      {
         fis = new FileInputStream(passwordFile);
         writer = new CharArrayWriter();
         int b;
         while( (b = fis.read()) >= 0 )
         {
            if( b == '\r' || b == '\n' )
               continue;
            writer.write(b);
         }
      }
      finally
      {
         safeClose(fis);
      }
      
      char[] password = writer.toCharArray();
      writer.reset();
      for(int n = 0; n < password.length; n ++)
         writer.write('\0');

      // Overwrite the password file
      RandomAccessFile raf = null;
      try
      {
         raf = new RandomAccessFile(passwordFile, "rws");
         for(int i = 0; i < 10; i ++)
         {
            raf.seek(0);
            for(int j = 0; j < password.length; j ++)
               raf.write(j);
         }
         raf.close();
      }
      catch(Exception e)
      {
          PicketBoxLogger.LOGGER.debugIgnoredException(e);
      }
      finally
      {
         safeClose(raf);
      }
      return password;
   }
   
   private void safeClose(InputStream is)
   {
      try
      {
         if( is != null)
         {
            is.close();
         }
      }
      catch(Exception e)
      {}
   }
   private void safeClose(RandomAccessFile f)
   {
      try
      {
         if(f != null)
         {
            f.close();
         }
      }
      catch(Exception e)
      {}
   }
}