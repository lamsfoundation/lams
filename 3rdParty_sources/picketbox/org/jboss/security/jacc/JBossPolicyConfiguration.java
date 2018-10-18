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

import java.security.Permission;
import java.security.PermissionCollection;

import javax.security.jacc.PolicyConfiguration;
import javax.security.jacc.PolicyContextException;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.util.state.IllegalTransitionException;
import org.jboss.security.util.state.State;
import org.jboss.security.util.state.StateMachine;

/** The JACC PolicyConfiguration implementation. This class associates a
 * context id with the permission ops it passes along to the global
 * DelegatingPolicy instance.
 *
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class JBossPolicyConfiguration implements PolicyConfiguration
{
   /** The JACC context id associated with the policy */
   private String contextID;
   /** The Policy impl which handles the JACC permissions */
   private DelegatingPolicy policy;
   /** A state machine whihc enforces the state behavior of this config */
   private StateMachine configStateMachine;

   protected JBossPolicyConfiguration(String contextID, DelegatingPolicy policy, StateMachine configStateMachine)
      throws PolicyContextException
   {
      this.contextID = contextID;
      this.policy = policy;
      this.configStateMachine = configStateMachine;

      if (contextID == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextID");
      if (policy == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("policy");
      if (configStateMachine == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("configStateMachine");

      validateState("getPolicyConfiguration");
      PicketBoxLogger.LOGGER.debugJBossPolicyConfigurationConstruction(contextID);
   }

   void initPolicyConfiguration(boolean remove)
      throws PolicyContextException
   {
      validateState("getPolicyConfiguration");
      policy.initPolicyConfiguration(contextID, remove);
   }

   public void addToExcludedPolicy(Permission permission)
      throws PolicyContextException
   {
      PicketBoxLogger.LOGGER.traceAddPermissionToExcludedPolicy(permission);
      validateState("addToExcludedPolicy");
      policy.addToExcludedPolicy(contextID, permission);
   }
   
   public void addToExcludedPolicy(PermissionCollection permissions)
      throws PolicyContextException
   {
      PicketBoxLogger.LOGGER.traceAddPermissionsToExcludedPolicy(permissions);
      validateState("addToExcludedPolicy");
      policy.addToExcludedPolicy(contextID, permissions);
   }

   public void addToRole(String roleName, Permission permission)
      throws PolicyContextException
   {
      PicketBoxLogger.LOGGER.traceAddPermissionToRole(permission);
      validateState("addToRole");
      policy.addToRole(contextID, roleName, permission);
   }

   public void addToRole(String roleName, PermissionCollection permissions)
      throws PolicyContextException
   {
      PicketBoxLogger.LOGGER.traceAddPermissionsToRole(permissions);
      validateState("addToRole");
      policy.addToRole(contextID, roleName, permissions);
   }

   public void addToUncheckedPolicy(Permission permission)
      throws PolicyContextException
   {
      PicketBoxLogger.LOGGER.traceAddPermissionToUncheckedPolicy(permission);
      validateState("addToUncheckedPolicy");
      policy.addToUncheckedPolicy(contextID, permission);
   }

   public void addToUncheckedPolicy(PermissionCollection permissions)
      throws PolicyContextException
   {
      PicketBoxLogger.LOGGER.traceAddPermissionsToUncheckedPolicy(permissions);
      validateState("addToUncheckedPolicy");
      policy.addToUncheckedPolicy(contextID, permissions);
   }

   public void commit()
      throws PolicyContextException
   {
      PicketBoxLogger.LOGGER.tracePolicyConfigurationCommit(contextID);
      validateState("commit");
      policy.commit(contextID);
   }

   public void delete()
      throws PolicyContextException
   {
      PicketBoxLogger.LOGGER.tracePolicyConfigurationDelete(contextID);
      validateState("delete");
      policy.delete(contextID);
   }

   public String getContextID()
      throws PolicyContextException
   {
      validateState("getContextID");
      return contextID;
   }

   public boolean inService()
      throws PolicyContextException
   {
      validateState("inService");
      State state = configStateMachine.getCurrentState();
      boolean inService = state.getName().equals("inService");
      return inService;
   }

   public void linkConfiguration(PolicyConfiguration link)
      throws PolicyContextException
   {
      if (PicketBoxLogger.LOGGER.isTraceEnabled())
      {
         PicketBoxLogger.LOGGER.traceLinkConfiguration(link.getContextID());
      }
      validateState("linkConfiguration");
      policy.linkConfiguration(contextID, link);
   }

   public void removeExcludedPolicy()
      throws PolicyContextException
   {
      PicketBoxLogger.LOGGER.traceRemoveExcludedPolicy(contextID);
      validateState("removeExcludedPolicy");
      policy.removeExcludedPolicy(contextID);
   }

   public void removeRole(String roleName)
      throws PolicyContextException
   {
      PicketBoxLogger.LOGGER.traceRemoveRole(roleName, contextID);
      validateState("removeRole");
      policy.removeRole(contextID, roleName);
   }

   public void removeUncheckedPolicy()
      throws PolicyContextException
   {
      PicketBoxLogger.LOGGER.traceRemoveUncheckedPolicy(contextID);
      validateState("removeUncheckedPolicy");
      policy.removeUncheckedPolicy(contextID);
   }

   protected void validateState(String action)
      throws PolicyContextException
   {
      try
      {
         configStateMachine.nextState(action);
      }
      catch(IllegalTransitionException e)
      {
         throw new PolicyContextException(PicketBoxMessages.MESSAGES.operationNotAllowedMessage(), e);
      }
   }
}