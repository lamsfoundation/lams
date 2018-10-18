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
package org.jboss.security.auth.login;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.login.AppConfigurationEntry;

import org.jboss.security.SecurityConstants;
import org.jboss.security.config.BaseSecurityInfo;

// $Id$

/**
 * Base for AuthenticationInfo(JAAS) and JASPIAuthenticationInfo(JSR-196)
 * 
 * @author <a href="mailto:anil.saldhana@jboss.org>Anil.Saldhana@jboss.org</a>
 * @since Dec 21, 2005
 */
public class BaseAuthenticationInfo extends BaseSecurityInfo<Object>
{
   public BaseAuthenticationInfo()
   {
      super();
   }

   public BaseAuthenticationInfo(String name)
   {
      super(name);
   }

   @Override
   protected BaseSecurityInfo<Object> create(String name)
   {
      return new BaseAuthenticationInfo(name);
   }

   /**
    * <p>
    * Gets the application authentication configuration. Execution of this method requires a
    * {@code getLoginConfiguration} permission.
    * </p>
    * 
    * @return an {@code AppConfigurationEntry} array containing the application's authentication configuration.
    */
   public AppConfigurationEntry[] getAppConfigurationEntry()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(GET_CONFIG_ENTRY_PERM);
      AppConfigurationEntry[] entries = new AppConfigurationEntry[super.moduleEntries.size()];
      super.moduleEntries.toArray(entries);
      return entries;
   }

   /**
    * <p>
    * Creates and returns a copy of the application authentication configuration. By default this returns the array
    * created by the {@code copyAppConfigurationEntry(List)} method using the {@code moduleEntries} as a parameter.
    * </p>
    * 
    * @return an {@code AppConfigurationEntry} array containing the copied entries.
    */
   public AppConfigurationEntry[] copyAppConfigurationEntry()
   {
      return this.copyAppConfigurationEntry(super.moduleEntries);
   }

   /**
    * <p>
    * Creates and returns a copy of the specified list of {@code AppConfigurationEntry} objects, adding the security
    * domain option when necessary. Execution of this method requires a {@code getLoginConfiguration} permission.
    * 
    * </p>
    * 
    * @param entries a {@code List} containing the {@code AppConfigurationEntry} objects to be copied.
    * @return an {@code AppConfigurationEntry} array containing the copied entries.
    */
   protected AppConfigurationEntry[] copyAppConfigurationEntry(List<Object> entries)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(GET_CONFIG_ENTRY_PERM);
      AppConfigurationEntry[] copy = new AppConfigurationEntry[entries.size()];
      for (int i = 0; i < copy.length; i++)
      {
         AppConfigurationEntry entry = (AppConfigurationEntry) entries.get(i);
         HashMap<String, Object> options = new HashMap<String, Object>(entry.getOptions());
         if (!disableSecurityDomainInOptions())
         {
            options.put(SecurityConstants.SECURITY_DOMAIN_OPTION, this.getName());
         }
         copy[i] = new AppConfigurationEntry(entry.getLoginModuleName(), entry.getControlFlag(), options);
      }
      return copy;
   }

   /**
    * <p>
    * Checks whether the {@code jboss.security.disable.secdomain.option} system property has been specified with a value
    * of {@code true} or not.
    * </p>
    * 
    * @return {@code true} if the {@code jboss.security.disable.secdomain.option=true} has been specified; {@code false}
    *         otherwise.
    */
   private boolean disableSecurityDomainInOptions()
   {
      String sysprop = AccessController.doPrivileged(new PrivilegedAction<String>()
      {
         public String run()
         {
            return System.getProperty(SecurityConstants.DISABLE_SECDOMAIN_OPTION);
         }
      });
      return "true".equalsIgnoreCase(sysprop);
   }
}
