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
package org.jboss.security.plugins;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.authorization.PolicyRegistration;
import org.jboss.security.xacml.core.JBossPDP;
import org.jboss.security.xacml.factories.PolicyFactory;
import org.jboss.security.xacml.interfaces.XACMLPolicy;

/**
 * Default implementation of Policy Registration interface
 * 
 * @author Anil.Saldhana@redhat.com
 * @since Mar 31, 2008
 * @version $Revision$
 */
public class JBossPolicyRegistration implements PolicyRegistration, Serializable
{
   private static final long serialVersionUID = 1L;

   private final Map<String, Set<XACMLPolicy>> contextIdToXACMLPolicy = new HashMap<String, Set<XACMLPolicy>>();

   /**
    * When the policy configuration file is registered, we directly store a copy of the JBossPDP that has read in the
    * config file
    */
   private final Map<String, JBossPDP> contextIDToJBossPDP = new HashMap<String, JBossPDP>();

   public void deRegisterPolicy(String contextID, String type)
   {
      if (PolicyRegistration.XACML.equalsIgnoreCase(type))
      {
         this.contextIdToXACMLPolicy.remove(contextID);
         PicketBoxLogger.LOGGER.traceDeregisterPolicy(contextID, type);
      }
   }

   @SuppressWarnings("unchecked")
   public <T> T getPolicy(String contextID, String type, Map<String, Object> contextMap)
   {
      if (PolicyRegistration.XACML.equalsIgnoreCase(type))
      {
         if (contextMap != null)
         {
            String pdp = (String) contextMap.get("PDP");
            if (pdp != null)
               return (T) this.contextIDToJBossPDP.get(contextID);
         }
         return (T) this.contextIdToXACMLPolicy.get(contextID);
      }
      throw PicketBoxMessages.MESSAGES.invalidPolicyRegistrationType(type);
   }

   /**
    * @see PolicyRegistration#registerPolicy(String, String, URL)
    */
   public void registerPolicy(String contextID, String type, URL location)
   {
      InputStream is = null;
      try
      {
         if (PicketBoxLogger.LOGGER.isTraceEnabled())
         {
            PicketBoxLogger.LOGGER.traceRegisterPolicy(contextID, type, location != null ? location.getPath() : null);
         }
         is = location.openStream();
         registerPolicy(contextID, type, is);
      }
      catch (Exception e)
      {
         PicketBoxLogger.LOGGER.debugIgnoredException(e);
      }
      finally
      {
         safeClose(is);
      }
   }

   /**
    * @see PolicyRegistration#registerPolicy(String, String, InputStream)
    */
   public void registerPolicy(String contextID, String type, InputStream stream)
   {
      if (PolicyRegistration.XACML.equalsIgnoreCase(type))
      {
         try
         {
            XACMLPolicy policy = PolicyFactory.createPolicy(stream);

            Set<XACMLPolicy> policySet = this.contextIdToXACMLPolicy.get(contextID);
            if (policySet == null)
            {
               policySet = new HashSet<XACMLPolicy>();
            }
            policySet.add(policy);
            this.contextIdToXACMLPolicy.put(contextID, policySet);
         }
         catch (Exception e)
         {
            PicketBoxLogger.LOGGER.debugIgnoredException(e);
         }
      }
   }

   /**
    * @see PolicyRegistration#registerPolicyConfig(String, String, Object)
    */ 
   public <P> void registerPolicyConfig(String contextId, String type, P objectModel)
   {
      if (PolicyRegistration.XACML.equalsIgnoreCase(type))
      {
         if(objectModel instanceof JAXBElement == false)
            throw PicketBoxMessages.MESSAGES.invalidType(JAXBElement.class.getName());

         try
         {
            JAXBElement<?> jaxbModel = (JAXBElement<?>) objectModel;
            JBossPDP pdp = new JBossPDP(jaxbModel);
            this.contextIDToJBossPDP.put(contextId, pdp);
         }
         catch (Exception e)
         {
            throw new RuntimeException(e);
         }
      }
   }
   
   /**
    * @see PolicyRegistration#registerPolicyConfigFile(String, String, InputStream)
    */
   public void registerPolicyConfigFile(String contextId, String type, InputStream stream)
   {
      if (PolicyRegistration.XACML.equalsIgnoreCase(type))
      {
         try
         {
            JBossPDP pdp = new JBossPDP(stream);
            this.contextIDToJBossPDP.put(contextId, pdp);
         }
         catch (Exception e)
         {
            throw new RuntimeException(e);
         }
      }
   }
   private void safeClose(InputStream fis)
   {
      try
      {
         if(fis != null)
         {
            fis.close();
         }
      }
      catch(Exception e)
      {}
   }
}