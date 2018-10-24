/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.picketbox.factories;

import java.net.URL;

import javax.security.auth.login.Configuration;

import org.jboss.security.AuthenticationManager;
import org.jboss.security.AuthorizationManager;
import org.jboss.security.ISecurityManagement;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextFactory;
import org.jboss.security.audit.AuditManager;
import org.jboss.security.config.ApplicationPolicyRegistration;
import org.jboss.security.config.StandaloneConfiguration;
import org.jboss.security.mapping.MappingManager;
import org.picketbox.plugins.PicketBoxSecurityManagement;

/**
 * Security Factory
 * This is the main factory for PicketBox
 * 
 * Two methods that are important are {@link #prepare()} and {@link #release()}
 * <a href="mailto:anil.saldhana@redhat.com">Anil Saldhana</a>
 */
public class SecurityFactory
{
   private static ISecurityManagement securityManagement = new PicketBoxSecurityManagement();
   
   private static Configuration parentConfiguration = null;

   private static String AUTH_CONF_FILE = "auth.conf";

   private static String AUTH_CONF_SYSPROP = "java.security.auth.login.config";

   static
   { 
      try
      {
         ClassLoader tcl = SecurityActions.getContextClassLoader();
         if( tcl == null )
            throw PicketBoxMessages.MESSAGES.invalidThreadContextClassLoader();
         URL configLocation = tcl.getResource(AUTH_CONF_FILE);
         if(SecurityActions.getSystemProperty(AUTH_CONF_SYSPROP, null) == null)
         {
            if( configLocation == null )
               throw PicketBoxMessages.MESSAGES.invalidNullLoginConfig();

            SecurityActions.setSystemProperty(AUTH_CONF_SYSPROP, configLocation.toExternalForm());
         }
         
         parentConfiguration = Configuration.getConfiguration();
      }
      catch(Exception e)
      {
         throw PicketBoxMessages.MESSAGES.unableToInitSecurityFactory(e);
      }
   }
   
   private static StandaloneConfiguration standaloneConfiguration = StandaloneConfiguration.getInstance();
   
   /**
    * Get the {@code AuthenticationManager} interface
    * @param securityDomain security domain such as "other"
    * @return
    */
   public static AuthenticationManager getAuthenticationManager(String securityDomain)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() + ".getAuthenticationManager"));
      }
      validate();
      return securityManagement.getAuthenticationManager(securityDomain);
   }
   
   /**
    * Get the {@code AuthorizationManager} interface
    * @param securityDomain security domain such as "other"
    * @return
    */
   public static AuthorizationManager getAuthorizationManager(String securityDomain)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName()+ ".getAuthorizationManager"));
      }
      validate();
      return securityManagement.getAuthorizationManager(securityDomain);
   }
   
   /**
    * Get the {@code AuditManager} interface
    * @param securityDomain security domain such as "other"
    * @return
    */
   public static AuditManager getAuditManager(String securityDomain)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() + ".getAuditManager"));
      }
      validate();
      return securityManagement.getAuditManager(securityDomain);
   }
   
   /**
    * Get the {@code MappingManager}
    * @param securityDomain
    * @return
    */
   public static MappingManager getMappingManager(String securityDomain)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() + ".getMappingManager"));
      }
      validate();
      return securityManagement.getMappingManager(securityDomain);
   }
   
   /**
    * Get the {@code ISecurityManagement} interface  
    * @return
    */
   public static ISecurityManagement getSecurityManagement()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() + ".getSecurityManagement"));
      }
      return securityManagement;
   }
   
   /**
    * Set {@code ISecurityManagement}
    * @param iSecurityManagement
    */
   public static void setSecurityManagement(ISecurityManagement iSecurityManagement)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() + ".setSecurityManagement"));
      }
      securityManagement = iSecurityManagement;
   }
 
   /**
    * Prepare for security operations. One of the operations
    * that is undertaken is to establish the JAAS {@code Configuration}
    * that uses our xml based configuration.
    * @see #release() to release the configuration
    */
   public static void prepare()
   { 
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() +  ".prepare"));
      }
      if(Configuration.getConfiguration() instanceof ApplicationPolicyRegistration == false)
      {
         standaloneConfiguration.setParentConfig(parentConfiguration);
         Configuration.setConfiguration(standaloneConfiguration);
      }
   }
   
   /**
    * Establish a security context on the thread
    * @param securityDomainName
    */
   public static SecurityContext establishSecurityContext(String securityDomainName)
   { 
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() + ".establishSecurityContext"));
      }
      SecurityContext securityContext = null;
      try
      {
         securityContext = SecurityContextFactory.createSecurityContext(securityDomainName);
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
      SecurityActions.setSecurityContext(securityContext);
      return securityContext;
   }
   
   /**
    * Will release anything that was done during {@link #prepare()} step
    */
   public static void release()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() + ".release"));
      }
      Configuration config = Configuration.getConfiguration();
      if(config == standaloneConfiguration)
      {
         Configuration.setConfiguration(parentConfiguration); //Set back the previously valid configuration
      }
   }
   
   private static void validate()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityFactory.class.getName() + ".validate"));
      }
      assert(securityManagement != null);
   }
}