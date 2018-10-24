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

import org.jboss.security.PicketBoxMessages;

import javax.security.jacc.*;
import java.security.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A JAAC Policy provider implementation that delegates any non-JACC permissions
 * to the java.security.Policy either passed in to the ctor, or the pre existing
 * Policy.getPolicy value.
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class DelegatingPolicy extends Policy
{
   private static DelegatingPolicy instance;

   /**
    * The system Policy we delegate non-JACC checks to
    */
   private Policy delegate;
   /**
    * Map<String, ContextPolicy> for the JACC context IDs that have been
    * committed.
    */
   private ConcurrentMap<String,ContextPolicy> activePolicies = new ConcurrentHashMap<String,ContextPolicy>();
   /**
    * Map<String, ContextPolicy> for the JACC policies that are in the open
    * state and should be excluded from the active permission set.
    */ 
   private ConcurrentMap<String,ContextPolicy> openPolicies = new ConcurrentHashMap<String,ContextPolicy>();
   /**
    * The Policy proxy returned via the PolicyProxy attribute
    */
   private PolicyProxy policyProxy = new PolicyProxy(this);

   /**
    The types of permissions which should be treated as JACC permission types
    in terms of whether this policy should validate the permission.
    */
   private Class<?>[] externalPermissionTypes = {};

   public synchronized static DelegatingPolicy getInstance()
   {
      if (instance == null)
      {
         instance = new DelegatingPolicy();
      }
      return instance;
   }

   public DelegatingPolicy()
   {
      this(null);
   }

   public DelegatingPolicy(Policy delegate)
   {
      if (delegate == null)
         delegate = Policy.getPolicy();
      this.delegate = delegate;
      if (instance == null)
         instance = this;
      /* When run with a security manager the act of class loading can trigger
      security checks which in turn causes this classes implies method to be
      called as soon as the this class is installed as the Policy implementation.
      The implies method cannot cause class loading to occur before there is
      the delegation to the non-JACC Policy provider or else an infinite
      recursion scenario arises where entrance into implies triggers class
      loading which recurses into implies. Here we load the JACC permission
      classes to ensure we get to the point of being able to delegate non-JACC
      permission to the delegate policy. This is the same type of statement
      performed at the start of implies which was causing the JACC permissions
      to be loaded. See [JBAS-1363].
      */
      Permission permission = new RuntimePermission("test");
      boolean loadedPerms = !(permission instanceof EJBMethodPermission
         || permission instanceof EJBRoleRefPermission
         || permission instanceof WebResourcePermission
         || permission instanceof WebRoleRefPermission
         || permission instanceof WebUserDataPermission);
      // Load PolicyContext as this also can trigger permission checks in implies
      Class<?> c = PolicyContext.class;
   }

   public Class<?>[] getExternalPermissionTypes()
   {
      return externalPermissionTypes;
   }
   public void setExternalPermissionTypes(Class<?>[] externalPermissionTypes)
   {
      if( externalPermissionTypes == null )
         externalPermissionTypes = new Class[0];
      this.externalPermissionTypes = externalPermissionTypes;
   }

   public PermissionCollection getPermissions(ProtectionDomain domain)
   {
      PermissionCollection pc = super.getPermissions(domain);
      PermissionCollection delegated = delegate.getPermissions(domain);
      for (Enumeration<Permission> e = delegated.elements(); e.hasMoreElements();)
      {
         Permission p = (Permission) e.nextElement();
         pc.add(p);
      }
      return pc;
   }

   public boolean implies(ProtectionDomain domain, Permission permission)
   {
      boolean isJaccPermission = permission instanceof EJBMethodPermission
         || permission instanceof EJBRoleRefPermission
         || permission instanceof WebResourcePermission
         || permission instanceof WebRoleRefPermission
         || permission instanceof WebUserDataPermission;
      boolean implied = false;
      // If there are external permission types check them
      if( isJaccPermission == false && externalPermissionTypes.length > 0 )
      {
         Class<?> pc = permission.getClass();
         for(int n = 0; n < externalPermissionTypes.length; n ++)
         {
            Class<?> epc = externalPermissionTypes[n];
            if( epc.isAssignableFrom(pc) )
            {
               isJaccPermission = true;
               break;
            }
         }
      }

      if (isJaccPermission == false)
      {
         // Let the delegate policy handle the check
         implied = delegate.implies(domain, permission);
      }
      else
      { 
         String contextID = PolicyContext.getContextID();
         ContextPolicy contextPolicy = activePolicies.get(contextID);
         if (contextPolicy != null)
            implied = contextPolicy.implies(domain, permission);
         //else
         //   PicketBoxLogger.LOGGER.traceNoPolicyContextForId(contextID);
      }
      // PicketBoxLogger.LOGGER.debugImpliesResult(implied);
      return implied;
   }

   /**
    * Return the permission collection associated with the cs.
    * If there is no active JACC PolicyContext then the delegate value for 
    * getPermissions(CodeSource) is returned. Otherwise the JACC policy context
    * permissions are returned. 
    * 
    * @param cs - the CodeSource  
    * @return the associated permission collection
    */ 
   public PermissionCollection getPermissions(CodeSource cs)
   {
      PermissionCollection pc = null;
      String contextID = PolicyContext.getContextID();
      if (contextID == null)
      {
         pc = delegate.getPermissions(cs);
      }
      else
      {
         ContextPolicy policy = activePolicies.get(contextID);
         if (policy != null)
         {
            pc = policy.getPermissions();
            PermissionCollection delegatePerms = delegate.getPermissions(cs);
            for(Enumeration<Permission> e = delegatePerms.elements();e.hasMoreElements();)
            {
               pc.add(e.nextElement());
            } 
         }
         else
         {
            pc = delegate.getPermissions(cs);
         }
      }
      return pc;
   }
   
   

   /**
    * We dynamically manage the active policies on commit so refresh is a noop.
    * Its not clear from the spec whether committed policies should not be visible
    * until a refresh.
    */
   public void refresh()
   {
      
   }

    /**
     * Access the current ContextPolicy instances
     * @return Map<String, ContextPolicy> of the contextID to policy mappings
     */
    public String listContextPolicies()
    {
        StringBuffer tmp = new StringBuffer("<ActiveContextPolicies>");
        Iterator<String> iter = activePolicies.keySet().iterator();
        while (iter.hasNext())
        {
            String contextID = iter.next();
            ContextPolicy cp = activePolicies.get(contextID);
            tmp.append(cp);
            tmp.append('\n');
        }
        tmp.append("</ActiveContextPolicies>");

        tmp.append("<OpenContextPolicies>");
        iter = openPolicies.keySet().iterator();
        while (iter.hasNext())
        {
            String contextID = iter.next();
            ContextPolicy cp = openPolicies.get(contextID);
            tmp.append(cp);
            tmp.append('\n');
        }
        tmp.append("</OpenContextPolicies>");

        return tmp.toString();
    }

    /**
    * @return A proxy for our Policy interface
    */
   public Policy getPolicyProxy()
   {
      return policyProxy;
   }

   // Policy configuration methods used by the PolicyConfiguration impl

   synchronized ContextPolicy getContextPolicy(String contextID)
      throws PolicyContextException
   {
      ContextPolicy policy = openPolicies.get(contextID);
      if (policy == null)
         throw new PolicyContextException(PicketBoxMessages.MESSAGES.noPolicyContextForIdMessage(contextID));
      return policy;
   }

   /**
    * Create or update a ContextPolicy for contextID. This moves any active
    * policy to the openPolicies map until its committed.
    * 
    * @param contextID
    * @param remove
    * @throws PolicyContextException
    */
   synchronized void initPolicyConfiguration(String contextID, boolean remove)
      throws PolicyContextException
   {
      // Remove from the active policy map
      ContextPolicy policy = activePolicies.remove(contextID);
      if( policy == null )
         policy = openPolicies.get(contextID);
      if (policy == null)
      {
         policy = new ContextPolicy(contextID);
      }
      // Add to the open policy map
      openPolicies.put(contextID, policy);
      if (remove == true)
         policy.clear();
   }

   void addToExcludedPolicy(String contextID, Permission permission)
      throws PolicyContextException
   {
      ContextPolicy policy = getContextPolicy(contextID);
      policy.addToExcludedPolicy(permission);
   }

   void addToExcludedPolicy(String contextID, PermissionCollection permissions)
      throws PolicyContextException
   {
      ContextPolicy policy = getContextPolicy(contextID);
      policy.addToExcludedPolicy(permissions);
   }

   void addToRole(String contextID, String roleName, Permission permission)
      throws PolicyContextException
   {
      ContextPolicy policy = getContextPolicy(contextID);
      policy.addToRole(roleName, permission);
   }

   void addToRole(String contextID, String roleName, PermissionCollection permissions)
      throws PolicyContextException
   {
      ContextPolicy policy = getContextPolicy(contextID);
      policy.addToRole(roleName, permissions);
   }

   void addToUncheckedPolicy(String contextID, Permission permission)
      throws PolicyContextException
   {
      ContextPolicy policy = getContextPolicy(contextID);
      policy.addToUncheckedPolicy(permission);
   }

   void addToUncheckedPolicy(String contextID, PermissionCollection permissions)
      throws PolicyContextException
   {
      ContextPolicy policy = getContextPolicy(contextID);
      policy.addToUncheckedPolicy(permissions);
   }

   void linkConfiguration(String contextID, PolicyConfiguration link)
      throws PolicyContextException
   {
      ContextPolicy policy = getContextPolicy(contextID);
      ContextPolicy linkPolicy = getContextPolicy(link.getContextID());
      policy.linkConfiguration(linkPolicy);
   }

   /**
    * May need to make this synchronized to allow the move from the open to
    * active policy map atomic. Right now the assumption is that a single thread
    * is active for a given contextID.
    * 
    * @param contextID
    * @throws PolicyContextException
    */ 
   public void commit(String contextID)
      throws PolicyContextException
   {
      ContextPolicy policy = getContextPolicy(contextID);
      openPolicies.remove(contextID);
      activePolicies.put(contextID, policy);
      policy.commit();
   }

   public void delete(String contextID)
      throws PolicyContextException
   {
      ContextPolicy policy = activePolicies.remove(contextID);
      if( policy == null )
         policy = openPolicies.remove(contextID);
      if( policy != null )
         policy.delete();
   }
   
   void removeExcludedPolicy(String contextID)
      throws PolicyContextException
   {
      ContextPolicy policy = getContextPolicy(contextID);
      policy.removeExcludedPolicy();
   }

   void removeRole(String contextID, String roleName)
      throws PolicyContextException
   {
      ContextPolicy policy = getContextPolicy(contextID);
      policy.removeRole(roleName);
   }

   void removeUncheckedPolicy(String contextID)
      throws PolicyContextException
   {
      ContextPolicy policy = getContextPolicy(contextID);
      policy.removeUncheckedPolicy();
   }
   
   //Methods used by subclasses
   /**
    * This proxy wrapper restricts the visible methods to only those from the
    * Policy base class.
    */ 
   private static class PolicyProxy extends Policy
   {
      private Policy delegate;

      PolicyProxy(Policy delegate)
      {
         this.delegate = delegate;
      }

      public void refresh()
      {
         delegate.refresh();
      }

      public PermissionCollection getPermissions(CodeSource codesource)
      {
         return delegate.getPermissions(codesource);
      }

      public boolean implies(ProtectionDomain domain, Permission permission)
      {
         return delegate.implies(domain, permission);
      }

      public PermissionCollection getPermissions(ProtectionDomain domain)
      {
         return delegate.getPermissions(domain);
      }
   }
}
