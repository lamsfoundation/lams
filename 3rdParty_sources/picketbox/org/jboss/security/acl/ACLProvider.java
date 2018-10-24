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
package org.jboss.security.acl;

import java.util.Map;
import java.util.Set;

import org.jboss.security.authorization.AuthorizationException;
import org.jboss.security.authorization.Resource;
import org.jboss.security.identity.Identity;

/**
 * <p>
 * An {@code ACLProvider} is responsible for the management of the ACLs associated to the
 * resources being protected. Implementations of this interface will typically interact with some
 * sort of repository, where the ACLs are stored.
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 * @author Anil.Saldhana@redhat.com
 */
public interface ACLProvider
{
   /**
    * Initialize the provider
    * @param sharedState Shared State
    * @param options Options
    */
   public void initialize(Map<String, Object> sharedState, Map<String, Object> options);

   /**
    * <p>
    * For a given Resource and an Identity, return all the entitlements
    * Eg: A portal page can consist of say 10 components such as windows, subpages
    * etc. Now the Portal page can be the resource and for a given identity, the
    * entitlements would be the subset of these 10 components to which the identity 
    * has access
    * </p>
    * @param <T>
    * @param resource
    * @param identity
    * @return
    * @throws AuthorizationException
    */
   public <T> Set<T> getEntitlements(Class<T> clazz, Resource resource, Identity identity)
         throws AuthorizationException;

   /**
    * <p>
    * Checks if the given identity has the permissions needed to access the specified resource. This
    * involves finding the {@code ACL} associated with the resource and consulting the {@code ACL} to
    * determine if access should be granted or not to the identity.
    * </p>
    * 
    * @param resource   the {@code Resource} being accessed.
    * @param identity   the {@code Identity} trying to access the resource.
    * @param permission the permissions needed to access the resource.
    * @return   {@code true} if the identity has enough permissions to access the resource; {@code false}
    * otherwise.
    * @throws AuthorizationException if no {@code ACL} can be found for the specified resource.
    */
   public boolean isAccessGranted(Resource resource, Identity identity, ACLPermission permission)
         throws AuthorizationException;

   /**
    * <p>
    * Obtains the {@code ACLPersistenceStrategy} associated with this provider.
    * </p>
    * 
    * @return   a reference to the {@code ACLPersistenceStrategy} used by this provider.
    */
   public ACLPersistenceStrategy getPersistenceStrategy();

   /**
    * <p>
    * Sets the persistence strategy to be used by this provider.
    * </p>
    * 
    * @param strategy   a reference to the {@code ACLPersistenceStrategy} to be used.
    */
   public void setPersistenceStrategy(ACLPersistenceStrategy strategy);

   /**
    * Give an opportunity for the provider to finalize the 
    * operations
    * @return
    */
   public boolean tearDown();
}
