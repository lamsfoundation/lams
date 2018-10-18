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
import java.io.IOException;

import org.jboss.security.PicketBoxMessages;

/** Read a password from the System.in stream. This may be used as a
 password accessor in conjunction with the JaasSecurityDomain
 {CLASS}org.jboss.security.plugins.ConsolePassword
 format of the KeyStorePass attribute.

 @author Scott.Stark@jboss.org
 @version $Revison:$
 */
public class ConsolePassword
{
   public ConsolePassword()
   {
   }

   public char[] toCharArray()
      throws IOException
   {
      System.err.print(PicketBoxMessages.MESSAGES.enterPasswordMessage());
      CharArrayWriter writer = new CharArrayWriter();
      int b;
      while( (b = System.in.read()) >= 0 )
      {
         if( b == '\r' || b == '\n' )
            break;
         writer.write(b);
      }
      char[] password = writer.toCharArray();
      writer.reset();
      for(int n = 0; n < password.length; n ++)
         writer.write('\0');
      return password;
   }

   public static void main(String[] args) throws IOException
   {
      ConsolePassword cp = new ConsolePassword();
      System.out.println(new String(cp.toCharArray()));
   }
}
