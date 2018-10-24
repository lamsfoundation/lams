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
package org.jboss.security.auth.certs;

import java.security.KeyStore;
import java.security.cert.X509Certificate;

/**
 * A verifier for X509Certificate used by authentication layers.
 * 
 * @see org.jboss.security.auth.spi.BaseCertLoginModule
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public interface X509CertificateVerifier
{
   /**
    * Validate a cert.
    * 
    * @param cert - the X509Certificate to verifier
    * @param alias - the expected keystore alias
    * @param keyStore - the keystore for the cert
    * @param trustStore - the truststore for the cert signer
    * @return true if the cert is valid, false otherwise
    */ 
   public boolean verify(X509Certificate cert, String alias,
      KeyStore keyStore, KeyStore trustStore);
}
