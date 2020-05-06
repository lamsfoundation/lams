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

import org.jboss.security.SecurityContext;

/**
 *  Factory to get to the helpers
 *  @author Anil.Saldhana@redhat.com
 *  @since  Apr 18, 2008 
 *  @version $Revision$
 */
public class SecurityHelperFactory
{
   private static String WebAuthorizationHelperClass = 
               "org.jboss.security.plugins.javaee.WebAuthorizationHelper";
   
   private static String EjbAuthorizationHelperClass = 
      "org.jboss.security.plugins.javaee.EJBAuthorizationHelper";
   
   private static Class<?> webAuthorizationHelperClazz;
   private static Class<?> ejbAuthorizationHelperClazz;
      
   /**
    * Get the EJB Authentication Helper given a security context
    * @param sc
    * @return
    */
   public static EJBAuthenticationHelper getEJBAuthenticationHelper(SecurityContext sc)
   {
     return new EJBAuthenticationHelper(sc); 
   }
   
   /**
    * Get the Web Authorization Helper given a security context
    * @param sc
    * @return
    * @throws Exception
    */
   public static AbstractWebAuthorizationHelper getWebAuthorizationHelper(SecurityContext sc) 
   throws Exception
   {
      if(webAuthorizationHelperClazz == null)
      {
         webAuthorizationHelperClazz = SecurityActions.loadClass(WebAuthorizationHelperClass);
      } 
      
      AbstractWebAuthorizationHelper awh = (AbstractWebAuthorizationHelper) webAuthorizationHelperClazz.newInstance();
      awh.setSecurityContext(sc);
      return awh;
   }
   
   /**
    * Get the EJB Authorization Helper given a security context
    * @param sc
    * @return
    * @throws Exception
    */
   public static AbstractEJBAuthorizationHelper getEJBAuthorizationHelper(SecurityContext sc) 
   throws Exception
   {
      if(ejbAuthorizationHelperClazz == null)
      {
         ejbAuthorizationHelperClazz = SecurityActions.loadClass(EjbAuthorizationHelperClass);
      }
      AbstractEJBAuthorizationHelper awh = (AbstractEJBAuthorizationHelper) ejbAuthorizationHelperClazz.newInstance();
      awh.setSecurityContext(sc);
      return awh;
   }
   
   /**
    * Set the FQN of the ejb authorization helper class
    * @param fqn
    */
   public static void setEJBAuthorizationHelperClass(String fqn)
   {
      EjbAuthorizationHelperClass = fqn;
      ejbAuthorizationHelperClazz = null;
   }
   
   /**
    * Set the FQN of the web authorization helper class
    * @param fqn
    */
   public static void setWebAuthorizationHelperClass(String fqn)
   {
      WebAuthorizationHelperClass = fqn;
      webAuthorizationHelperClazz = null;
   }
}