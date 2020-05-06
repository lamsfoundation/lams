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
package org.jboss.security.authorization.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.authorization.PolicyRegistration;
import org.jboss.security.xacml.core.JBossPDP;
import org.jboss.security.xacml.interfaces.PolicyDecisionPoint;
import org.jboss.security.xacml.interfaces.PolicyLocator;
import org.jboss.security.xacml.interfaces.XACMLPolicy;
import org.jboss.security.xacml.locators.JBossPolicyLocator;
import org.jboss.security.xacml.locators.JBossPolicySetLocator;

/**
 *  Utility class dealing with JBossXACML
 *  @author Anil.Saldhana@redhat.com
 *  @since  Apr 16, 2008 
 *  @version $Revision$
 */
public class JBossXACMLUtil
{
   @SuppressWarnings("unchecked")
   public PolicyDecisionPoint getPDP(PolicyRegistration policyRegistration, String contextID)
   {
      //See if a PDP exists already
      Map<String,Object> contextMap = new HashMap<String,Object>();
      contextMap.put("PDP", "PDP");
      
      PolicyDecisionPoint pdp = null;
      try
      {
         pdp = policyRegistration.getPolicy(contextID,
               PolicyRegistration.XACML, contextMap); 
      }
      catch(Exception ignore)
      {   
      } 
      if(pdp == null)
      {
         Set<XACMLPolicy> policies = (Set<XACMLPolicy>)policyRegistration.getPolicy(contextID,
               PolicyRegistration.XACML, null);
         if(policies == null)
            throw PicketBoxMessages.MESSAGES.missingXACMLPolicyForContextId(contextID);

         JBossPolicyLocator jpl = new JBossPolicyLocator(policies);
         JBossPolicySetLocator jpsl = new JBossPolicySetLocator(policies);
         HashSet<PolicyLocator> plset = new HashSet<PolicyLocator>();
         plset.add(jpl);
         plset.add(jpsl);
         
         pdp = new JBossPDP();
         pdp.setPolicies(policies);
         pdp.setLocators(plset); 
      }
      return pdp;
   } 
}