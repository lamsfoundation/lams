/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors. 
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
package org.jboss.security.acl;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.authorization.AuthorizationException;
import org.jboss.security.authorization.Resource;
import org.jboss.security.authorization.ResourceKeys;
import org.jboss.security.identity.Identity;

/**
 * <p>
 * This class is the standard {@code ACLProvider} implementation. The access control decisions are based on the name of
 * the specified identity (that is, it assumes that entries in an ACL are keyed by the name of the identity and not by
 * other attributes, like the its roles).
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
public class ACLProviderImpl implements ACLProvider
{

   private static final String PERSISTENCE_STRATEGY_OPTION = "persistenceStrategy";

   private static final String CHECK_PARENT_ACL_OPTION = "checkParentACL";
   
   /** persistence strategy used to retrieve the ACLs */
   protected ACLPersistenceStrategy strategy;

   private boolean checkParentACL;
   
   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.security.acl.ACLProvider#initialize(java.util.Map, java.util.Map)
    */
   public void initialize(Map<String, Object> sharedState, Map<String, Object> options)
   {
      String strategyClassName = (String) options.get(PERSISTENCE_STRATEGY_OPTION);
      if (strategyClassName == null)
         strategyClassName = "org.jboss.security.acl.JPAPersistenceStrategy";

      this.checkParentACL = Boolean.valueOf((String) options.get(CHECK_PARENT_ACL_OPTION)); 
         
      try
      {
         Class<?> strategyClass = this.loadClass(strategyClassName);
         this.strategy = (ACLPersistenceStrategy) strategyClass.newInstance();
      }
      catch (Exception e)
      {
         throw PicketBoxMessages.MESSAGES.unableToCreateACLPersistenceStrategy(e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.security.acl.ACLProvider#getEntitlements(java.lang.Class,
    *      org.jboss.security.authorization.Resource, org.jboss.security.identity.Identity)
    */
   @SuppressWarnings("unchecked")
   public <T> Set<T> getEntitlements(Class<T> clazz, Resource resource, Identity identity)
         throws AuthorizationException
   {
      // currently we only provide sets of EntitlementEntry objects.
      if (!EntitlementEntry.class.equals(clazz))
         return null;

      Set<EntitlementEntry> entitlements = new HashSet<EntitlementEntry>();
      // get the initial permissions - those that apply to the specified resource.
      ACLPermission permission = this.getInitialPermissions(resource, identity.getName());
      if (permission != null)
         this.fillEntitlements(entitlements, resource, identity.getName(), permission);
      return (Set<T>) entitlements;
   }

   /**
    * <p>
    * Helper method that populates the {@code entitlements} collection as it traverses through the resources. The
    * resources are visited using a depth-first search algorithm, and when each node is visited one of the following
    * happens:
    * <li>
    * <ul>
    * an ACL for the resource is located and there is an entry for the identity - the permissions assigned to the
    * identity are used to construct the {@code EntitlementEntry} object and this object is added to the collection. The
    * method is then called recursively for each one of the resource's children passing the permissions that were
    * extracted from the ACL.
    * </ul>
    * <ul>
    * an ACL for the resource is found, but there is no entry for the identity - this means the identity doesn't have
    * any permissions regarding the specified resource. Thus, no {@code EntitlementEntry} object is constructed and the
    * method simply returns. No child resources are processed as it is assumed that the identity doesn't have the right
    * to do anything in the resource's subtree.
    * </ul>
    * <ul>
    * no ACL is found - this means that the resource itself is not protected by any ACL. We assume that if a parent
    * resource has an ACL, then the permissions assigned to the parent's ACL should be used.
    * </ul>
    * </li>
    * </p>
    * 
    * @param entitlements a reference for the collection of {@code EntitlementEntry} objects that is being constructed.
    * @param resource the {@code Resource} being visited.
    * @param identityName a {@code String} representing the identity for which the entitlements are being built.
    * @param permission the {@code ACLPermission} to be used in case no ACL is found for the resource being visited.
    */
   @SuppressWarnings("unchecked")
   protected void fillEntitlements(Set<EntitlementEntry> entitlements, Resource resource, String identityName,
         ACLPermission permission)
   {
      ACLPermission currentPermission = permission;

      ACL acl = this.strategy.getACL(resource);
      if (acl != null)
      {
         ACLEntry entry = acl.getEntry(identityName);
         // null entry means the identity has no permissions over the specified resource.
         if (entry == null)
            return;
         currentPermission = entry.getPermission();
         entitlements.add(new EntitlementEntry(resource, currentPermission, identityName));
      }
      else
      {
         // if resource's ACL is null, build an entry using the permission parameter.
         entitlements.add(new EntitlementEntry(resource, currentPermission, identityName));
      }

      // iterate through the sub-resources (if any), adding their entries to the entitlements collection.
      Collection<Resource> childResources = (Collection<Resource>) resource.getMap().get(ResourceKeys.CHILD_RESOURCES);
      if (childResources != null)
      {
         for (Resource childResource : childResources)
            fillEntitlements(entitlements, childResource, identityName, currentPermission);
      }
   }

   /**
    * <p>
    * This method retrieves the permissions the specified identity has over the specified resource. It starts by looking
    * for the resource's ACL. If one is found and if the ACL has entry for the identity, the respective permissions are
    * returned. If no entry is found, we assume the identity hasn't been assigned any permissions and {@code null} is
    * returned.
    * </p>
    * <p>
    * If the resource doesn't have an associated ACL, we start looking for an ACL in the parent resource recursively,
    * until an ACL is located or until no parent resource is found. In the first case, the algorithm described above is
    * used to return the identity's permissions. In the latter case, we return all permissions (lack of an ACL means
    * that the resource is not protected and the user should be granted all permissions).
    * </p>
    * 
    * @param resource the {@code Resource} for which we want to discover the permissions that have been assigned to the
    *            specified identity.
    * @param identityName a {@code String} representing the identity for which we want to discover the permissions
    *            regarding the specified resource.
    * @return an {@code ACLPermission} containing the permissions that have been assigned to the identity with respect
    *         to the specified resource, or {@code null} if the identity has no permissions at all.
    */
   protected ACLPermission getInitialPermissions(Resource resource, String identityName)
   {
      ACL acl = this.strategy.getACL(resource);
      // if no ACL was found, try to find a parent ACL.
      if (acl == null)
      {
         Resource parent = (Resource) resource.getMap().get(ResourceKeys.PARENT_RESOURCE);
         if (parent != null)
            return getInitialPermissions(parent, identityName);
         // no ACL was found and no parent resource exists - identity has all permissions as resource is not protected.
         return new CompositeACLPermission(BasicACLPermission.values());
      }
      // if an ACL was found, return the permissions associated with the specified identity.
      ACLEntry entry = acl.getEntry(identityName);
      if (entry != null)
         return entry.getPermission();
      // the absence of an entry means that the identity has no permissions over the specified resource.
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.security.acl.ACLProvider#getPersistenceStrategy()
    */
   public ACLPersistenceStrategy getPersistenceStrategy()
   {
      return this.strategy;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.security.acl.ACLProvider#setPersistenceStrategy(org.jboss.security.acl.ACLPersistenceStrategy)
    */
   public void setPersistenceStrategy(ACLPersistenceStrategy persistenceStrategy)
   {
      if (persistenceStrategy == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("persistenceStrategy");
      this.strategy = persistenceStrategy;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.security.acl.ACLProvider#isAccessGranted(org.jboss.security.authorization.Resource,
    *      org.jboss.security.identity.Identity, org.jboss.security.acl.ACLPermission)
    */
   public boolean isAccessGranted(Resource resource, Identity identity, ACLPermission permission)
         throws AuthorizationException
   {
      ACL acl = this.retrieveACL(resource);
      if (acl != null)
      {
         ACLEntry entry = acl.getEntry(identity);
         if (entry != null)
         {
            // check the permission associated with the identity.
            return entry.checkPermission(permission);
         }
         // no entry for identity = deny access
         return false;
      }
      else
         throw new AuthorizationException(PicketBoxMessages.MESSAGES.unableToLocateACLForResourceMessage(
                 resource != null ? resource.toString() : null));
   }

   /**
    * <p>
    * Retrieves the ACL that is to be used to perform authorization decisions on the specified resource. If an ACL
    * for the specified resource can be located by the strategy, this will be the returned ACL. On the other hand,
    * if no ACL can be located for the resource then the method verifies if the {@code checkParentACL} property has
    * been set:
    * <ol>
    *   <li>if {@code checkParentACL} is true, then check if the resource has a parent resource and try to locate an
    *   ACL for the parent resource recursively. The idea here is that child resources "inherit" the permissions from
    *   the parent resources (instead of providing an ACL that would be a copy of the parent ACL).</li>
    *   <li>if {@code checkParentACL} is false, then {@code null} is returned.</li>
    * </ol>
    * 
    * </p>
    * 
    * @param resource the {@code Resource} that is the target of the authorization decision.
    * @return the {@code ACL} that is to be used to perform authorization decisions on the resource; {@code null} if
    * no ACL can be found for the specified resource.
    */
   private ACL retrieveACL(Resource resource)
   {
      ACL acl = this.strategy.getACL(resource);
      if (acl == null && this.checkParentACL)
      {
         Resource parent = (Resource) resource.getMap().get(ResourceKeys.PARENT_RESOURCE);
         if (parent != null)
            acl = retrieveACL(parent);
      }
      return acl;
   }
   
   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.security.acl.ACLProvider#tearDown()
    */
   public boolean tearDown()
   {
      return true;
   }

   /**
    * <p>
    * Loads the specified class using a {@code PrivilegedExceptionAction}.
    * </p>
    * 
    * @param name a {@code String} containing the fully-qualified name of the class to be loaded.
    * @return a reference to the loaded {@code Class}.
    * @throws PrivilegedActionException if an error occurs while loading the specified class.
    */
   protected Class<?> loadClass(final String name) throws PrivilegedActionException
   {
      return AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>()
      {
         public Class<?> run() throws PrivilegedActionException
         {
            try
            {
               ClassLoader loader = Thread.currentThread().getContextClassLoader();
               return loader.loadClass(name);
            }
            catch (Exception e)
            {
               throw new PrivilegedActionException(e);
            }
         }
      });
   }
}