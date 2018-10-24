package org.jboss.security.acl;

import org.jboss.security.authorization.Resource;

/**
 * <p>
 * This interface provides a factory for {@code Resource}s.
 * </p>
 *  
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
public interface ACLResourceFactory
{

   /**
    * <p>
    * Creates an instance of the {@code Resource} with the specified class name and id.
    * </p>
    * 
    * @param resourceClassName  the fully-qualified class name of the {@code Resource}.
    * @param id the unique identifier of the {@code Resource}.
    * @return a reference to the instantiated {@code Resource}.
    */
   public Resource instantiateResource(String resourceClassName, Object id);
}
