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
package org.jboss.security.mapping.providers.principal;

import java.security.Principal;
import java.util.Map;
import java.util.Properties;

import org.jboss.security.SimplePrincipal;
import org.jboss.security.mapping.MappingResult;

/**
 * A principal mapping provider that takes in a 
 * {@code SimplePrincipal} and converts into
 * a {@code SimplePrincipal} with a different principal name
 * @author Anil.Saldhana@redhat.com
 * @since Feb 5, 2010
 */
public class SimplePrincipalMappingProvider extends AbstractPrincipalMappingProvider
{
   private static final String PRINCIPALS_MAP = "principalsMap"; 

   private MappingResult<Principal> result;

   Properties principalMapProperties = null;

   public void init(Map<String, Object> options)
   { 
      if(options != null)
      { 
         if(options.containsKey(PRINCIPALS_MAP))
         {
            principalMapProperties = (Properties)options.get(PRINCIPALS_MAP);
         } 
      } 

   }

   public void performMapping(Map<String, Object> map, Principal mappedObject)
   {
      if(mappedObject instanceof SimplePrincipal == false)
         return; 
      
      SimplePrincipal simplePrincipal = (SimplePrincipal) mappedObject;
      if(principalMapProperties != null)
      {
         String newPrincipalName = principalMapProperties.getProperty(simplePrincipal.getName());
         if(newPrincipalName != null && newPrincipalName.length() > 0)
         {
            result.setMappedObject(new SimplePrincipal(newPrincipalName));
         }
      }
   }

   public void setMappingResult(MappingResult<Principal> result)
   {
      this.result = result;
   }

}