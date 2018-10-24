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
package org.jboss.security;

import java.io.ByteArrayOutputStream;

/**
 * Base64 encoding/decoding utilities. This implementation is not MIME compliant (rfc1421). The padding in this implementation
 * (if used) is a prefix of the output.
 * 
 * @author Scott.Stark@jboss.org
 * @author Josef Cacek
 */
public class Base64Utils
{

   private static final String base64Str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz./";
   private static final char[] base64Table = base64Str.toCharArray();
   public static final String BASE64_ENCODING = "BASE64";
   public static final String BASE16_ENCODING = "HEX";
   public static final char PAD = '_';
   public static final String REGEX = "^" + PAD + "{0,2}[" + base64Str + "]*$";

   public static String tob64(byte[] buffer)
   {
      return tob64(buffer, false);
   }

   public static String tob64(byte[] buffer, boolean usePadding)
   {
      int len = buffer.length, pos = len % 3, c;
      byte b0 = 0, b1 = 0, b2 = 0;
      StringBuffer sb = new StringBuffer();

      int i = 0;
      if (usePadding)
      {
         for (i = pos; i != 0; i = (i + 1) % 3)
         {
            sb.append(PAD);
         }
         i = 0;
      }
      switch (pos)
      {
         case 2:
            b1 = buffer[i++];
            c = ((b0 & 3) << 4) | ((b1 & 0xf0) >>> 4);
            sb.append(base64Table[c]);
         case 1:
            b2 = buffer[i++];
            c = ((b1 & 0xf) << 2) | ((b2 & 0xc0) >>> 6);
            sb.append(base64Table[c]);
            c = b2 & 0x3f;
            sb.append(base64Table[c]);
            break;
      }

      while (pos < len)
      {
         b0 = buffer[pos++];
         b1 = buffer[pos++];
         b2 = buffer[pos++];
         c = (b0 & 0xfc) >>> 2;
         sb.append(base64Table[c]);
         c = ((b0 & 3) << 4) | ((b1 & 0xf0) >>> 4);
         sb.append(base64Table[c]);
         c = ((b1 & 0xf) << 2) | ((b2 & 0xc0) >>> 6);
         sb.append(base64Table[c]);
         c = b2 & 0x3f;
         sb.append(base64Table[c]);
      }

      return sb.toString();
   }

   public static byte[] fromb64(String str) throws NumberFormatException
   {
      if (str.length() == 0)
      {
         return new byte[0];
      }

      while (str.length() % 4 != 0)
      {
         str = PAD + str;
      }
      if (!str.matches(REGEX))
      {
         throw PicketBoxMessages.MESSAGES.invalidBase64String(str);
      }
      ByteArrayOutputStream bos = new ByteArrayOutputStream((str.length() * 3) / 4);
      for (int i = 0, n = str.length(); i < n;)
      {
         int pos0 = base64Str.indexOf(str.charAt(i++));
         int pos1 = base64Str.indexOf(str.charAt(i++));
         int pos2 = base64Str.indexOf(str.charAt(i++));
         int pos3 = base64Str.indexOf(str.charAt(i++));
         if (pos0 > -1)
         {
            bos.write(((pos1 & 0x30) >>> 4) | (pos0 << 2));
         }
         if (pos1 > -1)
         {
            bos.write(((pos2 & 0x3c) >>> 2) | ((pos1 & 0xf) << 4));
         }
         bos.write(((pos2 & 3) << 6) | pos3);
      }
      return bos.toByteArray();
   }

}
