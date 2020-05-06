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
package org.jboss.security;

import java.io.Serializable;

import org.jboss.security.audit.AuditManager;
import org.jboss.security.identitytrust.IdentityTrustManager;
import org.jboss.security.mapping.MappingManager;
 
/**
 *  Interface to obtain the various managers for security
 *  like authentication, authorization, audit, identitytrust etc
 *  @author Anil.Saldhana@redhat.com
 *  @since  Sep 9, 2007 
 *  @version $Revision$
 */
public interface ISecurityManagement extends Serializable
{
   /**
    * Authentication Manager for the security domain
    * @param securityDomain the SecurityDomain
    */
   public AuthenticationManager getAuthenticationManager(String securityDomain); 
   /**
    * Authorization Manager for the security domain 
    * @param securityDomain the SecurityDomain
    * @return 
    */
   public AuthorizationManager getAuthorizationManager(String securityDomain);
   
   /** 
    * Mapping manager configured with providers
    * @param securityDomain the SecurityDomain 
    * @return 
    */ 
   public MappingManager getMappingManager(String securityDomain);
   
   /**
    * AuditManager configured for the security domain 
    * @param securityDomain the SecurityDomain
    * @return 
    */
   public AuditManager getAuditManager(String securityDomain);
   
   /**
    * IdentityTrustManager configured for the security domain
    * @param securityDomain the SecurityDomain
    * @return 
    */
   public IdentityTrustManager getIdentityTrustManager(String securityDomain);
   
   /**
    * JSSE configuration for the security domain
    * 
    * @param securityDomain the SecurityDomain
    * @return
    */
   public JSSESecurityDomain getJSSE(String securityDomain);
}
