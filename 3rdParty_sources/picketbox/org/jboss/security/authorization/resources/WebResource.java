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
package org.jboss.security.authorization.resources;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.jboss.security.authorization.Resource;
import org.jboss.security.authorization.ResourceType;

//$Id: WebResource.java 62260 2007-04-11 16:32:33Z anil.saldhana@jboss.com $

/**
 *  Represents a Resource for the Web Layer
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jun 18, 2006 
 *  @version $Revision: 62260 $
 */
public class WebResource extends JavaEEResource
{  
   /** System Property setting to configure the web audit 
    *  off = turn it off
    *  headers = audit the headers
    *  cookies = audit the cookie
    *  parameters = audit the parameters
    *  attributes = audit the attributes
    *  headers,cookies,parameters = audit the headers,cookie and parameters
    *  headers,cookies = audit the headers and cookies
    *  and so on 
    *  
    *  Note: If this flag is not set in the system property, then we get no
    *  audit data for the web request
    * */
   public static final String WEB_AUDIT_FLAG = "org.jboss.security.web.audit";
   
   private ServletRequest servletRequest = null;
   private ServletResponse servletResponse = null;
   
   private String servletName = null;
   
   private String canonicalRequestURI = null;
   
   private static String auditFlag = " ";
   
   static
   {
      auditFlag = SecurityActions.getSystemProperty(WEB_AUDIT_FLAG, " ").toLowerCase(Locale.ENGLISH);
   }
   /**
    * Create a new WebResource.
    */
   public WebResource()
   {   
   }
   
   /**
    * 
    * Create a new WebResource.
    * 
    * @param map Contextual Map
    */
   public WebResource(Map<String,Object> map)
   {
      this.map = map;
   }

   /**
    * @see Resource#getLayer()
    */
   public ResourceType getLayer()
   {
      return ResourceType.WEB;
   } 
   
   public String getCanonicalRequestURI()
   {
      return canonicalRequestURI;
   }

   public void setCanonicalRequestURI(String canonicalRequestURI)
   {
      this.canonicalRequestURI = canonicalRequestURI;
   }

   public ServletRequest getServletRequest()
   {
      return servletRequest;
   }

   public void setServletRequest(ServletRequest servletRequest)
   {
      this.servletRequest = servletRequest;
   } 

   public ServletResponse getServletResponse()
   {
      return servletResponse;
   }

   public void setServletResponse(ServletResponse servletResponse)
   {
      this.servletResponse = servletResponse;
   }

   /**
    * The Servlet for which the authorization request is for
    * @return
    */
   public String getServletName()
   {
      return servletName;
   }

   public void setServletName(String servletName)
   {
      this.servletName = servletName;
   }

   public String toString()
   {
      StringBuffer buf = new StringBuffer();
      buf.append("[").append(getClass().getName()).append(":contextMap=").append(map).
      append(",canonicalRequestURI=").append(this.canonicalRequestURI);
      
      /** Audit the request based on the audit flag */
      if(!auditFlag.contains("off"))
        buf.append(",request=").append(deriveUsefulInfo()).
      
      append(",CodeSource=").append(this.codeSource).
      append("]");
      return buf.toString();
   }
   
   private String deriveUsefulInfo()
   {
      if(servletRequest instanceof HttpServletRequest == false)
         return " ";
      
      HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
      StringBuilder sb = new StringBuilder();
      sb.append("[").append(httpRequest.getContextPath());
      //Append cookies
      if(auditFlag.contains("cookies"))
      {
         sb.append(":cookies=").append(Arrays.toString(httpRequest.getCookies()));   
      }
      //Append Header information
      if(auditFlag.contains("headers"))
      {
         sb.append(":headers=");
         Enumeration<?> en = httpRequest.getHeaderNames();
         for(;en.hasMoreElements();)
         {
            String headerName = (String)en.nextElement();
            sb.append(headerName).append("="); 
            if(headerName.contains("authorization") == false)
               sb.append(httpRequest.getHeader(headerName)).append(",");
         }
         sb.append("]");         
      }
      
      //Append Request parameter information
      if(auditFlag.contains("parameters"))
      {
         sb.append("[parameters=");
         Enumeration<?> enparam = httpRequest.getParameterNames();
         for(;enparam.hasMoreElements();)
         {
            String paramName = (String)enparam.nextElement();
            sb.append(paramName).append("=");
            if (paramName.equalsIgnoreCase("j_password"))
            {
               sb.append("***");
            }
            else
            {
               String[] paramValues = httpRequest.getParameterValues(paramName);
               int len = paramValues != null ? paramValues.length : 0;
               for(int i = 0 ; i < len ; i++)
                  sb.append(paramValues[i]).append("::");
            }
            sb.append(",");
         }
      } 
      //Append Request attribute information
      if(auditFlag.contains("attributes"))
      {
         sb.append("][attributes=");
         Enumeration<?> enu = httpRequest.getAttributeNames();
         for(;enu.hasMoreElements();)
         {
            String attrName = (String)enu.nextElement();
            sb.append(attrName).append("=");
            sb.append(httpRequest.getAttribute(attrName)).append(",");
         }
      }
      sb.append("]");
      return sb.toString();
   } 
}
