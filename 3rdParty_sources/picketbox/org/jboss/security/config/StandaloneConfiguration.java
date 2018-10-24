/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.security.config;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

import org.jboss.logging.Logger;
import org.jboss.security.PicketBoxLogger;
import org.jboss.security.SecurityConstants;
import org.jboss.security.auth.login.AuthenticationInfo;
import org.jboss.security.auth.login.BaseAuthenticationInfo;

/**
 * JAAS {@code Configuration} extended with {@code ApplicationPolicy} registration
 * To instantiate, use the {@link #getInstance()} method as this class acts
 * as a singleton 
 * @author Anil.Saldhana@redhat.com
 * @since Jan 24, 2010
 */
public class StandaloneConfiguration extends Configuration implements ApplicationPolicyRegistration
{
   /** The inherited configuration we delegate to */
   protected Configuration parentConfig;
   
   protected ConcurrentMap<String,ApplicationPolicy> appPolicyMap = new ConcurrentHashMap<String, ApplicationPolicy>();
   
   /**
    * Singleton instance
    */
   protected static StandaloneConfiguration _instance;
   
   protected StandaloneConfiguration()
   {   
   }
   
   public static StandaloneConfiguration getInstance()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(StandaloneConfiguration.class.getName() + ".getInstance"));
      }
      if(_instance == null)
        _instance = new StandaloneConfiguration();
     return _instance;
   }
   
   /**
    * @see {@code ApplicationPolicyRegistration#addApplicationPolicy(String, ApplicationPolicy)}
    */
   public void addApplicationPolicy(String appName, ApplicationPolicy aPolicy)
   {
      appPolicyMap.put(appName, aPolicy);
      SecurityConfiguration.addApplicationPolicy(aPolicy);
   }

   /**
    * @see {@code ApplicationPolicyRegistration#getApplicationPolicy(String)}
    */
   public ApplicationPolicy getApplicationPolicy(String domainName)
   {
      return appPolicyMap.get(domainName);
   }

   /**
    * @see {@code ApplicationPolicyRegistration#removeApplicationPolicy(String)}
    */
   public boolean removeApplicationPolicy(String domainName)
   {
      ApplicationPolicy ap = appPolicyMap.remove(domainName);
      return ap != null;
   }
   
   /**
    * Set the Parent Configuration to which we can delegate
    * @param parentConfig
    */
   public void setParentConfig(Configuration parentConfig)
   {
      this.parentConfig = parentConfig;
   }

   @Override
   public AppConfigurationEntry[] getAppConfigurationEntry(String appName)
   {
      AppConfigurationEntry[] entry = null;
      
      ApplicationPolicy aPolicy = getApplicationPolicy(appName);
      BaseAuthenticationInfo authInfo = null;
      if (aPolicy != null)
         authInfo = aPolicy.getAuthenticationInfo();

      if (authInfo == null)
      {
         if (PicketBoxLogger.LOGGER.isTraceEnabled())
         {
            PicketBoxLogger.LOGGER.traceGetAppConfigEntryViaParent(appName, parentConfig != null ? parentConfig.toString() : null);
         }
         if (parentConfig != null)
            entry = parentConfig.getAppConfigurationEntry(appName);
         if (entry == null)
         {
            PicketBoxLogger.LOGGER.traceGetAppConfigEntryViaDefault(appName, SecurityConstants.DEFAULT_APPLICATION_POLICY);
         }
         ApplicationPolicy defPolicy = getApplicationPolicy(SecurityConstants.DEFAULT_APPLICATION_POLICY);
         authInfo = defPolicy != null ? (AuthenticationInfo) defPolicy.getAuthenticationInfo() : null;
      }

      if (authInfo != null)
      {
         if (PicketBoxLogger.LOGGER.isTraceEnabled())
         {
            PicketBoxLogger.LOGGER.traceEndGetAppConfigEntryWithSuccess(appName, authInfo.toString());
         }
         // Make a copy of the authInfo object
         final BaseAuthenticationInfo theAuthInfo = authInfo;
         PrivilegedAction<AppConfigurationEntry[]> action = new PrivilegedAction<AppConfigurationEntry[]>()
         {
            public AppConfigurationEntry[] run()
            {
               return theAuthInfo.copyAppConfigurationEntry();
            }
         };
         entry = AccessController.doPrivileged(action);
      }
      else
      {
         PicketBoxLogger.LOGGER.traceEndGetAppConfigEntryWithFailure(appName);
      }

      return entry; 
   }
}
