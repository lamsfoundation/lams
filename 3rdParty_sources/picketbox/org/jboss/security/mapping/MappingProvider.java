/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */ 
package org.jboss.security.mapping;

import java.util.Map;

/**
 *  A provider with mapping functionality
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @version $Revision$
 * @param <T>
 *  @since  Aug 24, 2006
 */
public interface MappingProvider<T>
{
   /**
    * Initialize the provider with the configured module options
    * @param options
    */
   void init(Map<String,Object> options);
   
   /**
    * Map the passed object
    * @param map A read-only contextual map that can provide information to the provider
    * @param mappedObject an Object on which the mapping will be applied 
    * @throws IllegalArgumentException if the mappedObject is not understood by the 
    * provider.
    */
    void performMapping(Map<String,Object> map, T mappedObject);
    
    /**
     * Injected by the MappingContext
     * @param result
     */
    void setMappingResult(MappingResult<T> result);
    
    /**
     * Whether this mapping provider supports
     * mapping T
     * @param t
     * @return
     */
    boolean supports(Class<?> p);
} 