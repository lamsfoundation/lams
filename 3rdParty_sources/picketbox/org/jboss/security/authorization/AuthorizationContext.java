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
package org.jboss.security.authorization;
  
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

import org.jboss.security.identity.RoleGroup;
 

//$Id: AuthorizationContext.java 62954 2007-05-10 04:12:18Z anil.saldhana@jboss.com $

/**
 *  JBAS-3374: Authorization Framework for Policy Decision Modules
 *  For information on the behavior of the Authorization Modules,
 *  For Authorization Modules behavior(Required, Requisite, Sufficient and Optional)
 *  please refer to the javadoc for @see javax.security.auth.login.Configuration
 *  
 *  The AuthorizationContext derives the AuthorizationInfo(configuration for the modules)
 *  in the following way:
 *  a) If there has been an injection of ApplicationPolicy, then it will be used.
 *  b) Util.getApplicationPolicy will be used(which relies on SecurityConfiguration static class).
 *  c) Flag an error that there is no available Application Policy
 *  
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jun 11, 2006 
 *  @version $Revision: 62954 $
 */
public abstract class AuthorizationContext
{   
   protected String securityDomainName = null;
   protected CallbackHandler callbackHandler = null; 
   protected Map<String,Object> sharedState = new HashMap<String,Object>(); 
   
   public static final int PERMIT = 1;
   public static final int DENY = -1;  
   
   /**
    * Authorize the Resource 
    * @param resource
    * @return AuthorizationContext.PERMIT or AuthorizationContext.DENY
    * @throws AuthorizationException
    */
   public abstract int authorize(final Resource resource) throws AuthorizationException;
   
   /**
    * Authorize the resource
    * @param resource
    * @param subject Subject of the caller
    * @param roles Roles of the caller
    * @return
    * @throws AuthorizationException
    */
   public abstract int authorize(final Resource resource, final Subject subject,
         final RoleGroup roles)
   throws AuthorizationException;

   /**
    * Return the Security Domain Name
    * @return security domain
    */
   public String getSecurityDomain()
   {
     return this.securityDomainName;   
   }
}