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

import java.math.BigInteger;
import java.security.Principal;
import java.security.cert.X509Certificate;

import org.jboss.security.CertificatePrincipal;
import org.jboss.security.SimplePrincipal;

/** A CertificatePrincipal implementation that builds the principal name
 * based on the cert serialNumber and issuerDN
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class SerialNumberIssuerDNMapping
   implements CertificatePrincipal
{
   /** Create a SimplePrincipal with the name composed from
    * certs[0].getSerialNumber() + " " + certs[0].getIssuerDN()
    *
    * @param certs Array of client certificates, with the first one in
    * the array being the certificate of the client itself.
    */
   public Principal toPrincipal(X509Certificate[] certs)
   {
      BigInteger serialNumber = certs[0].getSerialNumber();
      Principal issuer = certs[0].getIssuerDN();
      SimplePrincipal principal = new SimplePrincipal(serialNumber+" "+issuer);
      return principal;
   }
   
   public Principal toPrinicipal(X509Certificate[] certs)
   {
      return toPrincipal(certs);
   }
}
