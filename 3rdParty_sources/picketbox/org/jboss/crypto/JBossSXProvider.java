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
package org.jboss.crypto;

/** A Java2 security provider for cryptographic algorithms provided by
 the JBossSX framework.

@author Scott.Stark@jboss.org
@version $Revision$
*/
public class JBossSXProvider extends java.security.Provider
{ 
   private static final long serialVersionUID = -2338131128387727845L;
   public static final String PROVIDER_NAME = "JBossSX";
   public static final String PROVIDER_INFO = "JBossSX Provier Version 1.0";
   public static final double PROVIDER_VERSION = 1.0;

   /** Creates a new instance of Provider */
   public JBossSXProvider()
   {
      this(PROVIDER_NAME, PROVIDER_VERSION, PROVIDER_INFO);
   }
   protected JBossSXProvider(String name, double version, String info)
   {
      super(name, version, info);
      // Setup
      super.put("MessageDigest.SHA_Interleave", "org.jboss.crypto.digest.SHAInterleave");
      super.put("Alg.Alias.MessageDigest.SHA-Interleave", "SHA_Interleave");
      super.put("Alg.Alias.MessageDigest.SHA-SRP", "SHA_Interleave");

      super.put("MessageDigest.SHA_ReverseInterleave", "org.jboss.crypto.digest.SHAReverseInterleave");
      super.put("Alg.Alias.MessageDigest.SHA-SRP-Reverse", "SHA_ReverseInterleave");
   }

}
