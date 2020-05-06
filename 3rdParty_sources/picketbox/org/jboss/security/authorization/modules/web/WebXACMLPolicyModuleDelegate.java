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
package org.jboss.security.authorization.modules.web;

import java.security.Principal;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.jacc.PolicyContext;
import javax.servlet.http.HttpServletRequest;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.authorization.AuthorizationContext;
import org.jboss.security.authorization.PolicyRegistration;
import org.jboss.security.authorization.Resource;
import org.jboss.security.authorization.ResourceKeys;
import org.jboss.security.authorization.modules.AuthorizationModuleDelegate;
import org.jboss.security.authorization.resources.WebResource;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.xacml.interfaces.PolicyDecisionPoint;
import org.jboss.security.xacml.interfaces.RequestContext;
import org.jboss.security.xacml.interfaces.ResponseContext;
import org.jboss.security.xacml.interfaces.XACMLConstants;

//$Id: WebXACMLPolicyModuleDelegate.java 46543 2006-07-27 20:22:05Z asaldhana $

/**
 *  XACML based authorization module helper that deals with the web layer 
 *  authorization decisions
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jun 13, 2006 
 *  @version $Revision: 46543 $
 */
public class WebXACMLPolicyModuleDelegate extends AuthorizationModuleDelegate
{ 
   private String policyContextID = null;
   
   /**
    * @see AuthorizationModuleDelegate#authorize(org.jboss.security.authorization.Resource, javax.security.auth.Subject, org.jboss.security.identity.RoleGroup)
    */
   public int authorize(Resource resource, Subject subject, RoleGroup role)
   {
      if(resource instanceof WebResource == false)
         throw PicketBoxMessages.MESSAGES.invalidType(WebResource.class.getName());

      WebResource webResource = (WebResource) resource;
      
      //Get the contextual map
      Map<String,Object> map = resource.getMap();
      if(map == null)
         throw PicketBoxMessages.MESSAGES.invalidNullProperty("resourceMap");

      HttpServletRequest request = (HttpServletRequest)webResource.getServletRequest();
      this.policyRegistration = (PolicyRegistration) map.get(ResourceKeys.POLICY_REGISTRATION);
      if(this.policyRegistration == null)
         throw PicketBoxMessages.MESSAGES.invalidNullProperty("policyRegistration");
      this.policyContextID = webResource.getPolicyContextID();
      
      Boolean userDataCheck = checkBooleanValue((Boolean)map.get(ResourceKeys.USERDATA_PERM_CHECK));
      Boolean roleRefCheck = checkBooleanValue((Boolean)map.get(ResourceKeys.ROLEREF_PERM_CHECK)); 
      
      //If it is a userDataCheck or a RoleRefCheck, then the base class (RealmBase) decision holds
      if(userDataCheck || roleRefCheck)
         return AuthorizationContext.PERMIT; //Base class decision holds good
      
      if(request == null)
         throw PicketBoxMessages.MESSAGES.invalidNullProperty("servletRequest");

      return process(request, role);
   } 
   
   /**
    * Ensure that the bool is a valid value
    * @param bool
    * @return bool or Boolean.FALSE (when bool is null)
    */
   private Boolean checkBooleanValue(Boolean bool)
   {
      if(bool == null)
         return Boolean.FALSE;
      return bool;
   } 
   
   /**
    * Process the web request
    * @param request
    * @param callerRoles
    * @return
    */ 
   private int process(HttpServletRequest request, RoleGroup callerRoles ) 
   { 
      Principal userP = request.getUserPrincipal();
      if(userP == null)
         throw PicketBoxMessages.MESSAGES.invalidNullProperty("userPrincipal");

      int result = AuthorizationContext.DENY;
      WebXACMLUtil util = new WebXACMLUtil();
      try
      {
         RequestContext requestCtx = util.createXACMLRequest(request,callerRoles);
         if(this.policyContextID == null)
           this.policyContextID = PolicyContext.getContextID();
          
         PolicyDecisionPoint pdp = util.getPDP(this.policyRegistration, this.policyContextID);
         ResponseContext response = pdp.evaluate(requestCtx);
         result = response.getDecision() == XACMLConstants.DECISION_PERMIT ? 
               AuthorizationContext.PERMIT : AuthorizationContext.DENY; 
      }
      catch(Exception e)
      {
         PicketBoxLogger.LOGGER.debugIgnoredException(e);
         result = AuthorizationContext.DENY;
      }  
      return result;
   } 
 }