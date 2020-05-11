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

import java.security.acl.Group;
import java.util.Iterator;
import java.util.Set;

import javax.naming.InitialContext;
import javax.security.auth.Subject;

import org.jboss.security.audit.AuditManager;
import org.jboss.security.authorization.PolicyRegistration;
import org.jboss.security.config.ApplicationPolicy;
import org.jboss.security.config.SecurityConfiguration;
import org.jboss.security.identitytrust.IdentityTrustManager;
import org.jboss.security.mapping.MappingManager;

/**
 * Security Utility Class
 * 
 * @author Anil.Saldhana@redhat.com
 * @since May 9, 2007
 * @version $Revision$
 */
public class SecurityUtil
{
   private static String LEGACY_JAAS_CONTEXT_ROOT = "java:/jaas/";

   /**
    * Strip the security domain of prefix (java:jaas or java:jbsx)
    * 
    * @param securityDomain
    * @return
    */
   public static String unprefixSecurityDomain(String securityDomain)
   {
      String result = null;
      if (securityDomain != null)
      {
         if (securityDomain.startsWith(SecurityConstants.JAAS_CONTEXT_ROOT))
            result = securityDomain.substring(SecurityConstants.JAAS_CONTEXT_ROOT.length());
         else if (securityDomain.startsWith(SecurityConstants.JASPI_CONTEXT_ROOT))
            result = securityDomain.substring(SecurityConstants.JASPI_CONTEXT_ROOT.length());
         else if (securityDomain.startsWith(LEGACY_JAAS_CONTEXT_ROOT))
            result = securityDomain.substring(LEGACY_JAAS_CONTEXT_ROOT.length());
         else
            result = securityDomain;
      }
      return result;

   }

   /**
    * Get the Subject roles by looking for a Group called 'Roles'
    * 
    * @param theSubject - the Subject to search for roles
    * @return the Group contain the subject roles if found, null otherwise
    */
   public static Group getSubjectRoles(Subject theSubject)
   {
      if (theSubject == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("theSubject");
      Set<Group> subjectGroups = theSubject.getPrincipals(Group.class);
      Iterator<Group> iter = subjectGroups.iterator();
      Group roles = null;
      while (iter.hasNext())
      {
         Group grp = iter.next();
         String name = grp.getName();
         if (name.equals("Roles"))
            roles = grp;
      }
      return roles;
   }

   /**
    * Obtain the Application Policy
    * 
    * @param domainName Security Domain
    * @return
    */
   public static ApplicationPolicy getApplicationPolicy(String domainName)
   {
      return SecurityConfiguration.getApplicationPolicy(domainName);
   }

   /**
    * Do a JNDI lookup to obtain the authentication manager
    * 
    * @param securityDomain
    * @param baseContext the BaseContext for JNDI(Eg: "java:/jaas")
    * @return
    */
   public static AuthenticationManager getAuthenticationManager(String securityDomain, String baseContext)
   {
      String securityMgrURL = "/securityMgr";
      String lookupURL = null;
      if (securityDomain.startsWith(baseContext))
         lookupURL = securityDomain + securityMgrURL;
      else
         lookupURL = baseContext + "/" + securityDomain + securityMgrURL;
      AuthenticationManager am = null;
      try
      {
         InitialContext ic = new InitialContext();
         am = (AuthenticationManager) ic.lookup(lookupURL);
      }
      catch (Exception e)
      {
         PicketBoxLogger.LOGGER.debugIgnoredException(e);
      }
      return am;
   }

   /**
    * Do a JNDI lookup to obtain the authorization manager
    * 
    * @param securityDomain
    * @param baseContext the BaseContext for JNDI(Eg: "java:/jaas")
    * @return
    */
   public static AuthorizationManager getAuthorizationManager(String securityDomain, String baseContext)
   {
      String authorizationMgrURL = "/authorizationMgr";
      String lookupURL = null;
      if (securityDomain.startsWith(baseContext))
         lookupURL = securityDomain + authorizationMgrURL;
      else
         lookupURL = baseContext + "/" + securityDomain + authorizationMgrURL;
      AuthorizationManager am = null;
      try
      {
         InitialContext ic = new InitialContext();
         am = (AuthorizationManager) ic.lookup(lookupURL);
      }
      catch (Exception e)
      {
         PicketBoxLogger.LOGGER.debugIgnoredException(e);
      }
      return am;
   }

   /**
    * Do a JNDI lookup to obtain the Audit Manager
    * 
    * @param securityDomain
    * @param baseContext the BaseContext for JNDI(Eg: "java:/jaas")
    * @return
    */
   public static AuditManager getAuditManager(String securityDomain, String baseContext)
   {
      String auditMgrURL = "/auditMgr";
      String lookupURL = null;
      if (securityDomain.startsWith(baseContext))
         lookupURL = securityDomain + auditMgrURL;
      else
         lookupURL = baseContext + "/" + securityDomain + auditMgrURL;
      AuditManager am = null;
      try
      {
         InitialContext ic = new InitialContext();
         am = (AuditManager) ic.lookup(lookupURL);
      }
      catch (Exception e)
      {
         PicketBoxLogger.LOGGER.debugIgnoredException(e);
      }
      return am;
   }

   /**
    * Do a JNDI lookup to obtain the IdentityTrust Manager
    * 
    * @param securityDomain
    * @param baseContext the BaseContext for JNDI(Eg: "java:/jaas")
    * @return
    */
   public static IdentityTrustManager getIdentityTrustManager(String securityDomain, String baseContext)
   {
      String identityTrustMgrURL = "/identityTrustMgr";
      String lookupURL = null;
      if (securityDomain.startsWith(baseContext))
         lookupURL = securityDomain + identityTrustMgrURL;
      else
         lookupURL = baseContext + "/" + securityDomain + identityTrustMgrURL;
      IdentityTrustManager am = null;
      try
      {
         InitialContext ic = new InitialContext();
         am = (IdentityTrustManager) ic.lookup(lookupURL);
      }
      catch (Exception e)
      {
         PicketBoxLogger.LOGGER.debugIgnoredException(e);
      }
      return am;
   }

   /**
    * Do a JNDI lookup to obtain the MappingManager
    * 
    * @param securityDomain
    * @param baseContext the BaseContext for JNDI(Eg: "java:/jaas")
    * @return
    */
   public static MappingManager getMappingManager(String securityDomain, String baseContext)
   {
      String mappingManagerURL = "/mappingMgr";
      String lookupURL = null;
      if (securityDomain.startsWith(baseContext))
         lookupURL = securityDomain + mappingManagerURL;
      else
         lookupURL = baseContext + "/" + securityDomain + mappingManagerURL;
      MappingManager am = null;
      try
      {
         InitialContext ic = new InitialContext();
         am = (MappingManager) ic.lookup(lookupURL);
      }
      catch (Exception e)
      {
         PicketBoxLogger.LOGGER.debugIgnoredException(e);
      }
      return am;
   }

   /**
    * <p>
    * Performs a JNDI lookup to retrieve the configured {@code PolicyRegistration}.
    * </p>
    * 
    * @return a reference to the configured {@code PolicyRegistration} implementation, or {@code null} if the look up
    *         fails.
    */
   public static PolicyRegistration getPolicyRegistration()
   {
      String lookupURL = "java:/policyRegistration";
      PolicyRegistration registration = null;
      try
      {
         InitialContext ic = new InitialContext();
         registration = (PolicyRegistration) ic.lookup(lookupURL);
      }
      catch (Exception e)
      {
         PicketBoxLogger.LOGGER.debugIgnoredException(e);
      }
      return registration;
   }
}