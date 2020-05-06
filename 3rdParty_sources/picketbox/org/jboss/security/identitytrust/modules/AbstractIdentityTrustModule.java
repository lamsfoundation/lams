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
package org.jboss.security.identitytrust.modules;

import java.util.Map;

import javax.security.auth.callback.CallbackHandler;

import org.jboss.security.SecurityContext;
import org.jboss.security.identitytrust.IdentityTrustException;
import org.jboss.security.identitytrust.IdentityTrustModule;
import org.jboss.security.identitytrust.IdentityTrustManager.TrustDecision;


/**
 *  Abstract IdentityTrustModule that pulls in common stuff
 *  @author Anil.Saldhana@redhat.com
 *  @since  Aug 2, 2007 
 *  @version $Revision$
 */
public abstract class AbstractIdentityTrustModule implements IdentityTrustModule
{
   protected SecurityContext securityContext;
   protected CallbackHandler callbackHandler;
   protected Map<String,Object> sharedState;
   protected Map<String,Object> options;
   
   /**
    * @see IdentityTrustModule#abort()
    */
   public boolean abort() throws IdentityTrustException
   {
      return true;
   }

   /**
    * @see IdentityTrustModule#commit()
    */
   public boolean commit() throws IdentityTrustException
   {
      return true;
   }
   
   /**
    * @see IdentityTrustModule#initialize(SecurityContext, CallbackHandler, Map, Map)
    */
   public void initialize(SecurityContext sc, 
         CallbackHandler handler, Map<String,Object> sharedState
         , Map<String,Object> options) 
   throws IdentityTrustException
   { 
      this.securityContext = sc;
      this.callbackHandler = handler;
      this.sharedState = sharedState;
      this.options = options;
   } 
   
   /**
    * @see IdentityTrustModule#isTrusted()
    */
   public abstract TrustDecision isTrusted() throws IdentityTrustException; 
}
