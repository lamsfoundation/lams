package org.jboss.security.acl;

import java.util.Collection;

import org.jboss.security.authorization.Resource;

/**
 * <p>
 * Interface to register {@code ACL}s.
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
public interface ACLRegistration
{

   /**
    * <p>
    * Registers an {@code ACL} associated with the specified {@code Resource}. This usually means interacting
    * with a {@code ACLPersistenceStrategy} to persist the created {@code ACL}.
    * </p>
    * 
    * @param resource    the {@code Resource} for which an {@code ACL} is to be registered.
    */
   public void registerACL(Resource resource);

   /**
    * <p>
    * Registers an {@code ACL} associated with the specified {@code Resource} using the supplied entries.
    * </p>
    * 
    * @param resource    the {@code Resource} for which an {@code ACL} is to be registered.
    * @param entries    the entries of the {@code ACL} being registered.
    */
   public void registerACL(Resource resource, Collection<ACLEntry> entries);

   /**
    * <p>
    * Deregisters the {@code ACL} associated with the specified resource.
    * </p>
    * 
    * @param resource    the {@code Resource} for which an {@code ACL} is to be deregistered.
    */
   public void deRegisterACL(Resource resource);
}
