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

import java.lang.SecurityException;
import java.security.KeyStore;
// JSSE key and trust managers
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

/** The SecurityDomain interface combines the SubjectSecurityManager and
 RealmMapping interfaces and adds a keyStore and trustStore as well as
 JSSE KeyManagerFactory and TrustManagerFactory accessors for use with SSL/JSSE.

@see java.security.KeyStore
@see javax.net.ssl.KeyManagerFactory
@see javax.net.ssl.TrustManagerFactory

 * @author  Scott.Stark@jboss.org
 * @version $Revision$
 */
public interface SecurityDomain extends SubjectSecurityManager, RealmMapping
{
   /** Get the keystore associated with the security domain */
   public KeyStore getKeyStore() throws SecurityException;
   /** Get the KeyManagerFactory associated with the security domain */
   public KeyManagerFactory getKeyManagerFactory() throws SecurityException;

   /** Get the truststore associated with the security domain. This may be
    the same as the keystore. */
   public KeyStore getTrustStore() throws SecurityException;
   /** Get the TrustManagerFactory associated with the security domain */
   public TrustManagerFactory getTrustManagerFactory() throws SecurityException;
   
}
