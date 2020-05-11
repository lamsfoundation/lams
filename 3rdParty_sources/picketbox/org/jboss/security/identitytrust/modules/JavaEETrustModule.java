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

import org.jboss.security.RunAs;
import org.jboss.security.RunAsIdentity;
import org.jboss.security.SecurityConstants;
import org.jboss.security.identitytrust.IdentityTrustException;
import org.jboss.security.identitytrust.IdentityTrustManager.TrustDecision;


/**
 *  Trust Module that deals with JavaEE RunAsIdentity
 *  We always trust the JavaEE RunAS to bypass authentication
 *  and will be governed by the authorization rules
 *  @author Anil.Saldhana@redhat.com
 *  @since  Aug 2, 2007 
 *  @version $Revision$
 */
public class JavaEETrustModule extends AbstractIdentityTrustModule
{  
   @Override
   public TrustDecision isTrusted() throws IdentityTrustException
   { 
      RunAs runAs = this.securityContext.getIncomingRunAs();
      if(runAs instanceof RunAsIdentity )
      {
         RunAsIdentity runAsIdentity = (RunAsIdentity)runAs;
         if(SecurityConstants.JAVAEE.equals(runAsIdentity.getProof()))
               return TrustDecision.Permit;
      }
      return TrustDecision.NotApplicable;
   }  
}
