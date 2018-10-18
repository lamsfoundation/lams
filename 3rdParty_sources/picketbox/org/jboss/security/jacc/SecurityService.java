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
package org.jboss.security.jacc;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.Policy;
import java.security.PrivilegedAction;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.security.jacc.PolicyContext;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.SecurityConstants;

/** The SecurityService installs a java.security.Policy implementation that
 * handles the JACC permission checks. The Policy provider can be done using the
 * standard javax.security.jacc.policy.provider system property, or by setting
 * the PolicyName attribute to an mbean which supports a Policy attribute of
 * type java.security.Policy.
 *
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class SecurityService
{
   /** The system property name for the Policy implementation class */
   private static final String JACC_POLICY_PROVIDER = "javax.security.jacc.policy.provider";

   /** The startup Policy.getPolicy() value */
   private Policy oldPolicy;
   /** The JACC_POLICY_PROVIDER Policy implementation */
   private Policy jaccPolicy;
   /** The mbean name to use if we should register the jaccPolicy as an mbean */
   private ObjectName policyName;

   /** The attribute name on policyName used to obtain the Policy impl */
   private String policyAttributeName = "Policy";
   private MBeanServer server;

   public ObjectName getPolicyName()
   {
      return policyName;
   }
   public void setPolicyName(ObjectName policyName)
   {
      this.policyName = policyName;
   }

   public String getPolicyAttributeName()
   {
      return policyAttributeName;
   }
   public void setPolicyAttributeName(String policyAttributeName)
   {
      this.policyAttributeName = policyAttributeName;
   }

   public MBeanServer getMBeanServer()
   {
      return server;
   }
   public void setMBeanServer(MBeanServer server)
   {
      this.server = server;
   }

   public Policy getPolicy()
   {
      return this.jaccPolicy;
   }
   public void setPolicy(Policy jaccPolicy)
   {
      this.jaccPolicy = jaccPolicy;
   }

   /**
    * The following permissions are required:
    * java.security.SecurityPermission("getPolicy")
    * java.security.SecurityPermission("setPolicy")
    * 
    * @throws Exception
    */ 
   public void start() throws Exception
   {
      // Get the current Policy impl
      oldPolicy = Policy.getPolicy();

      // If the policy is an mbean, first see if it already exists
      if( server != null && policyName != null && server.isRegistered(policyName) )
      {
         // Get the Policy from the mbean
         try
         {
            jaccPolicy = (Policy) server.getAttribute(policyName, policyAttributeName);
         }
         catch(Exception e)
         {
            PicketBoxLogger.LOGGER.debugIgnoredException(e);
         }
      }

      // Use the provider system property if there is no policy
      if( jaccPolicy == null )
      {
         String provider = getProperty(JACC_POLICY_PROVIDER, "org.jboss.security.jacc.DelegatingPolicy");
         ClassLoader loader = SecurityActions.getContextClassLoader();
         Class<?> providerClass = loader.loadClass(provider);
         try
         {
            // Look for a ctor(Policy) signature
            Class<?>[] ctorSig = {Policy.class};
            Constructor<?> ctor = providerClass.getConstructor(ctorSig);
            Object[] ctorArgs = {oldPolicy};
            jaccPolicy = (Policy) ctor.newInstance(ctorArgs);
         }
         catch(NoSuchMethodException e)
         {
            jaccPolicy = (Policy) providerClass.newInstance();
         }
      }

      // Install the JACC policy provider
      Policy.setPolicy(jaccPolicy);

      // Have the policy load/update itself
      jaccPolicy.refresh();

      // Register the default active Subject PolicyContextHandler
      SubjectPolicyContextHandler handler = new SubjectPolicyContextHandler();
      PolicyContext.registerHandler(SecurityConstants.SUBJECT_CONTEXT_KEY,
         handler, true);
   }

   public void stop() throws Exception
   {
      // Install the policy provider that existed on startup
      if( jaccPolicy != null )
         Policy.setPolicy(oldPolicy);      
   }
  
   static class PropertyAccessAction implements PrivilegedAction<String>
   {
      private String name;
      private String defaultValue;
      PropertyAccessAction(String name, String defaultValue)
      {
         this.name = name;
         this.defaultValue = defaultValue;
      }
      public String run()
      {
         return System.getProperty(name, defaultValue);
      }
   }

   static String getProperty(String name)
   {
      return getProperty(name, null);
   }

   static String getProperty(String name, String defaultValue)
   {
      PrivilegedAction<?> action = new PropertyAccessAction(name, defaultValue);
      String property = (String) AccessController.doPrivileged(action);
      return property;
   }
}
