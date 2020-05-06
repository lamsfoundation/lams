/*
  * JBoss, Home of Professional Open Source
  * Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.security.identity.extensions;

import java.security.Principal;
import java.security.acl.Group;
import java.security.cert.X509Certificate;

import org.jboss.security.identity.IdentityFactory;
import org.jboss.security.identity.Role;
 
/**
 *  Factory to create Certificate Identities
 *  @author Anil.Saldhana@redhat.com
 *  @since  Apr 23, 2008 
 *  @version $Revision$
 */
public class CertificateIdentityFactory extends IdentityFactory
{
   private static CertificateIdentityFactory _instance = null;
   
   protected CertificateIdentityFactory()
   {   
   }
   
   public static CertificateIdentityFactory getInstance()
   {
      if(_instance == null)
         _instance = new CertificateIdentityFactory();
      return _instance;
   }
   
   public CertificateIdentity createIdentity(final Principal principal,
         final X509Certificate[] certs, final Role roles)
   {
      return new CertificateIdentity()
      { 
         private static final long serialVersionUID = 1L;

         public X509Certificate[] getCredential()
         { 
            return certs;
         }

         public void setCredential(X509Certificate[] certs)
         {
         }

         public Group asGroup()
         { 
            return null;
         }

         public Principal asPrincipal()
         {
            return principal;
         }

         public String getName()
         {
            return principal.getName();
         }

         public Role getRole()
         {
            return roles;
         }

         @Override
         public String toString()
         {
            StringBuilder builder = new StringBuilder();
            builder.append("CertificateIdentity[").append(" Certs=").append(certs).append("]");
            return builder.toString();
         }  
      };  
   } 
}