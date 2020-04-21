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
package org.jboss.security.identitytrust;

import java.util.Map;

import javax.security.auth.callback.CallbackHandler;

import org.jboss.security.SecurityContext;
import org.jboss.security.identitytrust.IdentityTrustManager.TrustDecision;


//$Id$

/**
 *  IdentityTrustModule that is capable of making trust decisions
 *  @author Anil.Saldhana@redhat.com
 *  @since  Aug 2, 2007 
 *  @version $Revision$
 */
public interface IdentityTrustModule
{
   /**
    * Abort the Trust Process
    * @return true -abort process succeeded
    */ 
   boolean abort() throws IdentityTrustException;
   
   /**
    * The IdentityTrust Process has succeeded. The module
    * can commit its decision (maybe to a Database)
    * @return - commit process succeeded
    * @throws IdentityTrustException
    */
   boolean commit() throws IdentityTrustException;
   
   /**
    * Initialize the module with the SecurityContext
    * on which trust decisions will be made
    * @param securityContext
    * @param handler a CallbackHandler if needed
    * @param sharedState a Shared State passed to all modules
    * @param options configured options
    * @throws IdentityTrustException
    */
   void initialize(SecurityContext securityContext, CallbackHandler handler,
         Map<String,Object> sharedState, Map<String,Object> options) throws IdentityTrustException;
   
   /**
    * Make the trust decision
    * @return {@link TrustDecision#Permit}, {@link TrustDecision#Deny},
    *         {@link TrustDecision#NotApplicable}
    * @throws IdentityTrustException
    */
   TrustDecision isTrusted() throws IdentityTrustException;
}