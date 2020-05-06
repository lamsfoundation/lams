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

import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.SecurityContext;
import org.jboss.security.audit.AuditEvent;
import org.jboss.security.audit.AuditLevel;
import org.jboss.security.audit.AuditManager;
import org.jboss.security.authorization.AuthorizationException;
import org.jboss.security.authorization.PolicyRegistration;
import org.jboss.security.authorization.Resource;

/**
 *  Abstract Java EE Security Helper
 *  that does both programmatic as well as 
 *  regular security
 *  @author Anil.Saldhana@redhat.com
 *  @since  Apr 17, 2008 
 *  @version $Revision$
 */
public abstract class AbstractJavaEEHelper
{ 
   protected SecurityContext securityContext;
   protected PolicyRegistration policyRegistration;
   
   public SecurityContext getSecurityContext()
   {
      return this.securityContext;
   }
   
   public void setSecurityContext(SecurityContext sc)
   {
      if(sc == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("security context");
      this.securityContext = sc;
   }
    
   public PolicyRegistration getPolicyRegistration()
   {
      return policyRegistration;
   }

   public void setPolicyRegistration(PolicyRegistration policyRegistration)
   {
      this.policyRegistration = policyRegistration;
   }

   public Principal getCallerPrincipal()
   {
      return AccessController.doPrivileged(new PrivilegedAction<Principal>()
      {
         public Principal run()
         { 
            Principal caller = null;

            if(securityContext != null)
            {
               caller = securityContext.getIncomingRunAs(); 
               //If there is no caller run as, use the call principal
               if(caller == null)
                  caller = securityContext.getUtil().getUserPrincipal();
            }
            return caller;
         }
      });
   }
   
   protected void authorizationAudit(String level, Resource resource, Exception e)
   {
      if(securityContext.getAuditManager() == null)
         return;
      Map<String, Object> contextualMap = resource.getMap();
      Map<String,Object> auditContextMap = new HashMap<String,Object>(contextualMap.size() + 3);
      auditContextMap.putAll(contextualMap);
      auditContextMap.put("Resource:", resource);
      auditContextMap.put("Action", "authorization");
      if (e != null) {
         //Authorization Exception stacktrace is huge. Scale it down
         //as the original stack trace can be seen in server.log (if needed)
         String exceptionMessage = e != null ? e.getLocalizedMessage() : "";  
         auditContextMap.put("Exception:", exceptionMessage);
      }
      if (e instanceof AuthorizationException) {
         // changing level of audit, since in case of AuthorizationException it is FAILURE
         audit(AuditLevel.FAILURE, auditContextMap, null);
      }
      else {
         audit(level, auditContextMap, null);
      }
   }  
   
   protected void authenticationAudit(String level, Map<String,Object> contextMap, Exception e)
   {
      if (contextMap != null) {
         contextMap.put("Action", "authentication");
      }
      audit(level, contextMap, e);
   }
   
   protected void audit(String level,
         Map<String,Object> contextMap, Exception e)
   { 
      AuditManager am = securityContext.getAuditManager();
      if(am == null)
         return;
      contextMap.put("Source", getClass().getName());
      AuditEvent ae = new AuditEvent(level,contextMap,e); 
      am.audit(ae);
   }    
   
   protected Map<String,Object> getContextMap(Principal principal, String methodName)
   {
      Map<String,Object> cmap = new HashMap<String,Object>();
      cmap.put("principal", principal);
      cmap.put("method", methodName);
      return cmap;
   }  
}