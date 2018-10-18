/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.security.auth.spi;

import java.util.Map;
import java.util.HashSet;
import java.util.Arrays;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.jboss.logging.Logger;
import org.jboss.security.PicketBoxLogger;
import org.jboss.security.SecurityConstants;

/**
 * A login module that always fails authentication.
 * It is to be used for a security domain that needs to be disabled, for instance when we don't want JAAS to fallback to
 * using the 'other' security domain.
 * 
 * @author <a href="mmoyses@redhat.com">Marcus Moyses</a>
 */
public class DisabledLoginModule implements LoginModule
{
   // see AbstractServerLoginModule
   private static final String[] ALL_VALID_OPTIONS =
   {
	   SecurityConstants.SECURITY_DOMAIN_OPTION
   };
   
   private static Logger log = Logger.getLogger(DisabledLoginModule.class);
   
   protected String securityDomain;
    
   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
         Map<String, ?> options)
   {
	  /* TODO: this module should really extend AbstractServerLoginModule where the options check is integrated.
	   * the code here has been intentionally kept identical
	   */
      HashSet<String> validOptions = new HashSet<String>(Arrays.asList(ALL_VALID_OPTIONS));
      for (Object key : options.keySet())
      {
    	 if (!validOptions.contains(key))
         {
             PicketBoxLogger.LOGGER.warnInvalidModuleOption((String)key);
         }
      }
	  
	  securityDomain = (String) options.get(SecurityConstants.SECURITY_DOMAIN_OPTION);
   }
 
   public boolean login() throws LoginException
   {
      PicketBoxLogger.LOGGER.errorUsingDisabledDomain(this.securityDomain != null ? this.securityDomain : "");
      return false;
   }
 
   public boolean commit() throws LoginException
   {
      return false;
   }
 
   public boolean abort() throws LoginException
   {
      return false;
   }
 
   public boolean logout() throws LoginException
   {
      return false;
   }
}