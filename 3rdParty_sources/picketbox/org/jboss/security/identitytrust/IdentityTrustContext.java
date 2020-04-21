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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.callback.CallbackHandler;

import org.jboss.security.SecurityContext;
import org.jboss.security.config.ControlFlag;
import org.jboss.security.identitytrust.IdentityTrustManager.TrustDecision;

//$Id$

/**
 *  Identity Trust Context that encloses multiple
 *  IdentityTrustModules making trust decisions
 *  @author Anil.Saldhana@redhat.com
 *  @since  Aug 2, 2007 
 *  @version $Revision$
 */
public abstract class IdentityTrustContext
{
   protected TrustDecision DENY = TrustDecision.Deny;
   protected TrustDecision PERMIT = TrustDecision.Permit;
   protected TrustDecision NOTAPPLICABLE = TrustDecision.NotApplicable;
   
   /**
    * Security Context On which the Trust Context needs
    * to make a decision on. The security domain driving this
    * security context need not be the same as the one for this Trust Context
    */
   protected SecurityContext securityContext;
   
   /**
    * Any Callback Handler 
    */
   protected CallbackHandler callbackHandler;
   
   /**
    * Shared State between trust modules
    */
   protected Map<String,Object> sharedState = new HashMap<String,Object>();
   
   /**
    * Security Domain of the Identity Trust Context
    */
   protected String securityDomain;
   
   /**
    * List of Identity Trust Modules
    */
   protected List<IdentityTrustModule> modules = new ArrayList<IdentityTrustModule>();
   
   /**
    * Control Flags for the individual modules
    */
   protected ArrayList<ControlFlag> controlFlags = new ArrayList<ControlFlag>();
   
   /**
    * Make a trust decision
    * @return {@link TrustDecision#Deny},{@link TrustDecision#NotApplicable},
    *         {@link TrustDecision#Permit}
    * @throws IdentityTrustException
    */
   public abstract TrustDecision isTrusted() throws IdentityTrustException;
}