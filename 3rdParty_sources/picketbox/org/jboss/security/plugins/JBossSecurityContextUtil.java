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
package org.jboss.security.plugins;

import static org.jboss.security.SecurityConstants.CALLER_RAI_IDENTIFIER;
import static org.jboss.security.SecurityConstants.ROLES_IDENTIFIER;
import static org.jboss.security.SecurityConstants.RUNAS_IDENTITY_IDENTIFIER;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;

import javax.security.auth.Subject;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.RunAs;
import org.jboss.security.RunAsIdentity;
import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextUtil;
import org.jboss.security.SecurityIdentity;
import org.jboss.security.SubjectInfo;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.identity.extensions.CredentialIdentity;

//$Id$

/**
 *  Utility class for JBossSecurityContext implementation
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jan 5, 2007 
 *  @version $Revision$
 */
public class JBossSecurityContextUtil extends SecurityContextUtil
{  
   public JBossSecurityContextUtil(SecurityContext sc)
   {
      this.securityContext = sc;
   }
   
   @SuppressWarnings("unchecked")
   @Override
   public <T> T get(String key)
   { 
      validateSecurityContext();
      if(RUNAS_IDENTITY_IDENTIFIER.equals(key))
         return (T)securityContext.getOutgoingRunAs();
      else
         return (T) securityContext.getData().get(key);
   }

   @Override
   public String getUserName()
   {  
      Principal p = getUserPrincipal();
      return p != null ? p.getName() : null;
   }

   @Override
   public Principal getUserPrincipal()
   {  
      validateSecurityContext(); 
      Principal p = null; 
      SubjectInfo subjectInfo = this.securityContext.getSubjectInfo();
      if(subjectInfo != null)
      {
         CredentialIdentity<?> cIdentity = subjectInfo.getIdentity(CredentialIdentity.class);
         p = cIdentity != null ? cIdentity.asPrincipal() : null;
      }
      return p;
   }
   
   public Object getCredential()
   {
      validateSecurityContext(); 
      Object cred = null;
      SubjectInfo subjectInfo = this.securityContext.getSubjectInfo();
      if(subjectInfo != null)
      {
         CredentialIdentity<?> cIdentity = subjectInfo.getIdentity(CredentialIdentity.class);
         cred = cIdentity != null ? cIdentity.getCredential(): null;
      }
      return cred; 
   }
   
   public Subject getSubject()
   {
      validateSecurityContext(); 
      Subject s = null;
      SubjectInfo subjectInfo = this.securityContext.getSubjectInfo();
      if(subjectInfo != null)
      {
         s = subjectInfo.getAuthenticatedSubject();
      }
      return s;
   }

   @Override
   public <T> void set(String key, T obj)
   {   
      validateSecurityContext();
      if(key == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("key");
      if(obj != null)
      {
         if(RUNAS_IDENTITY_IDENTIFIER.equals(key) && obj instanceof RunAsIdentity == false)
            throw PicketBoxMessages.MESSAGES.invalidType(RunAsIdentity.class.getName());
         if(ROLES_IDENTIFIER.equals(key) &&  obj instanceof Group == false)
            throw PicketBoxMessages.MESSAGES.invalidType(Group.class.getName());
      }
      if(RUNAS_IDENTITY_IDENTIFIER.equals(key))
         setRunAsIdentity( (RunAsIdentity) obj);
      else
         securityContext.getData().put(key, obj);
   } 

   @SuppressWarnings("unchecked")
   @Override
   public <T> T remove(String key)
   { 
      if(key == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("key");
      Map<String,Object> contextMap = securityContext.getData();
      if(RUNAS_IDENTITY_IDENTIFIER.equals(key))
      {
         RunAs runAs = securityContext.getOutgoingRunAs();
         //Move the caller RAI to current RAI 
         securityContext.setOutgoingRunAs((RunAs) contextMap.get(CALLER_RAI_IDENTIFIER));
         
         //Clear the Caller RAI
         contextMap.remove(CALLER_RAI_IDENTIFIER); 
         return (T) runAs;
      }
      return (T) contextMap.remove(key);
   } 

   @Override
   public void setRoles(RoleGroup roles)
   {
      validateSecurityContext(); 
      securityContext.getSubjectInfo().setRoles(roles);  
   }

   
   @Override
   public void setSecurityIdentity(SecurityIdentity sidentity)
   {
      createSubjectInfo(sidentity.getPrincipal(), sidentity.getCredential(),
            sidentity.getSubject());  
      securityContext.setOutgoingRunAs(sidentity.getOutgoingRunAs());
      securityContext.setIncomingRunAs(sidentity.getIncomingRunAs()); 
   }

   @Override
   public SecurityIdentity getSecurityIdentity()
   {
      return new SecurityIdentity(securityContext.getSubjectInfo(), 
            securityContext.getOutgoingRunAs(), securityContext.getIncomingRunAs());
   }
   
   
   //PRIVATE METHODS 
   private void setRunAsIdentity(RunAsIdentity rai)
   {
      Map<String,Object> contextMap = securityContext.getData();
      
      //Move the current RAI on the sc into the caller rai
      RunAs currentRA = securityContext.getOutgoingRunAs(); 
      contextMap.put(CALLER_RAI_IDENTIFIER, currentRA);
      
      securityContext.setOutgoingRunAs(rai); 
   }
   
   
   @Override
   public RoleGroup getRoles()
   {
      validateSecurityContext();
      return securityContext.getSubjectInfo().getRoles(); 
   }

   // Private Methods
   private void validateSecurityContext()
   {
      if(securityContext == null)
         throw PicketBoxMessages.MESSAGES.invalidNullProperty("securityDomain");
   }
}