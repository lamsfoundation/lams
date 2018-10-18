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
package org.jboss.security.authorization;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

//$Id$

/**
 *  Interface to register policies
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jun 18, 2006 
 *  @version $Revision$
 */
public interface PolicyRegistration
{
   /**
    * Represents an xacml policy
    */
   String XACML = "XACML";
   
   /**
    * Represents a JACC policy
    */
   String JACC = "JACC";
   
   /**
    * Represents a Custom policy
    */
   String CUSTOM = "CUSTOM";
   
   /**
    * Register a policy given the location and a context id
    * @param contextID an unique id representing the context
    * @param type type of policy
    * @param location location of the Policy File
    */
   void registerPolicy(String contextID, String type, URL location);
   
   /**
    * 
    * Register a policy given a xml based stream and a context id
    * 
    * @param contextID an unique id representing the context
    * @param type type of policy
    * @param stream InputStream that is an XML stream
    */
   void registerPolicy(String contextID, String type, InputStream stream);
   
   /**
    * Register a policy config file (that lists locations of policies)
    * @param contextId an unique id representing the context
    * @param type type of policy
    * @param stream xml stream
    */
   void registerPolicyConfigFile(String contextId, String type, InputStream stream);
   
   /**
    * Register a Policy Config model such as a JAXB model
    * @param <P> Policy Config model
    * @param contextId
    * @param type
    * @param policyConfig
    */
   <P> void registerPolicyConfig(String contextId, String type, P policyConfig);
   
   /**
    * Unregister a policy  
    * @param contextID Context ID
    * @param type the type of policy
    */
   void deRegisterPolicy(String contextID, String type); 
   
   /**
    * Obtain the registered policy for the context id
    * @param contextID Context ID
    * @param type the type of policy (xacml, jacc, custom etc)
    * @param contextMap A map that can be used by the implementation
    *           to determine the policy choice (typically null)
    */
   <T> T getPolicy(String contextID, String type, Map<String,Object> contextMap);
}