/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */ 
package org.jboss.security.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  Generic Context used by the Mapping Framework
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @version $Revision$
 * @param <T>
 *  @since  Aug 24, 2006
 */
public class MappingContext<T>
{ 
   private List<MappingProvider<T>> modules = new ArrayList<MappingProvider<T>>();
   private MappingResult<T> result; 
   
   public MappingContext(List<MappingProvider<T>> mod)
   { 
      this.modules = mod;
   }
   
   /**
    * Get the set of mapping modules
    * @return
    */
   public List<MappingProvider<T>> getModules()
   {
      return this.modules;
   }
   
   /**
    * Apply mapping semantics on the passed object
    * @param contextMap Read-only Contextual Map
    * @param mappedObject an object on which mapping will be applied 
    */
   public void performMapping(Map<String,Object> contextMap, T mappedObject)
   {
      int len = modules.size(); 
      
      result = new MappingResult<T>();
      
      for(int i = 0 ; i < len; i++)
      {
         MappingProvider<T> mp = (MappingProvider<T>)modules.get(i);
         mp.setMappingResult(result);
         mp.performMapping(contextMap, mappedObject);
      } 
   } 
   
   /**
    * 
    * @return Result of previous mapping operation
    */
   public MappingResult<T> getMappingResult()
   {
      return result;
   }
   
   /**
    * Optimization Step to determine if we have configured mapping modules
    * to avoid unnecessary mapping step
    * @return true - at least one mapping provider is configured
    */
   public boolean hasModules()
   {
      return this.modules.size() > 0;
   }
}