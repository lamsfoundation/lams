package org.jboss.security.acl;

import java.util.Collection;

import org.jboss.security.authorization.Resource;

/**
 * <p>
 * This interface defines the methods that must be implemented by classes that manage the persistence of 
 * {@code ACL}s. It is used by the {@code ACLProvider} to obtain the {@code ACL}s that are used in the
 * instance-based authorization checks.
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
public interface ACLPersistenceStrategy
{

   /**
    * <p>
    * Obtains a reference to the {@code ACL} associated to the given resource.
    * </p>
    * 
    * @param resource   the {@code Resource} for which the associated ACL is wanted.
    * @return   a reference to the {@code ACL} associated with the resource, or null if no
    * ACL could be found.
    */
   public ACL getACL(Resource resource);

   /**
    * <p>
    * Obtains all {@code ACL}s that are managed by this {@code ACLPersistenceStrategy}.
    * </p>
    * 
    * @return   a {@code Collection} containing all {@code ACL}s retrieved by this strategy.
    */
   public Collection<ACL> getACLs();

   /**
    * <p>
    * Creates a new {@code ACL} and associates it to the given resource.
    * </p>
    * 
    * @param resource   the {@code Resource} for which an ACL is to be created.
    * @return   a reference to the created {@code ACL}.
    */
   public ACL createACL(Resource resource);

   /**
    * <p>
    * Creates a new {@code ACL} with the specified entries and associates it to the given resource.
    * </p>
    * 
    * @param resource   the {@code Resource} for which an ACL is to be created.
    * @param entries    a {@code Collection} containing the entries that must be added to the {@code ACL}.
    * @return   a reference to the created {@code ACL}.
    */
   public ACL createACL(Resource resource, Collection<ACLEntry> entries);

   /**
    * <p>
    * Updates the given {@code ACL}. This usually means updating the repository where the ACLs are stored.
    * </p>
    * 
    * @param acl the {@code ACL} that needs to be updated.
    * @return   {@code true} if the ACL was updated; {@code false} otherwise.
    */
   public boolean updateACL(ACL acl);

   /**
    * <p>
    * Removes the given {@code ACL}, breaking the existing association with the resource it relates to.
    * </p>
    * 
    * @param acl    a reference to the {@code ACL} that is to be removed.
    * @return   {@code true} if the ACL was removed; {@code false} otherwise.
    */
   public boolean removeACL(ACL acl);

   /**
    * <p>
    * Removes the {@code ACL} associated with the specified resource.
    * </p>
    * 
    * @param resource    the {@code Resource} whose associated ACL is to be removed.
    * @return   {@code true} if the ACL was removed; {@code false} otherwise.
    */
   public boolean removeACL(Resource resource);

}
