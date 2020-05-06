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
package org.jboss.security.javaee;

import java.security.Principal;
import java.util.Map;

import javax.security.auth.Subject;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SecurityContext;
import org.jboss.security.audit.AuditLevel;
import org.jboss.security.identitytrust.IdentityTrustException;
import org.jboss.security.identitytrust.IdentityTrustManager;
import org.jboss.security.identitytrust.IdentityTrustManager.TrustDecision;
 
/**
 *  Helper Class for EJB Authentication
 *  @author Anil.Saldhana@redhat.com
 *  @since  Apr 18, 2008 
 *  @version $Revision$
 */
public class EJBAuthenticationHelper extends AbstractJavaEEHelper
{
   public EJBAuthenticationHelper(SecurityContext sc)
   { 
      if(sc == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("security context");
      this.securityContext = sc;
   }
   
   /**
    * Whether the current caller can be trusted?
    * @return true - trust the caller, false - otherwise
    * @throws IdentityTrustException
    */
   public boolean isTrusted() throws IdentityTrustException
   {
      TrustDecision td = TrustDecision.NotApplicable;
      IdentityTrustManager itm = securityContext.getIdentityTrustManager();
      if(itm != null)
      {
         td = itm.isTrusted(securityContext);
         if(td == TrustDecision.Deny)
            throw new IdentityTrustException(PicketBoxMessages.MESSAGES.deniedByIdentityTrustMessage());
      }
      return td == TrustDecision.Permit;
   }   
   
   /**
    * Authenticate the caller
    * @param subject
    * @param methodName
    * @return
    * @throws IllegalArgumentException subject or methodName is null
    */
   public boolean isValid(Subject subject, String methodName)
   {
      if(subject == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("subject");
      if(methodName == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("method");

      Principal p = securityContext.getUtil().getUserPrincipal();
      Object cred = securityContext.getUtil().getCredential(); 
      
      Map<String,Object> cMap = getContextMap(p, methodName);
      
      boolean auth = securityContext.getAuthenticationManager().isValid(p, cred, subject);
      if(auth == false)
      { 
         // Check for the security association exception
         String EX_KEY = "org.jboss.security.exception"; 
         Exception ex = (Exception) securityContext.getData().get(EX_KEY); 
         if(ex == null)
         { 
            authenticationAudit(AuditLevel.FAILURE,cMap,null);  
         }
         else
         {
            authenticationAudit(AuditLevel.ERROR, cMap ,ex);  
         }
      } 
      else
      {
         authenticationAudit(AuditLevel.SUCCESS,cMap,null);          
      }
      return auth;
   }
   
   /**
    * Push the authenticated subject onto the security context
    * IMPORTANT - this needs to be done after the isValid call
    */
   public void pushSubjectContext(Subject subject)
   { 
      securityContext.getSubjectInfo().setAuthenticatedSubject(subject); 
   }
}