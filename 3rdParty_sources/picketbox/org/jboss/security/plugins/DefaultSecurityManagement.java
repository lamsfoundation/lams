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
package org.jboss.security.plugins;

import javax.security.auth.callback.CallbackHandler;

import org.jboss.security.AuthenticationManager;
import org.jboss.security.AuthorizationManager;
import org.jboss.security.ISecurityManagement;
import org.jboss.security.JBossJSSESecurityDomain;
import org.jboss.security.JSSESecurityDomain;
import org.jboss.security.audit.AuditManager;
import org.jboss.security.identitytrust.IdentityTrustManager;
import org.jboss.security.mapping.MappingManager;
import org.jboss.security.plugins.audit.JBossAuditManager;
import org.jboss.security.plugins.identitytrust.JBossIdentityTrustManager;
import org.jboss.security.plugins.mapping.JBossMappingManager;
 
/**
 *  The Default Security Management class that instantiates the standard 
 *  Security Managers (Authentication, Authorization, Audit, Mapping,IdentityTrust etc)
 *  @author Anil.Saldhana@redhat.com
 *  @since  Sep 9, 2007 
 *  @version $Revision$
 */
public class DefaultSecurityManagement implements ISecurityManagement
{   
   private static final long serialVersionUID = 1L;
   private CallbackHandler handler = null;
   
   public DefaultSecurityManagement( CallbackHandler cbh)
   {   
      this.handler = cbh;
   }
   
   /**
    * @see ISecurityManagement#getAuditManager(String)
    */
   public AuditManager getAuditManager(String securityDomain)
   {
      return new JBossAuditManager(securityDomain);
   }
   
   /**
    * @see ISecurityManagement#getAuthenticationManager(String)
    */
   public AuthenticationManager getAuthenticationManager(String securityDomain)
   {
      return new JBossAuthenticationManager(securityDomain, this.handler);
   }

   /**
    * @see ISecurityManagement#getAuthorizationManager(String)
    */
   public AuthorizationManager getAuthorizationManager(String securityDomain)
   {
      return new JBossAuthorizationManager(securityDomain);
   }

   /**
    * @see ISecurityManagement#getIdentityTrustManager(String)
    */
   public IdentityTrustManager getIdentityTrustManager(String securityDomain)
   {
      return new JBossIdentityTrustManager(securityDomain);
   }

   /**
    * @see ISecurityManagement#getMappingManager(String)
    */
   public MappingManager getMappingManager(String securityDomain)
   {
      return new JBossMappingManager(securityDomain);
   }
   
   /**
    * @see ISecurityManagement#getJSSE(String)
    */
   public JSSESecurityDomain getJSSE(String securityDomain)
   {
      return new JBossJSSESecurityDomain(securityDomain);
   }
}
