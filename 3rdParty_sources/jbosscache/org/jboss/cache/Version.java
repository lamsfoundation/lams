/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2000 - 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.cache;

import net.jcip.annotations.Immutable;

/**
 * Contains version information about this release of JBoss Cache.
 *
 * @author Bela Ban
 *
 */
@Immutable
public class Version
{
   public static final String version = "3.1.0.GA";
   public static final String codename = "Cascabel";

   static final byte[] version_id = {'0', '3', '1', '0', 'G', 'A'};

   private static final int MAJOR_SHIFT = 11;
   private static final int MINOR_SHIFT = 6;
   private static final int MAJOR_MASK = 0x00f800;
   private static final int MINOR_MASK = 0x0007c0;
   private static final int PATCH_MASK = 0x00003f;

   private static final short SHORT_1_2_3 = encodeVersion(1, 2, 3);
   private static final short SHORT_1_2_4_SP2 = encodeVersion(1, 2, 4);

   /**
    * Prints version information.
    */
   public static void main(String[] args)
   {
      System.out.println("\nVersion: \t" + version);
      System.out.println("Codename: \t" + codename);
      //System.out.println("CVS:      \t" + cvs);
      System.out.println("History:  \t(see http://jira.jboss.com/jira/browse/JBCACHE for details)\n");
   }

   /**
    * Returns version information as a string.
    */
   public static String printVersion()
   {
      return "JBossCache '" + codename + "' " + version;// + "[ " + cvs + "]";
   }

   public static String printVersionId(byte[] v, int len)
   {
      StringBuilder sb = new StringBuilder();
      if (v != null)
      {
         if (len <= 0)
         {
            len = v.length;
         }
         for (int i = 0; i < len; i++)
         {
            sb.append((char) v[i]);
         }
      }
      return sb.toString();
   }

   public static String printVersionId(byte[] v)
   {
      StringBuilder sb = new StringBuilder();
      if (v != null)
      {
         for (byte aV : v) sb.append((char) aV);
      }
      return sb.toString();
   }


   public static boolean compareTo(byte[] v)
   {
      if (v == null)
      {
         return false;
      }
      if (v.length < version_id.length)
      {
         return false;
      }
      for (int i = 0; i < version_id.length; i++)
      {
         if (version_id[i] != v[i])
         {
            return false;
         }
      }
      return true;
   }

   public static int getLength()
   {
      return version_id.length;
   }

   public static short getVersionShort()
   {
      return getVersionShort(version);
   }

   public static short getVersionShort(String versionString)
   {
      if (versionString == null)
      {
         throw new IllegalArgumentException("versionString is null");
      }

      // Special cases for version prior to 1.2.4.SP2
      if ("1.2.4".equals(versionString))
      {
         return 124;
      }
      else if ("1.2.4.SP1".equals(versionString))
      {
         return 1241;
      }

      String parts[] = versionString.split("[\\.\\-]");
      int a = 0;
      int b = 0;
      int c = 0;
      if (parts.length > 0)
      {
         a = Integer.parseInt(parts[0]);
      }
      if (parts.length > 1)
      {
         b = Integer.parseInt(parts[1]);
      }
      if (parts.length > 2)
      {
         c = Integer.parseInt(parts[2]);
      }
      return encodeVersion(a, b, c);
   }

   public static String getVersionString(short versionShort)
   {
      if (versionShort == SHORT_1_2_4_SP2)
      {
         return "1.2.4.SP2";
      }

      switch (versionShort)
      {
         case 124:
            return "1.2.4";
         case 1241:
            return "1.2.4.SP1";
         default:
            return decodeVersion(versionShort);
      }
   }

   public static short encodeVersion(int major, int minor, int patch)
   {
      return (short) ((major << MAJOR_SHIFT)
            + (minor << MINOR_SHIFT)
            + patch);
   }

   public static String decodeVersion(short version)
   {
      int major = (version & MAJOR_MASK) >> MAJOR_SHIFT;
      int minor = (version & MINOR_MASK) >> MINOR_SHIFT;
      int patch = (version & PATCH_MASK);
      return major + "." + minor + "." + patch;
   }

   public static boolean isBefore124(short version)
   {
      return (version > 1241 && version <= SHORT_1_2_3);
   }

   /**
    * Retroweaver version info.
    */
   public static class Retro
   {
      public static void main(String[] args)
      {
         System.out.println("\nVersion: \t" + version + " (Retroweaved for JDK 1.4.x compatibility)");
         System.out.println("Codename: \t" + codename);
         //System.out.println("CVS:      \t" + cvs);
         System.out.println("History:  \t(see http://jira.jboss.com/jira/browse/JBCACHE for details)\n");
      }
   }
}
