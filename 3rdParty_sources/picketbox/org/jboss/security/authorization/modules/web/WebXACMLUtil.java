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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.authorization.util.JBossXACMLUtil;
import org.jboss.security.identity.Role;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.xacml.core.model.context.ActionType;
import org.jboss.security.xacml.core.model.context.AttributeType;
import org.jboss.security.xacml.core.model.context.EnvironmentType;
import org.jboss.security.xacml.core.model.context.RequestType;
import org.jboss.security.xacml.core.model.context.ResourceType;
import org.jboss.security.xacml.core.model.context.SubjectType;
import org.jboss.security.xacml.factories.RequestAttributeFactory;
import org.jboss.security.xacml.factories.RequestResponseContextFactory;
import org.jboss.security.xacml.interfaces.RequestContext;
import org.jboss.security.xacml.interfaces.XACMLConstants;
 
//$Id: WebXACMLUtil.java 46543 2006-07-27 20:22:05Z asaldhana $

/**
 *  Utility class for creating XACML Requests
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jun 21, 2006 
 *  @version $Revision: 46543 $
 */
public class WebXACMLUtil extends JBossXACMLUtil
{

   @SuppressWarnings("unchecked")
   public RequestContext createXACMLRequest(HttpServletRequest request,
         RoleGroup callerRoles) throws Exception
         { 
      if(request == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("request");
      if(callerRoles == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("callerRoles");
      String httpMethod = request.getMethod();
      String action = "GET".equals(httpMethod) ? "read" : "write";

      //Non-standard uri
      String actionURIBase = "urn:oasis:names:tc:xacml:2.0:request-param:attribute:";

      Principal principal = request.getUserPrincipal(); 


      RequestContext requestCtx = RequestResponseContextFactory.createRequestCtx();

      //Create a subject type
      SubjectType subject = new SubjectType();
      subject.getAttribute().add(
            RequestAttributeFactory.createStringAttributeType(
                  XACMLConstants.ATTRIBUTEID_SUBJECT_ID, 
                  "jboss.org",
                  principal.getName()));

      Collection<Role> rolesList = callerRoles.getRoles();
      if(rolesList != null)
      {
         for(Role role:rolesList)
         {
            String roleName = role.getRoleName(); 
            AttributeType attSubjectID = RequestAttributeFactory.createStringAttributeType(
                  XACMLConstants.ATTRIBUTEID_ROLE, "jboss.org", roleName);
            subject.getAttribute().add(attSubjectID); 
         }
      } 

      //Create a resource type
      ResourceType resourceType = new ResourceType();
      resourceType.getAttribute().add(
            RequestAttributeFactory.createAnyURIAttributeType(
                  XACMLConstants.ATTRIBUTEID_RESOURCE_ID, 
                  null, 
                  new URI(request.getRequestURI())));

      //Create an action type
      ActionType actionType = new ActionType();
      actionType.getAttribute().add(
            RequestAttributeFactory.createStringAttributeType(
                  XACMLConstants.ATTRIBUTEID_ACTION_ID, 
                  "jboss.org", 
                  action));

      Enumeration<String> enumer = request.getParameterNames();
      while(enumer.hasMoreElements())
      {
         String paramName = enumer.nextElement();
         String paramValue = request.getParameter(paramName);
         URI actionUri = new URI(actionURIBase + paramName);
         actionType.getAttribute().add(
               RequestAttributeFactory.createStringAttributeType(
                     actionUri.toASCIIString(), 
                     "jboss.org", 
                     paramValue));  
      }


      //Create an Environment Type (Optional)
      EnvironmentType environmentType = new EnvironmentType();
      environmentType.getAttribute().add( RequestAttributeFactory.createDateTimeAttributeType(
            XACMLConstants.ATTRIBUTEID_CURRENT_TIME, null));

      //Create a Request Type
      RequestType requestType = new RequestType();
      requestType.getSubject().add(subject);
      requestType.getResource().add(resourceType);
      requestType.setAction(actionType);
      requestType.setEnvironment(environmentType);

      requestCtx.setRequest(requestType);


      if(PicketBoxLogger.LOGGER.isDebugEnabled())
      {
         ByteArrayOutputStream baos = null;
         try
         {
            baos = new ByteArrayOutputStream();
            requestCtx.marshall(baos);
            PicketBoxLogger.LOGGER.debug(new String(baos.toByteArray()));
         }
         catch(IOException e)
         {  
         }
         finally
         {
            safeClose(baos);
         }
      }
      return requestCtx;
   }
   
   private void safeClose(OutputStream os)
   {
      try
      {
         if(os != null)
         {
            os.close();
         }
      }
      catch(Exception e)
      {}
   }
}